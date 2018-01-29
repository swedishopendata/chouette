package mobi.chouette.exchange.noptis.model;

public interface DatedConnection {
    long getId();

    short getIsFromDataSourceId();

    long getIsBasedOnConnectionCandidateId();

    long getIsFromFeederDatedVehicleJourneyId();

    long getIsToFetcherDatedVehicleJourneyId();

    Integer getMinimumChangeDurationSeconds();

    Long getIsReplacedById();
}
