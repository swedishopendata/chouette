package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CallOnTimedJourneyPattern")
public class CallOnTimedJourneyPatternEntity implements CallOnTimedJourneyPattern {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsOnTimedJourneyPatternId", nullable = false)
    private long isOnTimedJourneyPatternId;

    @Column(name = "IsOnPointInJourneyPatternId", nullable = false)
    private long isOnPointInJourneyPatternId;

    @Column(name = "EarliestDepartureTimeOffsetSeconds", nullable = false)
    private int earliestDepartureTimeOffsetSeconds;

    @Column(name = "LatestArrivalTimeOffsetSeconds", nullable = false)
    private int latestArrivalTimeOffsetSeconds;

    @Column(name = "TimingPoint")
    private boolean timingPoint;

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
    public long getIsOnTimedJourneyPatternId() {
        return isOnTimedJourneyPatternId;
    }

    public void setIsOnTimedJourneyPatternId(long isOnTimedJourneyPatternId) {
        this.isOnTimedJourneyPatternId = isOnTimedJourneyPatternId;
    }

    @Override
    public long getIsOnPointInJourneyPatternId() {
        return isOnPointInJourneyPatternId;
    }

    public void setIsOnPointInJourneyPatternId(long isOnPointInJourneyPatternId) {
        this.isOnPointInJourneyPatternId = isOnPointInJourneyPatternId;
    }

    @Override
    public int getEarliestDepartureTimeOffsetSeconds() {
        return earliestDepartureTimeOffsetSeconds;
    }

    public void setEarliestDepartureTimeOffsetSeconds(int earliestDepartureTimeOffsetSeconds) {
        this.earliestDepartureTimeOffsetSeconds = earliestDepartureTimeOffsetSeconds;
    }

    @Override
    public int getLatestArrivalTimeOffsetSeconds() {
        return latestArrivalTimeOffsetSeconds;
    }

    public void setLatestArrivalTimeOffsetSeconds(int latestArrivalTimeOffsetSeconds) {
        this.latestArrivalTimeOffsetSeconds = latestArrivalTimeOffsetSeconds;
    }

    @Override
    public boolean isTimingPoint() {
        return timingPoint;
    }

    public void setTimingPoint(boolean timingPoint) {
        this.timingPoint = timingPoint;
    }

    @Override
    public String toString() {
        return "CallOnTimedJourneyPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isOnTimedJourneyPatternId=" + isOnTimedJourneyPatternId
                + ", isOnPointInJourneyPatternId=" + isOnPointInJourneyPatternId
                + ", earliestDepartureTimeOffsetSeconds=" + earliestDepartureTimeOffsetSeconds
                + ", latestArrivalTimeOffsetSeconds=" + latestArrivalTimeOffsetSeconds
                + ", timingPoint=" + timingPoint
                + '}';
    }
}
