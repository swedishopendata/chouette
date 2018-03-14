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
import mobi.chouette.exchange.noptis.parser.NoptisStopAreaParser;
import mobi.chouette.model.stip.StopArea;

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
    @EJB private JourneyPatternPointDAO journeyPatternPointDAO;
    @EJB private TransportAuthorityDAO transportAuthorityDAO;
    @EJB private ContractorDAO contractorDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());

            List<StopArea> stopAreas = stopAreaDAO.findByDataSourceId(dataSourceId);
            NoptisStopAreaParser noptisStopAreaParser = (NoptisStopAreaParser) ParserFactory.create(NoptisStopAreaParser.class.getName());
            noptisStopAreaParser.setNoptisStopAreas(stopAreas);
            noptisStopAreaParser.parse(context);

/*
            Long transportAuthorityId = line.getIsDefinedByTransportAuthorityId();
            TransportAuthority transportAuthority = transportAuthorityDAO.find(transportAuthorityId);
            context.put(TRANSPORT_AUTHORITY, transportAuthority);

            InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
            Command parser = CommandFactory.create(initialContext, NoptisLineParserCommand.class.getName());
            result = parser.execute(context);
*/

            daoContext.setRollbackOnly();
            stopAreaDAO.clear();
            stopPointDAO.clear();
            journeyPatternPointDAO.clear();
            transportAuthorityDAO.clear();
            contractorDAO.clear();

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
