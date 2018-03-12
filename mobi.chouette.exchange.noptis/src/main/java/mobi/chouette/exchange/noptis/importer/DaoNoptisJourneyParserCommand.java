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
import mobi.chouette.model.Company;
import mobi.chouette.model.Route;
import mobi.chouette.model.Timetable;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.stip.*;
import mobi.chouette.model.stip.type.DirectionCode;
import mobi.chouette.model.stip.type.TransportModeCode;
import mobi.chouette.model.stip.util.OffsetDayTime;
import mobi.chouette.model.type.AlightingPossibilityEnum;
import mobi.chouette.model.type.BoardingPossibilityEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import org.apache.commons.lang.StringUtils;

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

    @EJB
    private JourneyPatternPointDAO journeyPatternPointDAO;

    @EJB
    private StopPointDAO stopPointDAO;

    @EJB
    private ContractorDAO contractorDAO;

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

                List<Object[]> resultList = timetableDAO.findVehicleJourneyAndTemplatesForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                List<VehicleJourneyAndTemplate> vehicleJourneyAndTemplates = new ArrayList<>();
                resultList.forEach(resultRecord -> vehicleJourneyAndTemplates.add(new VehicleJourneyAndTemplate(
                        (VehicleJourneyTemplate) resultRecord[0], (VehicleJourney) resultRecord[1])));

                // Retrieve TimedJourneyPatterns

                List<TimedJourneyPattern> timedJourneyPatterns = timedJourneyPatternDAO.findTimedJourneyPatternsForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                for (TimedJourneyPattern timedJourneyPattern : timedJourneyPatterns) {
                    log.info(timedJourneyPattern);
                }

                // Cache all TimedJourneyPatterns for easy access by id later

                Map<Long, TimedJourneyPattern> timedJourneyPatternMap = new HashMap<>();
                timedJourneyPatterns.forEach(timedJourneyPattern -> timedJourneyPatternMap.put(timedJourneyPattern.getId(), timedJourneyPattern));

                // Iterate all templates and journeys and create a neptune VehicleJourney for each

                for (VehicleJourneyAndTemplate vehicleJourneyAndTemplate : vehicleJourneyAndTemplates) {
                    VehicleJourneyTemplate vehicleJourneyTemplate = vehicleJourneyAndTemplate.getVehicleJourneyTemplate();
                    mobi.chouette.model.stip.VehicleJourney noptisVehicleJourney = vehicleJourneyAndTemplate.getVehicleJourney();

                    String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            mobi.chouette.model.VehicleJourney.VEHICLEJOURNEY_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    mobi.chouette.model.VehicleJourney neptuneVehicleJourney = ObjectFactory.getVehicleJourney(referential, objectId);

                    try {
                        neptuneVehicleJourney.setNumber(noptisVehicleJourney.getId());
                    } catch (NumberFormatException e) {
                        neptuneVehicleJourney.setNumber(0L);
                        neptuneVehicleJourney.setPublishedJourneyName(String.valueOf(noptisVehicleJourney.getId()));
                    }

                    // parse and convert Contractor/Operator that operates this journey
                    // TODO consider Contractor to be pre-cached by datasourceId, same as TransportAuthority

                    if (vehicleJourneyTemplate.getContractorGid() != null) {
                        Contractor contractor = contractorDAO.findByGid(vehicleJourneyTemplate.getContractorGid());
                        Company company = ObjectFactory.getCompany(referential, String.valueOf(contractor.getGid()));
                        neptuneVehicleJourney.setCompany(company);
                    }

                    // Parse and convert journey pattern for this journey

                    TimedJourneyPattern timedJourneyPattern = timedJourneyPatternMap.get(vehicleJourneyTemplate.getIsWorkedOnTimedJourneyPatternId());

                    // retrieve the transportcode for the current journey
                    if (vehicleJourneyTemplate.getTransportModeCode() != null) {
                        TransportModeCode transportModeCode = vehicleJourneyTemplate.getTransportModeCode();
                        neptuneVehicleJourney.setTransportMode(AbstractNoptisParser.convertTransportModeCode(transportModeCode));
                    }

                    // retrieve the journey pattern id the current timed journey pattern is based on
                    long journeyPatternId = timedJourneyPattern.getIsBasedOnJourneyPatternId();

                    // this is how the shape key id is composed in stip
                    //String shapeKey = journeyPatternId + transportModeCode.getCodeValue();

                    // now we can retrieve all data required before creating the actual neptune journey pattern

                    // first get all points defined for the current journey pattern
                    List<PointInJourneyPattern> pointsInJourneyPattern = pointInJourneyPatternDAO.findByJourneyPatternId(journeyPatternId);

                    // when we have all points for the current journey pattern, we can collect the actual journey pattern points
                    List<JourneyPatternPoint> journeyPatternPointList = new ArrayList<>(pointsInJourneyPattern.size());


                    // Iterate all points in journey pattern and get corresponding journey pattern point
                    for (PointInJourneyPattern pointInJourneyPattern : pointsInJourneyPattern) {

                        // TODO JourneyPatternPoints is a good candidate for pre-caching, by datasourceId, e.g. when we are caching other types of stops, put in a NoptisReferential in a map and just get by id/gid
                        // TODO that will reduce the number of calls to the below dao operation. We shoulc consider caching all entities for the current datasourceId during init of import where we are only dependent
                        // TODO on the datasourceId and nothing else, this includes lines, stop areas, stop points, pattern points and so on... But for now we use the dao to get by id.

                        JourneyPatternPoint journeyPatternPoint = journeyPatternPointDAO.findByGid(pointInJourneyPattern.getIsJourneyPatternPointGid()); // must be mapped in cache by gid

                        // TODO consider throwing exception here if journeyPatternPoint == null, which means no matching journey pattern point was found for the current point in journey pattern

                        journeyPatternPointList.add(journeyPatternPoint);
                    }

                    // OK, so when we have all these points we should be able to create a journey pattern (list of shapes in gtfs) see ShapeCreator#createGtfsShapes, line 108 for reference
                    // this is probably where we involve RouteLink objects to find the actual route pattern, this is how it is done in stip:

                    // TODO hmm... route link data also seems to be a candidate for pre-caching

                    // TODO Ok, we must do conversion/creation of VehicleJourneyAtStops before creating the JourneyPattern, Find out what we need!

                    // First of all we try to create vehicle journey at stops

                    // VehicleJourneyAtStops

                    List<Object[]> callPointResultList = timetableDAO.findCallsForTimedJourneyPattern(timedJourneyPattern.getId());
                    List<CallAndPointInJourneyPattern> callAndPointInJourneyPatterns = new ArrayList<>();
                    callPointResultList.forEach(resultRecord -> callAndPointInJourneyPatterns.add(new CallAndPointInJourneyPattern(
                            (CallOnTimedJourneyPattern) resultRecord[0], (PointInJourneyPattern) resultRecord[1])));

                    // TODO everything inside this iteration should be moved to a separate parse/convert method specific for stops

                    for (CallAndPointInJourneyPattern callAndPointInJourneyPattern : callAndPointInJourneyPatterns) {
                        //VehicleJourneyAtStopWrapper vehicleJourneyAtStopWrapper = null; // TODO consider using the wrapper to support better data resolution
                        VehicleJourneyAtStop vehicleJourneyAtStop = new VehicleJourneyAtStop();

                        CallOnTimedJourneyPattern callOnTimedJourneyPattern = callAndPointInJourneyPattern.getCallOnTimedJourneyPattern();
                        PointInJourneyPattern pointInJourneyPattern = callAndPointInJourneyPattern.getPointInJourneyPattern();

                        // TODO consider sending these 2 into convert method for a VehicleJourneyAtStop instance
                        BoardingPossibilityEnum boardingPossibility = AbstractNoptisParser.convertDepartureType(pointInJourneyPattern.getDepartureType());
                        AlightingPossibilityEnum alightingPossibility = AbstractNoptisParser.convertArrivalType(pointInJourneyPattern.getArrivalType());

                        // TODO consider StopPoints as a candidate for pre-caching by datasourceId, get from referential instead of dao, for now we're getting from dao
                        mobi.chouette.model.stip.StopPoint noptisStopPoint = stopPointDAO.findByJourneyPatternPointGid(pointInJourneyPattern.getIsJourneyPatternPointGid());

                        // TODO consider using this wrapper constructor instead, to be able to store some extra data (needed?)
                        // TODO We really need the sequence number from PointInJourneyPattern instance!!!
                        //vehicleJourneyAtStop = new VehicleJourneyAtStopWrapper(noptisStopPoint.getGid(), pointInJourneyPattern.getSequenceNumber(), gtfsStopTime.getShapeDistTraveled(), boardingPossibility, alightingPossibility);
                        convert(context, noptisVehicleJourney, callOnTimedJourneyPattern, noptisStopPoint, vehicleJourneyAtStop);

                        vehicleJourneyAtStop.setVehicleJourney(neptuneVehicleJourney);
                    }

                    // Timetable

                    // TODO compute some index key to be used as calendar id see CalendarManager in stip
                    String timetableId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), Timetable.TIMETABLE_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    Timetable timetable = ObjectFactory.getTimetable(referential, timetableId);
                    neptuneVehicleJourney.getTimetables().add(timetable);

                    // JourneyPattern continue

                    Map<String, mobi.chouette.model.JourneyPattern> journeyPatternByStopSequence = new HashMap<>();
                    mobi.chouette.model.JourneyPattern neptuneJourneyPattern = journeyPatternByStopSequence.get("");

                    if (neptuneJourneyPattern == null) {
                        neptuneJourneyPattern = createJourneyPattern(context, referential, configuration, line, neptuneVehicleJourney, "", null, null, null);
                    }

                    //neptuneVehicleJourney.setRoute(journeyPattern.getRoute());
                    //neptuneVehicleJourney.setJourneyPattern(journeyPattern);
                    neptuneVehicleJourney.setFilled(true);
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
            timedJourneyPatternDAO.clear();
            pointInJourneyPatternDAO.clear();
            journeyPatternPointDAO.clear();
            stopPointDAO.clear();
            contractorDAO.clear();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            log.info(Color.MAGENTA + monitor.stop() + Color.NORMAL);
        }

        return result;
    }

    private mobi.chouette.model.JourneyPattern createJourneyPattern(Context context, Referential referential,
                                                                    NoptisImportParameters configuration, Line line,//GtfsTrip gtfsTrip, Iterable<GtfsShape> gtfsShapes,
                                                                    mobi.chouette.model.VehicleJourney vehicleJourney, String journeyKey, Map<String, mobi.chouette.model.JourneyPattern> journeyPatternByStopSequence,
                                                                    List<Route> lstNotShapedRoute, Map<String, List<Route>> mapRoutesByShapes) {

        mobi.chouette.model.JourneyPattern journeyPattern;

        // Route
        //Route route = createRoute(referential, configuration, gtfsTrip, vehicleJourney); // TODO FIX
        Route route = new Route();

        log.info(Color.CYAN + "createJourneyPattern : route " + route.getObjectId() + Color.NORMAL);

        // JourneyPattern
        String journeyPatternId = route.getObjectId().replace(Route.ROUTE_KEY, mobi.chouette.model.JourneyPattern.JOURNEYPATTERN_KEY);
        journeyPattern = ObjectFactory.getJourneyPattern(referential, journeyPatternId);

        if (StringUtils.isNotEmpty(line.getDesignation())) {
            journeyPattern.setName(line.getDesignation());
        } else {
            if (StringUtils.isNotEmpty(line.getName())) {
                journeyPattern.setName(line.getName());
            } else {
                journeyPattern.setName("");
            }
        }

        journeyPattern.setRoute(route);
        //journeyPatternByStopSequence.put(journeyKey, journeyPattern);
        log.info(Color.CYAN + "createJourneyPattern : journeyPattern " + journeyPattern.getObjectId() + Color.NORMAL);

        // StopPoints
        //createStopPoints(route, journeyPattern, vehicleJourney.getVehicleJourneyAtStops(), referential, configuration);

        List<mobi.chouette.model.StopPoint> stopPoints = journeyPattern.getStopPoints();
        journeyPattern.setDepartureStopPoint(stopPoints.get(0));
        journeyPattern.setArrivalStopPoint(stopPoints.get(stopPoints.size() - 1));

        journeyPattern.setFilled(true);
        route.setFilled(true);

        if (route.getName() == null) {
            if (!route.getStopPoints().isEmpty()) {
                String first = route.getStopPoints().get(0).getContainedInStopArea().getName();
                String last = route.getStopPoints().get(route.getStopPoints().size() - 1).getContainedInStopArea().getName();
                route.setName(first + " -> " + last);
            }
        }

        return null;
    }

    private Route createRoute(Referential referential, NoptisImportParameters configuration, Line noptisLine, DirectionOfLine directionOfLine, mobi.chouette.model.VehicleJourney vehicleJourney) {
        String lineId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), mobi.chouette.model.Line.LINE_KEY, String.valueOf(noptisLine.getGid()));
        mobi.chouette.model.Line neptuneLine = ObjectFactory.getLine(referential, lineId);

        String routeKey = noptisLine.getGid() + "_" + directionOfLine.getDirectionCode().ordinal();
        routeKey += "_" + neptuneLine.getRoutes().size();
        //routeKey += "_" + buildStopsKey(vehicleJourney);

        String routeId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), Route.ROUTE_KEY, routeKey);
        log.info(Color.LIGHT_BLUE + "createRoute : route " + routeId + Color.NORMAL);

        Route route = ObjectFactory.getRoute(referential, routeId);
        route.setLine(neptuneLine);

        String wayBack = directionOfLine.getDirectionCode().equals(DirectionCode.EVEN) ? "A" : "R";
        route.setWayBack(wayBack);
        return route;
    }

    private void createStopPoints(Route route, mobi.chouette.model.JourneyPattern journeyPattern, List<VehicleJourneyAtStop> list, Referential referential, NoptisImportParameters configuration) {
        Set<String> stopPointKeys = new HashSet<String>();

        int position = 0;
        for (VehicleJourneyAtStop vehicleJourneyAtStop : list) {
            VehicleJourneyAtStopWrapper wrapper = (VehicleJourneyAtStopWrapper) vehicleJourneyAtStop;
            //String baseKey = route.getObjectId().replace(Route.ROUTE_KEY, mobi.chouette.model.StopPoint.STOPPOINT_KEY) + "a" + wrapper.stopId.trim().replaceAll("[^a-zA-Z_0-9\\-]", "_"); // TODO FIX
            String baseKey = "";
            String stopKey = baseKey;
            int dup = 1;
            while (stopPointKeys.contains(stopKey)) {
                stopKey = baseKey + "_" + (dup++);
            }
            stopPointKeys.add(stopKey);

            mobi.chouette.model.StopPoint stopPoint = ObjectFactory.getStopPoint(referential, stopKey);

            String stopAreaId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), mobi.chouette.model.StopArea.STOPAREA_KEY, wrapper.getStopId());
            mobi.chouette.model.StopArea stopArea = ObjectFactory.getStopArea(referential, stopAreaId);
            stopPoint.setContainedInStopArea(stopArea);
            stopPoint.setRoute(route);
            stopPoint.setPosition(position++);

            //stopPoint.setForBoarding(wrapper.pickUpType);
            //stopPoint.setForAlighting(wrapper.dropOffType);

            journeyPattern.addStopPoint(stopPoint);
            stopPoint.setFilled(true);
        }

        //NeptuneUtil.refreshDepartureArrivals(journeyPattern);
    }

    private String buildStopsKey(mobi.chouette.model.VehicleJourney vehicleJourney) {
        String stopsKey = "";
        for (VehicleJourneyAtStop vjas : vehicleJourney.getVehicleJourneyAtStops()) {
            VehicleJourneyAtStopWrapper vjasw = (VehicleJourneyAtStopWrapper) vjas;
            String stopId = vjasw.getStopId();

            stopId += "_" + getPickUpTypeOrdinal(vjasw) + "_" + getDropOffTypeOrdinal(vjasw);

            stopsKey += stopId + " ";
        }
        Checksum checksum = new Adler32();
        byte bytes[] = stopsKey.getBytes();
        checksum.update(bytes, 0, bytes.length);
        return Long.toHexString(checksum.getValue());
    }

    private void convert(Context context, mobi.chouette.model.stip.VehicleJourney vehicleJourney,
                         CallOnTimedJourneyPattern callOnTimedJourneyPattern,
                         mobi.chouette.model.stip.StopPoint noptisStopPoint,
                         VehicleJourneyAtStop vehicleJourneyAtStop) {

        Referential referential = (Referential) context.get(REFERENTIAL);

        vehicleJourneyAtStop.setId(callOnTimedJourneyPattern.getId());
        //vehicleJourneyAtStop.setId(noptisStopPoint.getId());
        //vehicleJourneyAtStop.setId(PointInJourneyPattern#getId);

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

    // TODO consider moving all the below utiity methods to AbstractNoptisParser instead

    private int getPickUpTypeOrdinal(VehicleJourneyAtStopWrapper vjas) {
        return (vjas.getPickUpType() == null ? 0 : vjas.getPickUpType().ordinal());
    }

    private int getDropOffTypeOrdinal(VehicleJourneyAtStopWrapper vjas) {
        return (vjas.getDropOffType() == null ? 0 : vjas.getDropOffType().ordinal());
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
