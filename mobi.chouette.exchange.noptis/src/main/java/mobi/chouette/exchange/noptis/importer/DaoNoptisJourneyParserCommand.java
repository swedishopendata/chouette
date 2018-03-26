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
import mobi.chouette.model.Route;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.stip.*;
import mobi.chouette.model.stip.type.DirectionCode;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

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

    @EJB
    private PointInJourneyPatternDAO pointInJourneyPatternDAO;

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

            List<DirectionOfLine> directionOfLines = directionOfLineDAO.findByDataSourceAndLineId(dataSourceId, line.getId());
            for (DirectionOfLine directionOfLine : directionOfLines) {

                List<Object[]> resultList = timetableDAO.findVehicleJourneyAndTemplatesForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                List<VehicleJourneyAndTemplate> vehicleJourneyAndTemplates = new ArrayList<>();
                resultList.forEach(resultRecord -> vehicleJourneyAndTemplates.add(new VehicleJourneyAndTemplate(
                        (VehicleJourneyTemplate) resultRecord[0], (VehicleJourney) resultRecord[1])));

                List<TimedJourneyPattern> timedJourneyPatterns = timedJourneyPatternDAO.findTimedJourneyPatternsForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                Map<Long, TimedJourneyPattern> timedJourneyPatternMap = new HashMap<>();
                timedJourneyPatterns.forEach(timedJourneyPattern -> timedJourneyPatternMap.put(timedJourneyPattern.getId(), timedJourneyPattern));

                for (VehicleJourneyAndTemplate vehicleJourneyAndTemplate : vehicleJourneyAndTemplates) {
                    VehicleJourneyTemplate vehicleJourneyTemplate = vehicleJourneyAndTemplate.getVehicleJourneyTemplate();
                    mobi.chouette.model.stip.VehicleJourney noptisVehicleJourney = vehicleJourneyAndTemplate.getVehicleJourney();

                    TimedJourneyPattern timedJourneyPattern = timedJourneyPatternMap.get(vehicleJourneyTemplate.getIsWorkedOnTimedJourneyPatternId());
                    List<Object[]> callPointResultList = timetableDAO.findCallsForTimedJourneyPattern(timedJourneyPattern.getId());
                    List<CallAndPointInJourneyPattern> callAndPointInJourneyPatterns = new ArrayList<>();
                    callPointResultList.forEach(resultRecord -> callAndPointInJourneyPatterns.add(new CallAndPointInJourneyPattern(
                            (CallOnTimedJourneyPattern) resultRecord[0], (PointInJourneyPattern) resultRecord[1])));

                    List<PointInJourneyPattern> pointsInJourneyPattern = pointInJourneyPatternDAO.findByJourneyPatternId(timedJourneyPattern.getIsBasedOnJourneyPatternId());

                    // VehicleJourney

                    NoptisVehicleJourneyParser vehicleJourneyParser = (NoptisVehicleJourneyParser) ParserFactory.create(NoptisVehicleJourneyParser.class.getName());
                    vehicleJourneyParser.setVehicleJourneyAndTemplate(vehicleJourneyAndTemplate);
                    vehicleJourneyParser.setCallAndPointInJourneyPatterns(callAndPointInJourneyPatterns);
                    vehicleJourneyParser.parse(context);

                    String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            mobi.chouette.model.VehicleJourney.VEHICLEJOURNEY_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    mobi.chouette.model.VehicleJourney neptuneVehicleJourney = ObjectFactory.getVehicleJourney(referential, objectId);

                    // Timetable

                    List<LocalDate> operatingDates = timetableDAO.findDatesForVehicleJourney(noptisVehicleJourney.getId());

                    String timetableId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            Timetable.TIMETABLE_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    Timetable timetable = ObjectFactory.getTimetable(referential, timetableId);
                    neptuneVehicleJourney.getTimetables().add(timetable);

                    // Route

                    Route route = createRoute(context, line, directionOfLine, pointsInJourneyPattern);

                    // JourneyPattern

                    NoptisJourneyPatternParser journeyPatternParser = (NoptisJourneyPatternParser) ParserFactory.create(NoptisJourneyPatternParser.class.getName());
                    journeyPatternParser.setTimedJourneyPattern(timedJourneyPattern);
                    journeyPatternParser.setRoute(route);
                    journeyPatternParser.setPointsInJourneyPattern(pointsInJourneyPattern);
                    journeyPatternParser.parse(context);

                    String journeyPatternObjectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            mobi.chouette.model.JourneyPattern.JOURNEYPATTERN_KEY, String.valueOf(timedJourneyPattern.getIsBasedOnJourneyPatternId()));
                    mobi.chouette.model.JourneyPattern neptuneJourneyPattern = ObjectFactory.getJourneyPattern(referential, journeyPatternObjectId);

                    neptuneVehicleJourney.setRoute(neptuneJourneyPattern.getRoute());
                    neptuneVehicleJourney.setJourneyPattern(neptuneJourneyPattern);
                }
            }

            result = SUCCESS;

            daoContext.setRollbackOnly();
            directionOfLineDAO.clear();
            vehicleJourneyTemplateDAO.clear();
            timetableDAO.clear();
            timedJourneyPatternDAO.clear();
            pointInJourneyPatternDAO.clear();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
        }

        return result;
    }

    private Route createRoute(Context context, mobi.chouette.model.stip.Line line, DirectionOfLine directionOfLine, List<PointInJourneyPattern> pointsInJourneyPattern) {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        String lineId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                mobi.chouette.model.Line.LINE_KEY, String.valueOf(line.getGid()));
        mobi.chouette.model.Line neptuneLine = ObjectFactory.getLine(referential, lineId);

        String routeKey = line.getGid() + "_" + directionOfLine.getDirectionCode().ordinal();
        routeKey += "_" + buildStopsKey(context, pointsInJourneyPattern);
        String routeId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), Route.ROUTE_KEY, routeKey);

        Route route = ObjectFactory.getRoute(referential, routeId);
        route.setLine(neptuneLine);

        String wayBack = directionOfLine.getDirectionCode().equals(DirectionCode.EVEN) ? "A" : "R";
        route.setWayBack(wayBack);
        route.setFilled(true);

        return route;
    }

    private String buildStopsKey(Context context, List<PointInJourneyPattern> pointsInJourneyPattern) {
        NoptisReferential noptisReferential = (NoptisReferential) context.get(NOPTIS_REFERENTIAL);
        StringBuilder stopsKey = new StringBuilder();

        for (PointInJourneyPattern pointInJourneyPattern : pointsInJourneyPattern) {
            long journeyPatternPointGid = pointInJourneyPattern.getIsJourneyPatternPointGid();
            StopPoint noptisStopPoint = noptisReferential.getSharedStopPoints().get(journeyPatternPointGid);
            String stopId = String.valueOf(noptisStopPoint.getGid());
            stopId += "_" + getDepartureTypeOrdinal(pointInJourneyPattern) + "_" + getArrivalTypeOrdinal(pointInJourneyPattern);
            stopsKey.append(stopId).append(" ");
        }
        Checksum checksum = new Adler32();
        byte bytes[] = stopsKey.toString().getBytes();
        checksum.update(bytes, 0, bytes.length);
        return Long.toHexString(checksum.getValue());
    }

    private int getDepartureTypeOrdinal(PointInJourneyPattern pointInJourneyPattern) {
        return (pointInJourneyPattern.getDepartureType() == null ? 0 : pointInJourneyPattern.getDepartureType().ordinal());
    }

    private int getArrivalTypeOrdinal(PointInJourneyPattern pointInJourneyPattern) {
        return (pointInJourneyPattern.getArrivalType() == null ? 0 : pointInJourneyPattern.getArrivalType().ordinal());
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
