package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ConnectionCandidate")
public class ConnectionCandidateEntity implements ConnectionCandidate {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsFromFeederVehicleJourneyId", nullable = false)
    private long isFromFeederVehicleJourneyId;

    @Column(name = "IsFromFeederCallOnTimedJourneyPatternId", nullable = false)
    private long isFromFeederCallOnTimedJourneyPatternId;

    @Column(name = "IsToFetcherVehicleJourneyId", nullable = false)
    private long isToFetcherVehicleJourneyId;

    @Column(name = "IsToFetcherCallOnTimedJourneyPatternId", nullable = false)
    private long isToFetcherCallOnTimedJourneyPatternId;

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
    public long getIsFromFeederVehicleJourneyId() {
        return isFromFeederVehicleJourneyId;
    }

    public void setIsFromFeederVehicleJourneyId(long isFromFeederVehicleJourneyId) {
        this.isFromFeederVehicleJourneyId = isFromFeederVehicleJourneyId;
    }

    @Override
    public long getIsFromFeederCallOnTimedJourneyPatternId() {
        return isFromFeederCallOnTimedJourneyPatternId;
    }

    public void setIsFromFeederCallOnTimedJourneyPatternId(long isFromFeederCallOnTimedJourneyPatternId) {
        this.isFromFeederCallOnTimedJourneyPatternId = isFromFeederCallOnTimedJourneyPatternId;
    }

    @Override
    public long getIsToFetcherVehicleJourneyId() {
        return isToFetcherVehicleJourneyId;
    }

    public void setIsToFetcherVehicleJourneyId(long isToFetcherVehicleJourneyId) {
        this.isToFetcherVehicleJourneyId = isToFetcherVehicleJourneyId;
    }

    @Override
    public long getIsToFetcherCallOnTimedJourneyPatternId() {
        return isToFetcherCallOnTimedJourneyPatternId;
    }

    public void setIsToFetcherCallOnTimedJourneyPatternId(long isToFetcherCallOnTimedJourneyPatternId) {
        this.isToFetcherCallOnTimedJourneyPatternId = isToFetcherCallOnTimedJourneyPatternId;
    }

    @Override
    public String toString() {
        return "ConnectionCandidateEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isFromFeederVehicleJourneyId=" + isFromFeederVehicleJourneyId
                + ", isFromFeederCallOnTimedJourneyPatternId=" + isFromFeederCallOnTimedJourneyPatternId
                + ", isToFetcherVehicleJourneyId=" + isToFetcherVehicleJourneyId
                + ", isToFetcherCallOnTimedJourneyPatternId=" + isToFetcherCallOnTimedJourneyPatternId
                + '}';
    }
}
