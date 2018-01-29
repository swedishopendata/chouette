package mobi.chouette.exchange.noptis.model;

import java.time.LocalTime;

public interface AdvanceOrderCondition {
    long getId();

    short getIsFromDataSourceId();

    long getIsAppliedToVehicleJourneyId();

    Integer getMinimumDaysInAdvanceCount();

    LocalTime getLatestAbsoluteTime();

    Integer getLatestTimeSpanInAdvanceDurationSeconds();

    boolean isPublic();

    String getPublicNote();
}
