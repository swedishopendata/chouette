package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.StopAreaTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "StopArea")
public class StopAreaEntity implements StopArea {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "Number", nullable = false)
    private int number;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "ShortName")
    private String shortName;

    @Column(name = "TypeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private StopAreaTypeCode typeCode;

    @Column(name = "IsDefinedByTransportAuthorityId", nullable = false)
    private long isDefinedByTransportAuthorityId;

    @Column(name = "CoordinateSystemName")
    private String coordinateSystemName;

    @Column(name = "CentroidNorthingCoordinate")
    private String centroidNorthingCoordinate;

    @Column(name = "CentroidEastingCoordinate")
    private String centroidEastingCoordinate;

    @Column(name = "DefaultInterchangeDurationSeconds")
    private Integer defaultInterchangeDurationSeconds;

    @Column(name = "InterchangePriority")
    private Short interchangePriority;

    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Column(name = "ExistsUptoDate")
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
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
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
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public StopAreaTypeCode getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(StopAreaTypeCode typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public long getIsDefinedByTransportAuthorityId() {
        return isDefinedByTransportAuthorityId;
    }

    public void setIsDefinedByTransportAuthorityId(long isDefinedByTransportAuthorityId) {
        this.isDefinedByTransportAuthorityId = isDefinedByTransportAuthorityId;
    }

    @Override
    public String getCoordinateSystemName() {
        return coordinateSystemName;
    }

    public void setCoordinateSystemName(String coordinateSystemName) {
        this.coordinateSystemName = coordinateSystemName;
    }

    @Override
    public String getCentroidNorthingCoordinate() {
        return centroidNorthingCoordinate;
    }

    public void setCentroidNorthingCoordinate(String centroidNorthingCoordinate) {
        this.centroidNorthingCoordinate = centroidNorthingCoordinate;
    }

    @Override
    public String getCentroidEastingCoordinate() {
        return centroidEastingCoordinate;
    }

    public void setCentroidEastingCoordinate(String centroidEastingCoordinate) {
        this.centroidEastingCoordinate = centroidEastingCoordinate;
    }

    @Override
    public Integer getDefaultInterchangeDurationSeconds() {
        return defaultInterchangeDurationSeconds;
    }

    public void setDefaultInterchangeDurationSeconds(Integer defaultInterchangeDurationSeconds) {
        this.defaultInterchangeDurationSeconds = defaultInterchangeDurationSeconds;
    }

    @Override
    public Short getInterchangePriority() {
        return interchangePriority;
    }

    public void setInterchangePriority(Short interchangePriority) {
        this.interchangePriority = interchangePriority;
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
        return "StopAreaEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", number=" + number
                + ", name='" + name + '\''
                + ", shortName='" + shortName + '\''
                + ", typeCode=" + typeCode
                + ", isDefinedByTransportAuthorityId=" + isDefinedByTransportAuthorityId
                + ", coordinateSystemName='" + coordinateSystemName + '\''
                + ", centroidNorthingCoordinate='" + centroidNorthingCoordinate + '\''
                + ", centroidEastingCoordinate='" + centroidEastingCoordinate + '\''
                + ", defaultInterchangeDurationSeconds=" + defaultInterchangeDurationSeconds
                + ", interchangePriority=" + interchangePriority
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
