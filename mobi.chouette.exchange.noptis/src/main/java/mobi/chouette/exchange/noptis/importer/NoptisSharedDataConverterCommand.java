package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.JourneyPatternPointDAO;
import mobi.chouette.dao.stip.StopAreaDAO;
import mobi.chouette.dao.stip.StopPointDAO;
import mobi.chouette.dao.stip.TransportAuthorityDAO;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.converter.NoptisStopAreaConverter;
import mobi.chouette.exchange.noptis.converter.NoptisTransportAuthorityConverter;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.model.stip.JourneyPatternPoint;
import mobi.chouette.model.stip.StopArea;
import mobi.chouette.model.stip.StopPoint;
import mobi.chouette.model.stip.TransportAuthority;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.List;

@Log4j
@Stateless(name = NoptisSharedDataConverterCommand.COMMAND)
public class NoptisSharedDataConverterCommand implements Command, Constant {

    public static final String COMMAND = "NoptisSharedDataConverterCommand";

    @Resource
    private SessionContext daoContext;

    @EJB
    private TransportAuthorityDAO transportAuthorityDAO;

    @EJB
    private StopAreaDAO stopAreaDAO;

    @EJB
    private StopPointDAO stopPointDAO;

    @EJB
    private JourneyPatternPointDAO journeyPatternPointDAO;

    @Override
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            NoptisImportParameters parameters = (NoptisImportParameters) context.get(Constant.CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(parameters.getObjectIdPrefix());

            List<TransportAuthority> transportAuthorities = transportAuthorityDAO.findByDataSourceId(dataSourceId);

            NoptisTransportAuthorityConverter transportAuthorityConverter = (NoptisTransportAuthorityConverter)
                    ConverterFactory.create(NoptisTransportAuthorityConverter.class.getName());

            for (TransportAuthority transportAuthority : transportAuthorities) {
                context.put(NOPTIS_DATA_CONTEXT, transportAuthority);
                transportAuthorityConverter.convert(context);
            }

            List<StopArea> stopAreas = stopAreaDAO.findByDataSourceId(dataSourceId);

            NoptisStopAreaConverter stopAreaConverter = (NoptisStopAreaConverter)
                    ConverterFactory.create(NoptisStopAreaConverter.class.getName());

            for (StopArea stopArea : stopAreas) {
                context.put(NOPTIS_DATA_CONTEXT, stopArea);
                stopAreaConverter.convert(context);
            }

            List<StopPoint> stopPoints = stopPointDAO.findByDataSourceId(dataSourceId);

            for (StopPoint stopPoint : stopPoints) {
                log.info(stopPoint.toString());

                // TODO get from id mapping cache instead
                //StopArea stopArea = stopDao.findStopArea(stopPoint.getIsIncludedInStopAreaGid());
                //JourneyPatternPoint journeyPatternPoint = stopDao.findJourneyPatternPoint(stopPoint.getIsJourneyPatternPointGid());
            }

            List<JourneyPatternPoint> journeyPatternPoints = journeyPatternPointDAO.findByDataSourceId(dataSourceId);

            for (JourneyPatternPoint journeyPatternPoint : journeyPatternPoints) {
                log.info(journeyPatternPoint.toString());
            }

            result = SUCCESS;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        CommandFactory.factories.put(NoptisSharedDataConverterCommand.class.getName(), new NoptisSharedDataConverterCommand.DefaultCommandFactory());
    }

}
