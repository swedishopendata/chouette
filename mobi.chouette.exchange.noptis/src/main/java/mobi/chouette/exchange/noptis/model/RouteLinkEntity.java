package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "RouteLink")
public class RouteLinkEntity implements RouteLink {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "StartsAtJourneyPatternPointGid", nullable = false)
    private long startsAtJourneyPatternPointGid;

    @Column(name = "EndsAtJourneyPatternPointGid", nullable = false)
    private long endsAtJourneyPatternPointGid;

    @Column(name = "name")
    private String name;

    @Column(name = "DistanceMeters", nullable = false)
    private int distanceMeters;

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
    public long getStartsAtJourneyPatternPointGid() {
        return startsAtJourneyPatternPointGid;
    }

    public void setStartsAtJourneyPatternPointGid(long startsAtJourneyPatternPointGid) {
        this.startsAtJourneyPatternPointGid = startsAtJourneyPatternPointGid;
    }

    @Override
    public long getEndsAtJourneyPatternPointGid() {
        return endsAtJourneyPatternPointGid;
    }

    public void setEndsAtJourneyPatternPointGid(long endsAtJourneyPatternPointGid) {
        this.endsAtJourneyPatternPointGid = endsAtJourneyPatternPointGid;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(int distanceMeters) {
        this.distanceMeters = distanceMeters;
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
        return "RouteLinkEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", startsAtJourneyPatternPointGid=" + startsAtJourneyPatternPointGid
                + ", endsAtJourneyPatternPointGid=" + endsAtJourneyPatternPointGid
                + ", name='" + name + '\''
                + ", distanceMeters=" + distanceMeters
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
