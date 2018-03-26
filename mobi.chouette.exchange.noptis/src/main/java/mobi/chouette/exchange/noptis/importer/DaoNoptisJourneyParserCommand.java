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
import mobi.chouette.exchange.noptis.importer.util.NoptisReferential;
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
import mobi.chouette.model.util.NeptuneUtil;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;
import org.apache.commons.collections.CollectionUtils;
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

    @SuppressWarnings("unchecked")
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean execute(Context context) throws Exception {
        boolean result = ERROR;
        Monitor monitor = MonitorFactory.start(COMMAND);

        try {

            Referential referential = (Referential) context.get(REFERENTIAL);
            NoptisReferential noptisReferential = (NoptisReferential) context.get(NOPTIS_REFERENTIAL);
            NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);
            short dataSourceId = NoptisImporterUtils.getDataSourceId(configuration.getObjectIdPrefix());
            Line line = (Line) context.get(LINE);

            // Retrieve DirectionOfLines

            List<DirectionOfLine> directionOfLines = directionOfLineDAO.findByDataSourceAndLineId(dataSourceId, line.getId());
            for (DirectionOfLine directionOfLine : directionOfLines) {

                // Retrieve VehicleJourneys

                List<Object[]> resultList = timetableDAO.findVehicleJourneyAndTemplatesForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                List<VehicleJourneyAndTemplate> vehicleJourneyAndTemplates = new ArrayList<>();
                resultList.forEach(resultRecord -> vehicleJourneyAndTemplates.add(new VehicleJourneyAndTemplate(
                        (VehicleJourneyTemplate) resultRecord[0], (VehicleJourney) resultRecord[1])));

                // Retrieve and cache all TimedJourneyPatterns for easy access by id later

                List<TimedJourneyPattern> timedJourneyPatterns = timedJourneyPatternDAO.findTimedJourneyPatternsForDirectionOfLine(dataSourceId, directionOfLine.getGid());
                Map<Long, TimedJourneyPattern> timedJourneyPatternMap = new HashMap<>();
                timedJourneyPatterns.forEach(timedJourneyPattern -> timedJourneyPatternMap.put(timedJourneyPattern.getId(), timedJourneyPattern));

                // Iterate all templates and journeys and create a neptune VehicleJourney for each

                for (VehicleJourneyAndTemplate vehicleJourneyAndTemplate : vehicleJourneyAndTemplates) {
                    VehicleJourneyTemplate vehicleJourneyTemplate = vehicleJourneyAndTemplate.getVehicleJourneyTemplate();
                    mobi.chouette.model.stip.VehicleJourney noptisVehicleJourney = vehicleJourneyAndTemplate.getVehicleJourney();

                    String objectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            mobi.chouette.model.VehicleJourney.VEHICLEJOURNEY_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    mobi.chouette.model.VehicleJourney neptuneVehicleJourney = ObjectFactory.getVehicleJourney(referential, objectId);
                    convert(context, vehicleJourneyTemplate, noptisVehicleJourney, neptuneVehicleJourney);

                    TimedJourneyPattern timedJourneyPattern = timedJourneyPatternMap.get(vehicleJourneyTemplate.getIsWorkedOnTimedJourneyPatternId());

                    // VehicleJourneyAtStops

                    List<Object[]> callPointResultList = timetableDAO.findCallsForTimedJourneyPattern(timedJourneyPattern.getId());
                    List<CallAndPointInJourneyPattern> callAndPointInJourneyPatterns = new ArrayList<>();
                    callPointResultList.forEach(resultRecord -> callAndPointInJourneyPatterns.add(new CallAndPointInJourneyPattern(
                            (CallOnTimedJourneyPattern) resultRecord[0], (PointInJourneyPattern) resultRecord[1])));

                    for (CallAndPointInJourneyPattern callAndPointInJourneyPattern : callAndPointInJourneyPatterns) {
                        CallOnTimedJourneyPattern callOnTimedJourneyPattern = callAndPointInJourneyPattern.getCallOnTimedJourneyPattern();
                        PointInJourneyPattern pointInJourneyPattern = callAndPointInJourneyPattern.getPointInJourneyPattern();

                        VehicleJourneyAtStop vehicleJourneyAtStop = ObjectFactory.getVehicleJourneyAtStop();
                        convert(context, noptisVehicleJourney, callOnTimedJourneyPattern, pointInJourneyPattern, vehicleJourneyAtStop);
                        //vehicleJourneyAtStop.setVehicleJourney(neptuneVehicleJourney);
                    }

                    // Timetable

                    String timetableId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), Timetable.TIMETABLE_KEY, String.valueOf(noptisVehicleJourney.getId()));
                    Timetable timetable = ObjectFactory.getTimetable(referential, timetableId);
                    neptuneVehicleJourney.getTimetables().add(timetable);

                    // Route and JourneyPattern

                    long journeyPatternId = timedJourneyPattern.getIsBasedOnJourneyPatternId();
                    List<PointInJourneyPattern> pointsInJourneyPattern = pointInJourneyPatternDAO.findByJourneyPatternId(journeyPatternId);

                    // Route

                    String lineId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), mobi.chouette.model.Line.LINE_KEY, String.valueOf(line.getGid()));
                    mobi.chouette.model.Line neptuneLine = ObjectFactory.getLine(referential, lineId);

                    String routeKey = line.getGid() + "_" + directionOfLine.getDirectionCode().ordinal();
                    //routeKey += "_" + buildStopsKey(neptuneVehicleJourney);
                    routeKey += "_" + buildStopsKey(context, pointsInJourneyPattern);

                    String routeId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), Route.ROUTE_KEY, routeKey);
                    log.info(Color.LIGHT_BLUE + "createRoute : route " + routeId + Color.NORMAL);

                    Route route = ObjectFactory.getRoute(referential, routeId);
                    route.setLine(neptuneLine);

                    String wayBack = directionOfLine.getDirectionCode().equals(DirectionCode.EVEN) ? "A" : "R";
                    route.setWayBack(wayBack);
                    route.setFilled(true);

                    // JourneyPattern

                    String journeyPatternObjectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                            mobi.chouette.model.JourneyPattern.JOURNEYPATTERN_KEY, String.valueOf(journeyPatternId));
                    mobi.chouette.model.JourneyPattern neptuneJourneyPattern = ObjectFactory.getJourneyPattern(referential, journeyPatternObjectId);

                    neptuneJourneyPattern.setName("");
                    neptuneJourneyPattern.setRoute(route);

                    log.info(Color.CYAN + "createJourneyPattern : journeyPattern " +
                    neptuneJourneyPattern.getObjectId() + Color.NORMAL);

                    Set<String> stopPointKeys = new HashSet<>();

                    for (PointInJourneyPattern pointInJourneyPattern : pointsInJourneyPattern) {

                        long journeyPatternPointGid = pointInJourneyPattern.getIsJourneyPatternPointGid();
                        StopPoint noptisStopPoint = noptisReferential.getSharedStopPoints().get(journeyPatternPointGid);
                        String stopPointGidAsString = String.valueOf(noptisStopPoint.getGid());

                        String baseKey = route.getObjectId().replace(Route.ROUTE_KEY, mobi.chouette.model.StopPoint.STOPPOINT_KEY)
                                + "a" + stopPointGidAsString.trim().replaceAll("[^a-zA-Z_0-9\\-]", "_");
                        String stopKey = baseKey;
                        int dup = 1;
                        while (stopPointKeys.contains(stopKey)) {
                            stopKey = baseKey + "_" + (dup++);
                        }
                        stopPointKeys.add(stopKey);

                        mobi.chouette.model.StopPoint stopPoint = ObjectFactory.getStopPoint(referential, stopKey);

                        String stopAreaId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                                mobi.chouette.model.StopArea.STOPAREA_KEY, String.valueOf(noptisStopPoint.getGid()));
                        mobi.chouette.model.StopArea stopArea = ObjectFactory.getStopArea(referential, stopAreaId);
                        stopPoint.setContainedInStopArea(stopArea);
                        stopPoint.setRoute(route);
                        stopPoint.setPosition(pointInJourneyPattern.getSequenceNumber());

                        stopPoint.setForBoarding(AbstractNoptisParser.convertDepartureType(pointInJourneyPattern.getDepartureType()));
                        stopPoint.setForAlighting(AbstractNoptisParser.convertArrivalType(pointInJourneyPattern.getArrivalType()));

                        neptuneJourneyPattern.addStopPoint(stopPoint);
                        stopPoint.setFilled(true);
                    }

                    List<mobi.chouette.model.StopPoint> journeyPatternStopPoints = neptuneJourneyPattern.getStopPoints();
                    if (CollectionUtils.isNotEmpty(journeyPatternStopPoints)) {
                        neptuneJourneyPattern.setDepartureStopPoint(journeyPatternStopPoints.get(0));
                        neptuneJourneyPattern.setArrivalStopPoint(journeyPatternStopPoints.get(journeyPatternStopPoints.size() - 1));
                    }

                    NeptuneUtil.refreshDepartureArrivals(neptuneJourneyPattern);

                    neptuneJourneyPattern.setFilled(true);

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

    private Route createRoute(Referential referential, NoptisImportParameters configuration, Line noptisLine, DirectionOfLine directionOfLine) {
        String lineId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), mobi.chouette.model.Line.LINE_KEY, String.valueOf(noptisLine.getGid()));
        mobi.chouette.model.Line neptuneLine = ObjectFactory.getLine(referential, lineId);

        String routeKey = noptisLine.getGid() + "_" + directionOfLine.getDirectionCode().ordinal();
        //routeKey += "_" + neptuneLine.getRoutes().size();
        //routeKey += "_" + buildStopsKey(vehicleJourney);

        String routeId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(), Route.ROUTE_KEY, routeKey);
        log.info(Color.LIGHT_BLUE + "createRoute : route " + routeId + Color.NORMAL);

        Route route = ObjectFactory.getRoute(referential, routeId);
        route.setLine(neptuneLine);

        String wayBack = directionOfLine.getDirectionCode().equals(DirectionCode.EVEN) ? "A" : "R";
        route.setWayBack(wayBack);

