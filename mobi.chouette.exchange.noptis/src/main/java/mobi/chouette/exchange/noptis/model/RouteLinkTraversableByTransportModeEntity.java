package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.persistence.*;

@Entity
@Table(name = "RouteLinkTraversableByTransportMode")
public class RouteLinkTraversableByTransportModeEntity implements RouteLinkTraversableByTransportMode {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsRouteLinkId", nullable = false)
    private long isRouteLinkId;

    @Column(name = "TransportModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportModeCode transportModeCode;

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public short getIsFromDataSourceId() {
        return isFromDataSourceId;
    }

    public void setIsFromDataSourceId(short isFromDataSourceId) {
        this.isFromDataSourceId = isFromDataSourceId;
    }

    @Override
    public long getIsRouteLinkId() {
        return isRouteLinkId;
    }

    public void setIsRouteLinkId(long isRouteLinkId) {
        this.isRouteLinkId = isRouteLinkId;
    }

    @Override
    public TransportModeCode getTransportModeCode() {
        return transportModeCode;
    }

    public void setTransportModeCode(TransportModeCode transportModeCode) {
        this.transportModeCode = transportModeCode;
    }

    @Override
    public String toString() {
        return "RouteLinkTraversableByTransportModeEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isRouteLinkId=" + isRouteLinkId
                + ", transportModeCode=" + transportModeCode
                + '}';
    }
}
