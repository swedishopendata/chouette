package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.DirectionOfLineDAO;
import mobi.chouette.dao.stip.TimedJourneyPatternDAO;
import mobi.chouette.dao.stip.TimetableDAO;
import mobi.chouette.dao.stip.VehicleJourneyTemplateDAO;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.exchange.noptis.parser.VehicleJourneyAndTemplate;
import mobi.chouette.model.stip.*;
import mobi.chouette.model.util.Referential;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Stateless(name = DaoNoptisJourneyParserCommand.COMMAND)
public class DaoNoptisJourneyParserCommand implements Command, Constant {

    public static final String COMMAND = "DaoNoptisJourneyParserCommand";

    @Resource
    private SessionContext daoContext;

    @EJB
    private DirectionOfLineDAO directionOfLineDAO;

    @EJB
    private VehicleJourneyTemplateDAO vehicleJourneyTemplateDAO;

    @EJB
    private TimedJourneyPatternDAO timedJourneyPatternDAO;

    @EJB
    private TimetableDAO timetableDAO;

    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {

            Referential referential = (Referential) context.get(REFERENTIAL);
            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());
            Line line = (Line) context.get(LINE);

            // Retrieve DirectionOfLines

            List<DirectionOfLine> directionOfLines = directionOfLineDAO.findByDataSourceAndLineId(dataSourceId, line.getId());
            for (DirectionOfLine directionOfLine : directionOfLines) {
                log.info(directionOfLine);

                // Retrieve VehicleJourneys

                List<Object[]> vehicleJourneysAndTemplates = timetableDAO.findVehicleJourneyAndTemplatesForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                List<VehicleJourneyAndTemplate> vehicleJourneyAndTemplates = new ArrayList<>();

                for (Object[] journeyRecord : vehicleJourneysAndTemplates) {
                    VehicleJourneyTemplate vehicleJourneyTemplate = (VehicleJourneyTemplate) journeyRecord[0];
                    VehicleJourney vehicleJourney = (VehicleJourney) journeyRecord[1];
                    vehicleJourneyAndTemplates.add(new VehicleJourneyAndTemplate(vehicleJourneyTemplate, vehicleJourney));
                }

                // Retrieve TimedJourneyPatterns

                List<TimedJourneyPattern> timedJourneyPatterns = timedJourneyPatternDAO.findTimedJourneyPatternsForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                for (TimedJourneyPattern timedJourneyPattern : timedJourneyPatterns) {
                    log.info(timedJourneyPattern);
                }

                // Iterate all templates and journeys and create a neptune VehicleJourney for each

                for (VehicleJourneyAndTemplate vehicleJourneyAndTemplate : vehicleJourneyAndTemplates) {
                    log.info(vehicleJourneyAndTemplate.getVehicleJourneyTemplate());
                    log.info(vehicleJourneyAndTemplate.getVehicleJourney());
                }
            }

            // TODO consider adding all retrieved objects for the current line in cache (NoptisReferential)

//            InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
//            Command parser = CommandFactory.create(initialContext, NoptisLineParserCommand.class.getName());
//            result = parser.execute(context);

            result = SUCCESS;

            daoContext.setRollbackOnly();
            directionOfLineDAO.clear();
            vehicleJourneyTemplateDAO.clear();
            timetableDAO.clear();

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
        CommandFactory.factories.put(DaoNoptisJourneyParserCommand.class.getName(), new DefaultCommandFactory());
    }

}
