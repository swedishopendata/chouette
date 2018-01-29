package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.util.OffsetDayTime;

public interface VehicleJourney {
    long getId();

    short getIsFromDataSourceId();

    OffsetDayTime getPlannedStartOffsetDayTime();

    OffsetDayTime getPlannedEndOffsetDayTime();

    long getIsDescribedByVehicleJourneyTemplateId();
}
