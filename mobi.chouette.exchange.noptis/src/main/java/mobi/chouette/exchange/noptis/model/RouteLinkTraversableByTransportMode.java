package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

public interface RouteLinkTraversableByTransportMode {
    long getId();

    short getIsFromDataSourceId();

    long getIsRouteLinkId();

    TransportModeCode getTransportModeCode();
}
