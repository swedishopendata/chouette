package mobi.chouette.exchange.noptis.importer;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Color;
import mobi.chouette.common.Context;
import mobi.chouette.common.chain.Command;
import mobi.chouette.common.chain.CommandFactory;
import mobi.chouette.dao.stip.*;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.exchange.noptis.parser.AbstractNoptisParser;
import mobi.chouette.exchange.noptis.parser.CallAndPointInJourneyPattern;
import mobi.chouette.exchange.noptis.parser.VehicleJourneyAndTemplate;
import mobi.chouette.exchange.noptis.parser.VehicleJourneyAtStopWrapper;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.stip.*;
import mobi.chouette.model.stip.type.ArrivalType;
import mobi.chouette.model.stip.type.DepartureType;
import mobi.chouette.model.stip.type.TransportModeCode;
import mobi.chouette.model.stip.util.OffsetDayTime;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private CallOnTimedJourneyPatternDAO callOnTimedJourneyPatternDAO;

    @EJB
    private StopPointDAO stopPointDAO;

    @EJB
    private PointInJourneyPatternDAO pointInJourneyPatternDAO;

    @EJB
    private JourneyPatternPointDAO journeyPatternPointDAO;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {
            Referential referential = (Referential) context.get(REFERENTIAL);
            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());

            List<DirectionOfLine> directionOfLines = directionOfLineDAO.findByDataSourceId(dataSourceId);

            // TODO find out how to get distinct direction of lines entities

            for (DirectionOfLine directionOfLine : directionOfLines) {
                //timetableDao.findVehicleJourneyAndTemplatesForDirectionOfLine(directionOfLine.getGid());
                List<Object[]> resultList = vehicleJourneyTemplateDAO.findVehicleJourneyAndTemplatesForDirectionOfLine(dataSourceId, directionOfLine.getGid());

                List<VehicleJourneyAndTemplate> vehicleJourneyAndTemplates = new ArrayList<>();

                for (Object[] oneRow : resultList) {
                    VehicleJourneyTemplate vehicleJourneyTemplate = (VehicleJourneyTemplate) oneRow[0];
                    VehicleJourney vehicleJourney = (VehicleJourney) oneRow[1];
                    vehicleJourneyAndTemplates.add(new VehicleJourneyAndTemplate(vehicleJourneyTemplate, vehicleJourney));
                }

                List<TimedJourneyPattern> timedJourneyPatterns = timedJourneyPatternDAO.findTimedJourneyPatternsForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                Map<Long, TimedJourneyPattern> timedJourneyPatternMap = new HashMap<>();
                timedJourneyPatterns.forEach(timed -> timedJourneyPatternMap.put(timed.getId(), timed));

                // VehicleJourneys

                for (VehicleJourneyAndTemplate vehicleJourneyAndTemplate : vehicleJourneyAndTemplates) {
                    VehicleJourneyTemplate vehicleJourneyTemplate = vehicleJourneyAndTemplate.getVehicleJourneyTemplate();
                    mobi.chouette.model.stip.VehicleJourney noptisVehicleJourney = vehicleJourneyAndTemplate.getVehicleJourney();

                    // JourneyPatterns start

                    TransportModeCode transportModeCode = vehicleJourneyAndTemplate.getVehicleJourneyTemplate().getTransportModeCode();
                    TimedJourneyPattern timedJourneyPattern = timedJourneyPatternMap.get(vehicleJourneyAndTemplate.getVehicleJourneyTemplate().getIsWorkedOnTimedJourneyPatternId());
                    List<PointInJourneyPattern> pointInJourneyPatterns = pointInJourneyPatternDAO.findByJourneyPatternId(timedJourneyPattern.getIsBasedOnJourneyPatternId());
                    List<JourneyPatternPoint> journeyPatternPointList = new ArrayList<>();

                    for (PointInJourneyPattern pointInJourneyPattern : pointInJourneyPatterns) {
                        JourneyPatternPoint journeyPatternPoint = journeyPatternPointDAO.findByGid(pointInJourneyPattern.getIsJourneyPatternPointGid());
                        journeyPatternPointList.add(journeyPatternPoint);
                    }

//                  List<GtfsShape> gtfsShapeList = createGtfsShapes(transportModeCode, shapeDataHolder, pointInJourneyPatternList, journeyPatternPointList);

                    // Done creating JourneyPatterns

                    long timedJourneyPatternId = vehicleJourneyTemplate.getIsWorkedOnTimedJourneyPatternId();
                    List<Object[]> callObjects = callOnTimedJourneyPatternDAO.findCallsForTimedJourneyPattern(timedJourneyPatternId);
                    List<CallAndPointInJourneyPattern> callAndPointInJourneyPatterns = new ArrayList<>();

                    for (Object[] oneRow : callObjects) {
                        CallOnTimedJourneyPattern callOnTimedJourneyPattern = (CallOnTimedJourneyPattern) oneRow[0];
                        PointInJourneyPattern pointInJourneyPattern = (PointInJourneyPattern) oneRow[1];
                        callAndPointInJourneyPatterns.add(new CallAndPointInJourneyPattern(callOnTimedJourneyPattern, pointInJourneyPattern));
                    }

                    // VehicleJourneyAtStops

                    for (CallAndPointInJourneyPattern callAndPointInJourneyPattern : callAndPointInJourneyPatterns) {
                        VehicleJourneyAtStopWrapper vehicleJourneyAtStop = null;
                        //createOneStopTime(vehicleJourneyTemplate, vehicleJourney, shape, call);

                        CallOnTimedJourneyPattern callOnTimedJourneyPattern = callAndPointInJourneyPattern.getCallOnTimedJourneyPattern();
                        PointInJourneyPattern pointInJourneyPattern = callAndPointInJourneyPattern.getPointInJourneyPattern();

                        BoardingPossibilityEnum boardingPossibility = convertNoptisDepartureTypeToBoardingPossibility(pointInJourneyPattern.getDepartureType());
                        AlightingPossibilityEnum alightingPossibility = convertNoptisArrivalTypeToAlightingPossibility(pointInJourneyPattern.getArrivalType());

                        long journeyPatternPointGid = pointInJourneyPattern.getIsJourneyPatternPointGid();
                        mobi.chouette.model.stip.StopPoint noptisStopPoint = stopPointDAO.findByJourneyPatternPointGid(journeyPatternPointGid);

                        // TODO must fix journey pattern first before calculating distance
/*
                        Double distanceTravelled = null;
                        if (shape != null) {
                            distanceTravelled = shape.getDistanceAtJourneyPatternPoint(call.getPointInJourneyPattern().getSequenceNumber());
                        }
*/

                        //vehicleJourneyAtStop = new VehicleJourneyAtStopWrapper(stopPoint.getGid(), pointInJourneyPattern.getSequenceNumber(), gtfsStopTime.getShapeDistTraveled(), boardingPossibility, alightingPossibility);
                        // convertVehicleJourneyAtStop();
                    }

                    String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            mobi.chouette.model.VehicleJourney.VEHICLEJOURNEY_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    mobi.chouette.model.VehicleJourney vehicleJourney = ObjectFactory.getVehicleJourney(referential, objectId);

                    try {
                        vehicleJourney.setNumber(noptisVehicleJourney.getId());
                    } catch (NumberFormatException e) {
                        vehicleJourney.setNumber(0L);
                        vehicleJourney.setPublishedJourneyName(String.valueOf(noptisVehicleJourney.getId()));
                    }

                    vehicleJourney.setFilled(true);
                }
            }

