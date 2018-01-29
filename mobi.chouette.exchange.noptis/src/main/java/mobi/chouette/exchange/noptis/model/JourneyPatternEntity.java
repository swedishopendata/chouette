package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JourneyPattern")
public class JourneyPatternEntity implements JourneyPattern {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsOnDirectionOfLineId")
    private Long isOnDirectionOfLineId;

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
    public Long getIsOnDirectionOfLineId() {
        return isOnDirectionOfLineId;
    }

    public void setIsOnDirectionOfLineId(Long isOnDirectionOfLineId) {
        this.isOnDirectionOfLineId = isOnDirectionOfLineId;
    }

    @Override
    public String toString() {
        return "JourneyPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isOnDirectionOfLineId=" + isOnDirectionOfLineId
                + '}';
    }
}
