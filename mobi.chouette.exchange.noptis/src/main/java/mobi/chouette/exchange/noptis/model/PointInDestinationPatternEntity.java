package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PointInDestinationPattern")
public class PointInDestinationPatternEntity implements PointInDestinationPattern {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsInDestinationPatternId", nullable = false)
    private long isInDestinationPatternId;

    @Column(name = "IsOnPointInJourneyPatternId", nullable = false)
    private long isOnPointInJourneyPatternId;

    @Column(name = "hasDestinationDisplayId")
    private Long hasDestinationDisplayId;

    @Override
    public long getId() {
        return id;
    }

    public void setId(final long id) {
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
    public long getIsInDestinationPatternId() {
        return isInDestinationPatternId;
    }

    public void setIsInDestinationPatternId(final long isInDestinationPatternId) {
        this.isInDestinationPatternId = isInDestinationPatternId;
    }

    @Override
    public long getIsOnPointInJourneyPatternId() {
        return isOnPointInJourneyPatternId;
    }

    public void setIsOnPointInJourneyPatternId(final long isOnPointInJourneyPatternId) {
        this.isOnPointInJourneyPatternId = isOnPointInJourneyPatternId;
    }

    @Override
    public Long getHasDestinationDisplayId() {
        return hasDestinationDisplayId;
    }

    public void setHasDestinationDisplayId(final Long hasDestinationDisplayId) {
        this.hasDestinationDisplayId = hasDestinationDisplayId;
    }

    @Override
    public String toString() {
        return "PointInDestinationPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isInDestinationPatternId=" + isInDestinationPatternId
                + ", isOnPointInJourneyPatternId=" + isOnPointInJourneyPatternId
                + ", hasDestinationDisplayId=" + hasDestinationDisplayId
                + '}';
    }
}
