package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

public interface Line extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    short getNumber();

    String getName();

    String getDesignation();

    TransportModeCode getDefaultTransportModeCode();

    long getIsDefinedByTransportAuthorityId();

    boolean isMonitored();
}
