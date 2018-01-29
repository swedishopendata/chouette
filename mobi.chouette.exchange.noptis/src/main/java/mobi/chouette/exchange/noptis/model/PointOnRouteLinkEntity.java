package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.PointOnLinkTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PointOnRouteLink")
public class PointOnRouteLinkEntity implements PointOnRouteLink {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "PointOnLinkTypeCode")
    @Enumerated(EnumType.STRING)
    private PointOnLinkTypeCode pointOnLinkTypeCode;

    @Column(name = "AtOffsetMeters")
    private Double atOffsetMeters;

    @Column(name = "DurationFromBeginningSeconds")
    private Integer durationFromBeginningSeconds;

    @Column(name = "DurationBeforeLatestArrivalSeconds")
    private Integer durationFromLatestArrival;

    @Column(name = "CoordinateSystemName")
    private String coordinateSystemName;

    @Column(name = "LocationNorthingCoordinate")
    private String locationNorthingCoordinate;

    @Column(name = "LocationEastingCoordinate")
    private String locationEastingCoordinate;

    @Column(name = "IsOnRouteLinkId")
    private long isOnRouteLinkId;

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
    public PointOnLinkTypeCode getPointOnLinkTypeCode() {
        return pointOnLinkTypeCode;
    }

    public void setPointOnLinkTypeCode(PointOnLinkTypeCode pointOnLinkTypeCode) {
        this.pointOnLinkTypeCode = pointOnLinkTypeCode;
    }

    @Override
    public Double getAtOffsetMeters() {
        return atOffsetMeters;
    }

    public void setAtOffsetMeters(Double atOffsetMeters) {
        this.atOffsetMeters = atOffsetMeters;
    }

    @Override
    public Integer getDurationFromBeginningSeconds() {
        return durationFromBeginningSeconds;
    }

    public void setDurationFromBeginningSeconds(Integer durationFromBeginningSeconds) {
        this.durationFromBeginningSeconds = durationFromBeginningSeconds;
    }

    @Override
    public Integer getDurationFromLatestArrival() {
        return durationFromLatestArrival;
    }

    public void setDurationFromLatestArrival(Integer durationFromLatestArrival) {
        this.durationFromLatestArrival = durationFromLatestArrival;
    }

    @Override
    public String getCoordinateSystemName() {
        return coordinateSystemName;
    }

    public void setCoordinateSystemName(String coordinateSystemName) {
        this.coordinateSystemName = coordinateSystemName;
    }

    @Override
    public String getLocationNorthingCoordinate() {
        return locationNorthingCoordinate;
    }

    public void setLocationNorthingCoordinate(String locationNorthingCoordinate) {
        this.locationNorthingCoordinate = locationNorthingCoordinate;
    }

    @Override
    public String getLocationEastingCoordinate() {
        return locationEastingCoordinate;
    }

    public void setLocationEastingCoordinate(String locationEastingCoordinate) {
        this.locationEastingCoordinate = locationEastingCoordinate;
    }

    @Override
    public long getIsOnRouteLinkId() {
        return isOnRouteLinkId;
    }

    public void setIsOnRouteLinkId(long isOnRouteLinkId) {
        this.isOnRouteLinkId = isOnRouteLinkId;
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
        return "PointOnRouteLinkEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", pointOnLinkTypeCode=" + pointOnLinkTypeCode
                + ", atOffsetMeters=" + atOffsetMeters
                + ", durationFromBeginningSeconds=" + durationFromBeginningSeconds
                + ", durationFromLatestArrival=" + durationFromLatestArrival
                + ", coordinateSystemName='" + coordinateSystemName + '\''
                + ", locationNorthingCoordinate='" + locationNorthingCoordinate + '\''
                + ", locationEastingCoordinate='" + locationEastingCoordinate + '\''
                + ", isOnRouteLinkId=" + isOnRouteLinkId
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
