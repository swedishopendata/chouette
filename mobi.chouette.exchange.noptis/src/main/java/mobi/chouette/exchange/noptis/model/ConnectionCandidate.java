package mobi.chouette.exchange.noptis.model;

public interface ConnectionCandidate {
    long getId();

    short getIsFromDataSourceId();

    long getIsFromFeederVehicleJourneyId();

    long getIsFromFeederCallOnTimedJourneyPatternId();

    long getIsToFetcherVehicleJourneyId();

    long getIsToFetcherCallOnTimedJourneyPatternId();
}
