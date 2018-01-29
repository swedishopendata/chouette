package mobi.chouette.exchange.noptis.model;

public interface NamedJourneyPattern extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    String getReferenceName();

    Long getIsOnDirectionOfLineId();

    Long getIsInScopeOfContractorId();

    long getIsJourneyPatternId();

    long getHasDestinationPatternId();
}
