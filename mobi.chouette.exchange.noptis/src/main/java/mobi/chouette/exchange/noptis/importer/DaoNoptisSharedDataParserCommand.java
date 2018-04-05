package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.*;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.exchange.noptis.importer.util.NoptisReferential;
import mobi.chouette.exchange.noptis.parser.*;
import mobi.chouette.model.stip.*;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;

@Log4j
@Stateless(name = DaoNoptisSharedDataParserCommand.COMMAND)
public class DaoNoptisSharedDataParserCommand implements Command, Constant {

    public static final String COMMAND = "DaoNoptisSharedDataParserCommand";

    @Resource
    private SessionContext daoContext;

    @EJB private StopAreaDAO stopAreaDAO;
    @EJB private StopPointDAO stopPointDAO;
    @EJB private ConnectionLinkDAO connectionLinkDAO;
    @EJB private JourneyPatternPointDAO journeyPatternPointDAO;
    @EJB private TransportAuthorityDAO transportAuthorityDAO;
    @EJB private ContractorDAO contractorDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            NoptisReferential noptisReferential = (NoptisReferential) context.get(NOPTIS_REFERENTIAL);
            short dataSourceId = (short) context.get(NOPTIS_DATA_SOURCE_ID);

            // StopArea

            List<StopArea> stopAreas = stopAreaDAO.findByDataSourceId(dataSourceId);
            stopAreas.forEach(stopArea -> {
                if (!noptisReferential.getSharedStopAreas().containsKey(stopArea.getGid())) {
                    noptisReferential.getSharedStopAreas().put(stopArea.getGid(), stopArea);
                }
            });

            NoptisStopAreaParser noptisStopAreaParser = (NoptisStopAreaParser) ParserFactory.create(NoptisStopAreaParser.class.getName());
            noptisStopAreaParser.setNoptisStopAreas(stopAreas);
            noptisStopAreaParser.parse(context);

            // JourneyPatternPoint

            List<JourneyPatternPoint> journeyPatternPoints = journeyPatternPointDAO.findByDataSourceId(dataSourceId);
            journeyPatternPoints.forEach(journeyPatternPoint -> {
                if (!noptisReferential.getSharedJourneyPatternPoints().containsKey(journeyPatternPoint.getGid())) {
                    noptisReferential.getSharedJourneyPatternPoints().put(journeyPatternPoint.getGid(), journeyPatternPoint);
                }
            });

            // StopPoint

            List<StopPoint> stopPoints = stopPointDAO.findByDataSourceId(dataSourceId);
            for (StopPoint stopPoint : stopPoints) {
                StopArea stopArea = noptisReferential.getSharedStopAreas().get(stopPoint.getIsIncludedInStopAreaGid());
                JourneyPatternPoint journeyPatternPoint = noptisReferential.getSharedJourneyPatternPoints().get(stopPoint.getIsJourneyPatternPointGid());

                NoptisStopPointParser noptisStopPointParser = (NoptisStopPointParser) ParserFactory.create(NoptisStopPointParser.class.getName());
                noptisStopPointParser.setStopPoint(stopPoint);
                noptisStopPointParser.setNoptisStopArea(stopArea);
                noptisStopPointParser.setJourneyPatternPoint(journeyPatternPoint);
                noptisStopPointParser.parse(context);
            }

            stopPoints.forEach(stopPoint -> {
                if (!noptisReferential.getSharedStopPoints().containsKey(stopPoint.getIsJourneyPatternPointGid())) {
                    noptisReferential.getSharedStopPoints().put(stopPoint.getIsJourneyPatternPointGid(), stopPoint);
                }
            });

            // ConnectionLink

            List<ConnectionLink> connectionLinks = connectionLinkDAO.findByDataSourceId(dataSourceId);
            for (ConnectionLink connectionLink : connectionLinks) {
                StopPoint fromStopPoint = noptisReferential.getSharedStopPoints().get(connectionLink.getStartsAtJourneyPatternPointGid());
                StopPoint toStopPoint = noptisReferential.getSharedStopPoints().get(connectionLink.getEndsAtJourneyPatternPointGid());

                if (fromStopPoint == null || toStopPoint == null) {
                    continue;
                }

                NoptisConnectionLinkParser connectionLinkParser = (NoptisConnectionLinkParser) ParserFactory.create(NoptisConnectionLinkParser.class.getName());
                connectionLinkParser.setNoptisConnectionLink(connectionLink);
                connectionLinkParser.setFromStopPoint(fromStopPoint);
                connectionLinkParser.setToStopPoint(toStopPoint);
                connectionLinkParser.parse(context);
            }

            // TransportAuthority

            List<TransportAuthority> transportAuthorities = transportAuthorityDAO.findByDataSourceId(dataSourceId);
            NoptisTransportAuthorityParser transportAuthorityParser = (NoptisTransportAuthorityParser)
                    ParserFactory.create(NoptisTransportAuthorityParser.class.getName());
            transportAuthorityParser.setTransportAuthorities(transportAuthorities);
            transportAuthorityParser.parse(context);

            // Contractor

            List<Contractor> contractors = contractorDAO.findByDataSourceId(dataSourceId);
            NoptisContractorParser contractorParser = (NoptisContractorParser) ParserFactory.create(NoptisContractorParser.class.getName());
            contractorParser.setContractors(contractors);
            contractorParser.parse(context);

            daoContext.setRollbackOnly();
            stopAreaDAO.clear();
            stopPointDAO.clear();
            connectionLinkDAO.clear();
            journeyPatternPointDAO.clear();
            transportAuthorityDAO.clear();
            contractorDAO.clear();

            result = SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
        }

        return result;
    }

    public static class DefaultCommandFactory extends CommandFactory {

        @Override
        protected Command create(InitialContext context) throws IOException {
            Command result = null;
            try {
                String name = "java:app/mobi.chouette.exchange.noptis/" + COMMAND;
                result = (Command) context.lookup(name);
            } catch (NamingException e) {
                // try another way on test context
                String name = "java:module/" + COMMAND;
                try {
                    result = (Command) context.lookup(name);
                } catch (NamingException e1) {
                    log.error(e);
                }
            }
            return result;
        }
    }

    static {
        CommandFactory.factories.put(DaoNoptisSharedDataParserCommand.class.getName(), new DaoNoptisSharedDataParserCommand.DefaultCommandFactory());
    }

}