/*
        while (xpp.nextTag() == XmlPullParser.START_TAG) {
            if (xpp.getName().equals("PointOnRoute")) {
                String id = xpp.getAttributeValue(null, ID);
                mobi.chouette.model.StopPoint stopPoint = ObjectFactory.getStopPoint(referential,
                        getStopPointObjectId(route, id));
                stopPoint.setRoute(route);
                stopPoint.setFilled(true);
                XPPUtil.skipSubTree(log, xpp);

            } else {
                XPPUtil.skipSubTree(log, xpp);
            }

        }
        for (mobi.chouette.model.StopPoint stopPoint : route.getStopPoints()) {
            stopPoint.setPosition(route.getStopPoints().indexOf(stopPoint));
        }
*/


        route.setFilled(true);
        return route;
    }

    protected void convert(Context context, VehicleJourneyTemplate vehicleJourneyTemplate,
                           mobi.chouette.model.stip.VehicleJourney noptisVehicleJourney,
                           mobi.chouette.model.VehicleJourney neptuneVehicleJourney) {

        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

        try {
            neptuneVehicleJourney.setNumber(noptisVehicleJourney.getId());
        } catch (NumberFormatException e) {
            neptuneVehicleJourney.setNumber(0L);
            neptuneVehicleJourney.setPublishedJourneyName(String.valueOf(noptisVehicleJourney.getId()));
        }

        if (vehicleJourneyTemplate.getContractorGid() != null) {
            String companyObjectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    Company.COMPANY_KEY, String.valueOf(vehicleJourneyTemplate.getContractorGid()));
            Company company = ObjectFactory.getCompany(referential, companyObjectId);
            neptuneVehicleJourney.setCompany(company);
        }

        if (vehicleJourneyTemplate.getTransportModeCode() != null) {
            TransportModeCode transportModeCode = vehicleJourneyTemplate.getTransportModeCode();
            neptuneVehicleJourney.setTransportMode(AbstractNoptisParser.convertTransportModeCode(transportModeCode));
        }

        neptuneVehicleJourney.setFilled(true);
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
                         PointInJourneyPattern pointInJourneyPattern,
                         VehicleJourneyAtStop vehicleJourneyAtStop) {

        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisReferential noptisReferential = (NoptisReferential) context.get(NOPTIS_REFERENTIAL);

        //BoardingPossibilityEnum boardingPossibility = AbstractNoptisParser.convertDepartureType(pointInJourneyPattern.getDepartureType());
        //AlightingPossibilityEnum alightingPossibility = AbstractNoptisParser.convertArrivalType(pointInJourneyPattern.getArrivalType());

        vehicleJourneyAtStop.setId(callOnTimedJourneyPattern.getId());

        //String objectId = gtfsStopTime.getStopId();
        //mobi.chouette.model.StopPoint stopPoint = ObjectFactory.getStopPoint(referential, objectId);

        long journeyPatternPointGid = pointInJourneyPattern.getIsJourneyPatternPointGid();
        //mobi.chouette.model.stip.StopPoint noptisStopPoint = stopPointDAO.findByJourneyPatternPointGid(pointInJourneyPattern.getIsJourneyPatternPointGid());

        StopPoint noptisStopPoint = noptisReferential.getSharedStopPoints().get(journeyPatternPointGid);
        mobi.chouette.model.StopPoint neptuneStopPoint = ObjectFactory.getStopPoint(referential, String.valueOf(noptisStopPoint.getGid()));
        //vehicleJourneyAtStop.setStopPoint(neptuneStopPoint);

        String arrivalTimeAsString = getArrivalTime(vehicleJourney, callOnTimedJourneyPattern).getAsHourMinuteSecondString();
        arrivalTimeAsString = arrivalTimeAsString.startsWith("24") ? arrivalTimeAsString.replaceFirst("24", "00") : arrivalTimeAsString;
        arrivalTimeAsString = arrivalTimeAsString.startsWith("25") ? arrivalTimeAsString.replaceFirst("25", "01") : arrivalTimeAsString;
        arrivalTimeAsString = arrivalTimeAsString.startsWith("26") ? arrivalTimeAsString.replaceFirst("26", "02") : arrivalTimeAsString;
        arrivalTimeAsString = arrivalTimeAsString.startsWith("27") ? arrivalTimeAsString.replaceFirst("27", "03") : arrivalTimeAsString;
        arrivalTimeAsString = arrivalTimeAsString.startsWith("28") ? arrivalTimeAsString.replaceFirst("28", "04") : arrivalTimeAsString;
        arrivalTimeAsString = arrivalTimeAsString.startsWith("29") ? arrivalTimeAsString.replaceFirst("29", "05") : arrivalTimeAsString;

        Time arrivalTime = toTime(toCalendar(arrivalTimeAsString, "HH:mm:ss"));
        //Time arrivalTime = toTime(toCalendar(arrivalTimeAsString, "kk:mm:ss"));
        vehicleJourneyAtStop.setArrivalTime(arrivalTime);

        String departureTimeAsString = getDepartureTime(vehicleJourney, callOnTimedJourneyPattern).getAsHourMinuteSecondString();
        departureTimeAsString = departureTimeAsString.startsWith("24") ? departureTimeAsString.replaceFirst("24", "00") : departureTimeAsString;
        departureTimeAsString = departureTimeAsString.startsWith("25") ? departureTimeAsString.replaceFirst("25", "01") : departureTimeAsString;
        departureTimeAsString = departureTimeAsString.startsWith("26") ? departureTimeAsString.replaceFirst("26", "02") : departureTimeAsString;
        departureTimeAsString = departureTimeAsString.startsWith("27") ? departureTimeAsString.replaceFirst("27", "03") : departureTimeAsString;
        departureTimeAsString = departureTimeAsString.startsWith("28") ? departureTimeAsString.replaceFirst("28", "04") : departureTimeAsString;
        departureTimeAsString = departureTimeAsString.startsWith("29") ? departureTimeAsString.replaceFirst("29", "05") : departureTimeAsString;

        Time departureTime = toTime(toCalendar(departureTimeAsString, "HH:mm:ss"));
        //Time departureTime = toTime(toCalendar(departureTimeAsString, "kk:mm:ss"));
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

    private int getDepartureTypeOrdinal(PointInJourneyPattern pointInJourneyPattern) {
        return (pointInJourneyPattern.getDepartureType() == null ? 0 : pointInJourneyPattern.getDepartureType().ordinal());
    }

    private int getArrivalTypeOrdinal(PointInJourneyPattern pointInJourneyPattern) {
        return (pointInJourneyPattern.getArrivalType() == null ? 0 : pointInJourneyPattern.getArrivalType().ordinal());
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
