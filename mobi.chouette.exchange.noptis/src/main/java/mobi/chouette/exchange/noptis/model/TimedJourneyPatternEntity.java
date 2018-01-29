package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TimedJourneyPattern")
public class TimedJourneyPatternEntity implements TimedJourneyPattern {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsBasedOnJourneyPatternId", nullable = false)
    private long isBasedOnJourneyPatternId;

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
    public long getIsBasedOnJourneyPatternId() {
        return isBasedOnJourneyPatternId;
    }

    public void setIsBasedOnJourneyPatternId(long isBasedOnJourneyPatternId) {
        this.isBasedOnJourneyPatternId = isBasedOnJourneyPatternId;
    }

    @Override
    public String toString() {
        return "TimedJourneyPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isBasedOnJourneyPatternId=" + isBasedOnJourneyPatternId
                + '}';
    }
}
