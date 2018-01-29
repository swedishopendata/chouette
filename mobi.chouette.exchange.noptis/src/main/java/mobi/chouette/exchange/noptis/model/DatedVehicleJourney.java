package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.PlannedTypeCode;

import java.time.LocalDate;

public interface DatedVehicleJourney {
    long getId();

    short getIsFromDataSourceId();

    LocalDate getOperatingDayDate();

    long getGid();

    long getIsBasedOnVehicleJourneyId();

    PlannedTypeCode getPlannedTypeCode();

    Long getIsReplacedById();
}
