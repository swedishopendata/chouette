package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.StopPointTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "StopPoint")
public class StopPointEntity implements StopPoint {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "Name")
    private String name;

    @Column(name = "ShortName")
    private String shortName;

    @Column(name = "Designation")
    private String designation;

    @Column(name = "LocalNumber", nullable = false)
    private short localNumber;

    @Column(name = "IsJourneyPatternPointGid", nullable = false)
    private long isJourneyPatternPointGid;

    @Column(name = "IsIncludedInStopAreaGid", nullable = false)
    private long isIncludedInStopAreaGid;

    @Column(name = "TypeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private StopPointTypeCode typeCode;

    @Column(name = "ForAlighting", nullable = false)
    private boolean forAlighting;

    @Column(name = "ForBoarding", nullable = false)
    private boolean forBoarding;

    @Column(name = "Fictitious", nullable = false)
    private boolean fictitious;

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
    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public short getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(short localNumber) {
        this.localNumber = localNumber;
    }

    @Override
    public long getIsJourneyPatternPointGid() {
        return isJourneyPatternPointGid;
    }

    public void setIsJourneyPatternPointGid(long isJourneyPatternPointGid) {
        this.isJourneyPatternPointGid = isJourneyPatternPointGid;
    }

    @Override
    public long getIsIncludedInStopAreaGid() {
        return isIncludedInStopAreaGid;
    }

    public void setIsIncludedInStopAreaGid(long isIncludedInStopAreaGid) {
        this.isIncludedInStopAreaGid = isIncludedInStopAreaGid;
    }

    @Override
    public StopPointTypeCode getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(StopPointTypeCode typeCode) {
        this.typeCode = typeCode;
    }

    @Override
    public boolean isForAlighting() {
        return forAlighting;
    }

    public void setForAlighting(boolean forAlighting) {
        this.forAlighting = forAlighting;
    }

    @Override
    public boolean isForBoarding() {
        return forBoarding;
    }

    public void setForBoarding(boolean forBoarding) {
        this.forBoarding = forBoarding;
    }

    @Override
    public boolean isFictitious() {
        return fictitious;
    }

    public void setFictitious(boolean fictitious) {
        this.fictitious = fictitious;
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
        return "StopPointEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", name='" + name + '\''
                + ", shortName='" + shortName + '\''
                + ", designation='" + designation + '\''
                + ", localNumber=" + localNumber
                + ", isJourneyPatternPointGid=" + isJourneyPatternPointGid
                + ", isIncludedInStopAreaGid=" + isIncludedInStopAreaGid
                + ", typeCode=" + typeCode
                + ", forAlighting=" + forAlighting
                + ", forBoarding=" + forBoarding
                + ", fictitious=" + fictitious
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
