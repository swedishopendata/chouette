package mobi.chouette.exchange.noptis.model;

public interface PointInDestinationPattern {
    long getId();

    short getIsFromDataSourceId();

    long getIsInDestinationPatternId();

    long getIsOnPointInJourneyPatternId();

    Long getHasDestinationDisplayId();
}
