package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Line")
public class LineEntity implements Line {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "Number", nullable = false)
    private short number;

    @Column(name = "Name")
    private String name;

    @Column(name = "Designation", nullable = false)
    private String designation;

    @Column(name = "DefaultTransportModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportModeCode defaultTransportModeCode;

    @Column(name = "IsDefinedByTransportAuthorityId", nullable = false)
    private long isDefinedByTransportAuthorityId;

    @Column(name = "Monitored")
    private boolean monitored;

    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Column(name = "ExistsUpToDate")
    private LocalDate existsUpToDate;

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
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    @Override
    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public TransportModeCode getDefaultTransportModeCode() {
        return defaultTransportModeCode;
    }

    public void setDefaultTransportModeCode(TransportModeCode defaultTransportModeCode) {
        this.defaultTransportModeCode = defaultTransportModeCode;
    }

    @Override
    public long getIsDefinedByTransportAuthorityId() {
        return isDefinedByTransportAuthorityId;
    }

    public void setIsDefinedByTransportAuthorityId(long isDefinedByTransportAuthorityId) {
        this.isDefinedByTransportAuthorityId = isDefinedByTransportAuthorityId;
    }

    @Override
    public boolean isMonitored() {
        return monitored;
    }

    public void setMonitored(boolean monitored) {
        this.monitored = monitored;
    }

    @Override
    public LocalDate getExistsFromDate() {
        return existsFromDate;
    }

    public void setExistsFromDate(LocalDate existsFromDate) {
        this.existsFromDate = existsFromDate;
    }

    @Override
    public LocalDate getExistsUpToDate() {
        return existsUpToDate;
    }

    public void setExistsUpToDate(LocalDate existsUpToDate) {
        this.existsUpToDate = existsUpToDate;
    }

    @Override
    public String toString() {
        return "LineEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", number=" + number
                + ", name='" + name + '\''
                + ", designation='" + designation + '\''
                + ", defaultTransportModeCode=" + defaultTransportModeCode
                + ", isDefinedByTransportAuthorityId=" + isDefinedByTransportAuthorityId
                + ", monitored=" + monitored
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
