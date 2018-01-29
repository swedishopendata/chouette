package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.StopAreaTypeCode;

public interface StopArea extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    int getNumber();

    String getName();

    String getShortName();

    StopAreaTypeCode getTypeCode();

    long getIsDefinedByTransportAuthorityId();

    String getCoordinateSystemName();

    String getCentroidNorthingCoordinate();

    String getCentroidEastingCoordinate();

    Integer getDefaultInterchangeDurationSeconds();

    Short getInterchangePriority();
}
