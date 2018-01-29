package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.PointOnLinkTypeCode;

import java.time.LocalDate;

public interface PointOnRouteLink {
    long getId();

    short getIsFromDataSourceId();

    PointOnLinkTypeCode getPointOnLinkTypeCode();

    Double getAtOffsetMeters();

    Integer getDurationFromBeginningSeconds();

    Integer getDurationFromLatestArrival();

    String getCoordinateSystemName();

    String getLocationNorthingCoordinate();

    String getLocationEastingCoordinate();

    long getIsOnRouteLinkId();

    LocalDate getExistsFromDate();

    LocalDate getExistsUpToDate();
}
