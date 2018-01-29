package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DestinationPattern")
public class DestinationPatternEntity implements DestinationPattern {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsOnJourneyPatternId")
    private long isOnJourneyPatternId;

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
    public long getIsOnJourneyPatternId() {
        return isOnJourneyPatternId;
    }

    public void setIsOnJourneyPatternId(long isOnJourneyPatternId) {
        this.isOnJourneyPatternId = isOnJourneyPatternId;
    }

    @Override
    public String toString() {
        return "DestinationPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isOnJourneyPatternId=" + isOnJourneyPatternId
                + '}';
    }
}
