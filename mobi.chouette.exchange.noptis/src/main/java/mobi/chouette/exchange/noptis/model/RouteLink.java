package mobi.chouette.exchange.noptis.model;

import java.time.LocalDate;

public interface RouteLink {
    long getId();

    short getIsFromDataSourceId();

    long getStartsAtJourneyPatternPointGid();

    long getEndsAtJourneyPatternPointGid();

    String getName();

    int getDistanceMeters();

    LocalDate getExistsFromDate();

    LocalDate getExistsUpToDate();
}
