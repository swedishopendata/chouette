package mobi.chouette.exchange.noptis.parser;

import com.google.common.base.Splitter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import mobi.chouette.common.Context;
import mobi.chouette.exchange.importer.Parser;
import mobi.chouette.exchange.importer.ParserFactory;
import mobi.chouette.exchange.noptis.Constant;
import mobi.chouette.exchange.noptis.importer.NoptisImportParameters;
import mobi.chouette.model.Company;
import mobi.chouette.model.VehicleJourneyAtStop;
import mobi.chouette.model.stip.CallOnTimedJourneyPattern;
import mobi.chouette.model.stip.PointInJourneyPattern;
import mobi.chouette.model.stip.VehicleJourney;
import mobi.chouette.model.stip.VehicleJourneyTemplate;
import mobi.chouette.model.stip.type.TransportModeCode;
import mobi.chouette.model.stip.util.OffsetDayTime;
import mobi.chouette.model.type.JourneyCategoryEnum;
import mobi.chouette.model.util.ObjectFactory;
import mobi.chouette.model.util.Referential;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Log4j
public class NoptisVehicleJourneyParser implements Parser, Constant {

    @Getter
    @Setter
    private VehicleJourneyAndTemplate vehicleJourneyAndTemplate;

    @Getter
    @Setter
    private List<CallAndPointInJourneyPattern> callAndPointInJourneyPatterns;

    @Override
    public void parse(Context context) throws Exception {
        Referential referential = (Referential) context.get(REFERENTIAL);
        NoptisImportParameters configuration = (NoptisImportParameters) context.get(CONFIGURATION);

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

        if (vehicleJourneyTemplate.getContractorGid() != null) {
            String companyObjectId = AbstractNoptisParser.composeObjectId(configuration.getObjectIdPrefix(),
                    Company.COMPANY_KEY, String.valueOf(vehicleJourneyTemplate.getContractorGid()));
            Company company = ObjectFactory.getCompany(referential, companyObjectId);
            neptuneVehicleJourney.setCompany(company);
        }

        if (vehicleJourneyTemplate.getTransportModeCode() != null) {
            TransportModeCode transportModeCode = vehicleJourneyTemplate.getTransportModeCode();
            neptuneVehicleJourney.setTransportMode(AbstractNoptisParser.convertTransportModeCode(transportModeCode));
            neptuneVehicleJourney.setVehicleTypeIdentifier(transportModeCode.getCodeValue());
        }

        neptuneVehicleJourney.setMobilityRestrictedSuitability(Boolean.TRUE);
        neptuneVehicleJourney.setJourneyCategory(JourneyCategoryEnum.Timesheet);
        //neptuneVehicleJourney.setFlexibleService(Boolean.FALSE);

        neptuneVehicleJourney.setFilled(true);

        for (CallAndPointInJourneyPattern callAndPointInJourneyPattern : callAndPointInJourneyPatterns) {
            CallOnTimedJourneyPattern callOnTimedJourneyPattern = callAndPointInJourneyPattern.getCallOnTimedJourneyPattern();
            PointInJourneyPattern pointInJourneyPattern = callAndPointInJourneyPattern.getPointInJourneyPattern();

            VehicleJourneyAtStop vehicleJourneyAtStop = ObjectFactory.getVehicleJourneyAtStop();
            parseCallsAndPoints(context, noptisVehicleJourney, callOnTimedJourneyPattern, pointInJourneyPattern, vehicleJourneyAtStop);

            vehicleJourneyAtStop.setVehicleJourney(neptuneVehicleJourney);
        }
    }

    private void parseCallsAndPoints(Context context, VehicleJourney vehicleJourney, CallOnTimedJourneyPattern callOnTimedJourneyPattern,
                                     PointInJourneyPattern pointInJourneyPattern, VehicleJourneyAtStop vehicleJourneyAtStop) {

        Referential referential = (Referential) context.get(REFERENTIAL);

        vehicleJourneyAtStop.setId(callOnTimedJourneyPattern.getId());

        mobi.chouette.model.StopPoint neptuneStopPoint = ObjectFactory.getStopPoint(referential, String.valueOf(pointInJourneyPattern.getId()));
        vehicleJourneyAtStop.setStopPoint(neptuneStopPoint);

        String arrivalTimeAsString = getArrivalTime(vehicleJourney, callOnTimedJourneyPattern).getAsHourMinuteSecondString();
        String arrivalHour = Splitter.on(":").trimResults().omitEmptyStrings().splitToList(arrivalTimeAsString).get(0);
        arrivalTimeAsString = Integer.valueOf(arrivalHour) > 23 ? arrivalTimeAsString.replaceFirst(arrivalHour, toHourInDay(arrivalHour)) : arrivalTimeAsString;
        Time arrivalTime = toTime(toCalendar(arrivalTimeAsString));
        vehicleJourneyAtStop.setArrivalTime(arrivalTime);

        String departureTimeAsString = getDepartureTime(vehicleJourney, callOnTimedJourneyPattern).getAsHourMinuteSecondString();
        String departureHour = Splitter.on(":").trimResults().omitEmptyStrings().splitToList(departureTimeAsString).get(0);
        departureTimeAsString = Integer.valueOf(departureHour) > 23 ? departureTimeAsString.replaceFirst(departureHour, toHourInDay(departureHour)) : departureTimeAsString;
        Time departureTime = toTime(toCalendar(departureTimeAsString));
        vehicleJourneyAtStop.setDepartureTime(departureTime);

        vehicleJourneyAtStop.setArrivalDayOffset(0);
        vehicleJourneyAtStop.setDepartureDayOffset(0);
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
            return departureOffsetSeconds;
        }
        return callOnTimedJourneyPattern.getLatestArrivalTimeOffsetSeconds();
    }

    private Time toTime(Calendar value) {
        return new Time(getTimeInMillis(value));
    }

    private Calendar toCalendar(String value) {
        try {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setLenient(false);
            format.parse(value);
            return format.getCalendar();
        } catch (Exception e) {
            log.error(e);
        }

        return null;
    }

    private long getTimeInMillis(Object date) {
        if (date instanceof Timestamp)
            return ((Timestamp) date).getTime();
        if (date instanceof Calendar)
            return ((Calendar) date).getTime().getTime();
        else
            return ((Date) date).getTime();
    }

    static {
        ParserFactory.register(NoptisVehicleJourneyParser.class.getName(), new ParserFactory() {
            @Override
            protected Parser create() {
                return new NoptisVehicleJourneyParser();
            }
        });
    }

    private String toHourInDay(String hourInDay) {
        String mappedHourInDay;
        switch (hourInDay) {
            case "24":
                mappedHourInDay = "00";
                break;
            case "25":
                mappedHourInDay = "01";
                break;
            case "26":
                mappedHourInDay = "02";
                break;
            case "27":
                mappedHourInDay = "03";
                break;
            case "28":
                mappedHourInDay = "04";
                break;
            case "29":
                mappedHourInDay = "05";
                break;
            case "30":
                mappedHourInDay = "06";
                break;
            case "31":
                mappedHourInDay = "07";
                break;
            default:
                throw new IllegalArgumentException("Invalid hour in day: " + hourInDay);
        }
        return mappedHourInDay;
    }
}