//            GidPeriodStateMap<DirectionOfLine> directionOfLineMap = new GidPeriodStateMap<>();
//            directionOfLineMap.addAll(allDirectionOfLines);
//            return directionOfLineMap.getAll();


//            InitialContext initialContext = (InitialContext) context.get(INITIAL_CONTEXT);
//            Command parser = CommandFactory.create(initialContext, NoptisVehicleJourneyParserCommand.class.getName());
//            result = parser.execute(context);

            daoContext.setRollbackOnly();

        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
        }

        return result;
    }

    protected void convertVehicleJourneyAtStop(Context context, mobi.chouette.model.stip.VehicleJourney vehicleJourney,
                                               CallOnTimedJourneyPattern callOnTimedJourneyPattern,
                                               mobi.chouette.model.stip.StopPoint noptisStopPoint, VehicleJourneyAtStop vehicleJourneyAtStop) {

        Referential referential = (Referential) context.get(REFERENTIAL);

        vehicleJourneyAtStop.setId(noptisStopPoint.getId());

        mobi.chouette.model.StopPoint neptuneStopPoint = ObjectFactory.getStopPoint(referential, String.valueOf(noptisStopPoint.getGid()));
        vehicleJourneyAtStop.setStopPoint(neptuneStopPoint);

        String arrivalTimeAsString = getArrivalTime(vehicleJourney, callOnTimedJourneyPattern).getAsHourMinuteSecondString();
        Time arrivalTime = toTime(toCalendar(arrivalTimeAsString, "HH:mm:ss"));
        vehicleJourneyAtStop.setArrivalTime(arrivalTime);

        String departureTimeAsString = getDepartureTime(vehicleJourney, callOnTimedJourneyPattern).getAsHourMinuteSecondString();
        Time departureTime = toTime(toCalendar(departureTimeAsString, "HH:mm:ss"));
        vehicleJourneyAtStop.setDepartureTime(departureTime);

        vehicleJourneyAtStop.setArrivalDayOffset(0);
        vehicleJourneyAtStop.setDepartureDayOffset(0);
    }

    private Time toTime(Calendar value) {
        return new Time(getTimeInMillis(value));
    }

    private long getTimeInMillis(Object date) {
        if (date instanceof Timestamp)
            return ((Timestamp) date).getTime();
        if (date instanceof Calendar)
            return ((Calendar) date).getTime().getTime();
        else
            return ((Date) date).getTime();
    }

    private Calendar toCalendar(String value, String pattern) {
        try {
            DateFormat format = new SimpleDateFormat(pattern);
            format.setLenient(false);
            format.parse(value);
            return format.getCalendar();
        } catch (Exception e) {
            log.error(e);
        }

        return null;
    }

    private OffsetDayTime getDepartureTime(mobi.chouette.model.stip.VehicleJourney vehicleJourney, CallOnTimedJourneyPattern callOnTimedJourneyPattern) {
        return calculateOffsetTime(vehicleJourney, chooseDepartureOffsetSeconds(callOnTimedJourneyPattern));
    }

    private OffsetDayTime getArrivalTime(mobi.chouette.model.stip.VehicleJourney vehicleJourney, CallOnTimedJourneyPattern callOnTimedJourneyPattern) {
        return calculateOffsetTime(vehicleJourney, chooseArrivalOffsetSeconds(callOnTimedJourneyPattern));
    }

    private OffsetDayTime calculateOffsetTime(mobi.chouette.model.stip.VehicleJourney vehicleJourney, int offsetSeconds) {
        return vehicleJourney.getPlannedStartOffsetDayTime().plusSeconds(offsetSeconds);
    }

    private int chooseArrivalOffsetSeconds(CallOnTimedJourneyPattern callOnTimedJourneyPattern) {
        if (isArrivalBeforeOrEqualToDeparture(callOnTimedJourneyPattern)) {
            return callOnTimedJourneyPattern.getLatestArrivalTimeOffsetSeconds();
        }
        return arrivalIsAfterDepartureChooseDepartureOrArrivalSeconds(callOnTimedJourneyPattern);
    }

    private int chooseDepartureOffsetSeconds(CallOnTimedJourneyPattern callOnTimedJourneyPattern) {
        if (isArrivalBeforeOrEqualToDeparture(callOnTimedJourneyPattern)) {
            return callOnTimedJourneyPattern.getEarliestDepartureTimeOffsetSeconds();
        }
        return arrivalIsAfterDepartureChooseDepartureOrArrivalSeconds(callOnTimedJourneyPattern);
    }

    private boolean isArrivalBeforeOrEqualToDeparture(CallOnTimedJourneyPattern callOnTimedJourneyPattern) {
        return callOnTimedJourneyPattern.getLatestArrivalTimeOffsetSeconds()
                <= callOnTimedJourneyPattern.getEarliestDepartureTimeOffsetSeconds();
    }

    private int arrivalIsAfterDepartureChooseDepartureOrArrivalSeconds(CallOnTimedJourneyPattern callOnTimedJourneyPattern) {
        int departureOffsetSeconds = callOnTimedJourneyPattern.getEarliestDepartureTimeOffsetSeconds();
        if (departureOffsetSeconds == 0) {
            // The first departure often have a later arrival time in NOPTIS, which makes no sense in GTFS
            // so in those cases we just use the departure offset seconds 0 for both departure and arrival
            return departureOffsetSeconds;
        }
        return callOnTimedJourneyPattern.getLatestArrivalTimeOffsetSeconds();
    }

    public BoardingPossibilityEnum convertNoptisDepartureTypeToBoardingPossibility(DepartureType departureType) {
        if (departureType != null) {
            switch (departureType) {
                case NO_STOP:
                    return BoardingPossibilityEnum.forbidden;
                case STOP_NO_BOARDING:
                    return BoardingPossibilityEnum.forbidden;
                case STOP_BOARDING_IF_NECESSARY:
                    return BoardingPossibilityEnum.request_stop;
                case STOP_BOARDING_ALWAYS:
                    return BoardingPossibilityEnum.normal;
                case FLEXIBLE_STOP:
                    return BoardingPossibilityEnum.is_flexible;
                default:
                    return BoardingPossibilityEnum.normal;
            }
        }

        return BoardingPossibilityEnum.normal;
    }

    public AlightingPossibilityEnum convertNoptisArrivalTypeToAlightingPossibility(ArrivalType arrivalType) {
        if (arrivalType != null) {
            switch (arrivalType) {
                case NO_STOP:
                    return AlightingPossibilityEnum.forbidden;
                case STOP_NO_ALIGHTING:
                    return AlightingPossibilityEnum.forbidden;
                case STOP_ALIGHTING_IF_NECESSARY:
                    return AlightingPossibilityEnum.request_stop;
                case STOP_ALIGHTING_ALWAYS:
                    return AlightingPossibilityEnum.normal;
                case FLEXIBLE_STOP:
                    return AlightingPossibilityEnum.is_flexible;
                default:
                    return AlightingPossibilityEnum.normal;
            }
        }

        return AlightingPossibilityEnum.normal;
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
