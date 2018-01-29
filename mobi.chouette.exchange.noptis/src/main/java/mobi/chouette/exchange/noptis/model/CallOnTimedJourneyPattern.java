package mobi.chouette.exchange.noptis.model;

public interface CallOnTimedJourneyPattern {
    long getId();

    short getIsFromDataSourceId();

    long getIsOnTimedJourneyPatternId();

    long getIsOnPointInJourneyPatternId();

    int getEarliestDepartureTimeOffsetSeconds();

    int getLatestArrivalTimeOffsetSeconds();

    boolean isTimingPoint();
}
