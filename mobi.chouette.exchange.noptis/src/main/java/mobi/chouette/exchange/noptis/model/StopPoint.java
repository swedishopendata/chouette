package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.StopPointTypeCode;

public interface StopPoint extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    String getName();

    String getShortName();

    String getDesignation();

    short getLocalNumber();

    long getIsJourneyPatternPointGid();

    long getIsIncludedInStopAreaGid();

    StopPointTypeCode getTypeCode();

    boolean isForAlighting();

    boolean isForBoarding();

    boolean isFictitious();
}
