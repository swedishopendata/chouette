package mobi.chouette.exchange.noptis.model;

public interface JourneyPatternPoint extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    int getNumber();

    long getIsDefinedByTransportAuthorityId();

    String getCoordinateSystemName();

    String getLocationNorthingCoordinate();

    String getLocationEastingCoordinate();
}
