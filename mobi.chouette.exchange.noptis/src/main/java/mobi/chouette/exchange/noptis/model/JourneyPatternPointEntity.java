package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "JourneyPatternPoint")
public class JourneyPatternPointEntity implements JourneyPatternPoint {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "Number", nullable = false)
    private int number;

    @Column(name = "IsDefinedByTransportAuthorityId", nullable = false)
    private long isDefinedByTransportAuthorityId;

    @Column(name = "CoordinateSystemName")
    private String coordinateSystemName;

    @Column(name = "LocationNorthingCoordinate")
    private String locationNorthingCoordinate;

    @Column(name = "LocationEastingCoordinate")
    private String locationEastingCoordinate;

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

    public String getLocationNorthingCoordinate() {
        return locationNorthingCoordinate;
    }

    public void setLocationNorthingCoordinate(String locationNorthingCoordinate) {
        this.locationNorthingCoordinate = locationNorthingCoordinate;
    }

    public String getLocationEastingCoordinate() {
        return locationEastingCoordinate;
    }

    public void setLocationEastingCoordinate(String locationEastingCoordinate) {
        this.locationEastingCoordinate = locationEastingCoordinate;
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
        return "JourneyPatternPointEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", number=" + number
                + ", isDefinedByTransportAuthorityId=" + isDefinedByTransportAuthorityId
                + ", coordinateSystemName='" + coordinateSystemName + '\''
                + ", locationNorthingCoordinate='" + locationNorthingCoordinate + '\''
                + ", locationEastingCoordinate='" + locationEastingCoordinate + '\''
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
