package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DatedConnection")
public class DatedConnectionEntity implements DatedConnection {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsBasedOnConnectionCandidateId", nullable = false)
    private long isBasedOnConnectionCandidateId;

    @Column(name = "IsFromFeederDatedVehicleJourneyId", nullable = false)
    private long isFromFeederDatedVehicleJourneyId;

    @Column(name = "IsToFetcherDatedVehicleJourneyId", nullable = false)
    private long isToFetcherDatedVehicleJourneyId;

    @Column(name = "MinimumChangeDurationSeconds", nullable = true)
    private Integer minimumChangeDurationSeconds;

    @Column(name = "IsReplacedById", nullable = true)
    private Long isReplacedById;

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
    public long getIsBasedOnConnectionCandidateId() {
        return isBasedOnConnectionCandidateId;
    }

    public void setIsBasedOnConnectionCandidateId(long isBasedOnConnectionCandidateId) {
        this.isBasedOnConnectionCandidateId = isBasedOnConnectionCandidateId;
    }

    @Override
    public long getIsFromFeederDatedVehicleJourneyId() {
        return isFromFeederDatedVehicleJourneyId;
    }

    public void setIsFromFeederDatedVehicleJourneyId(long isFromFeederDatedVehicleJourneyId) {
        this.isFromFeederDatedVehicleJourneyId = isFromFeederDatedVehicleJourneyId;
    }

    @Override
    public long getIsToFetcherDatedVehicleJourneyId() {
        return isToFetcherDatedVehicleJourneyId;
    }

    public void setIsToFetcherDatedVehicleJourneyId(long isToFetcherDatedVehicleJourneyId) {
        this.isToFetcherDatedVehicleJourneyId = isToFetcherDatedVehicleJourneyId;
    }

    @Override
    public Integer getMinimumChangeDurationSeconds() {
        return minimumChangeDurationSeconds;
    }

    public void setMinimumChangeDurationSeconds(Integer minimumChangeDurationSeconds) {
        this.minimumChangeDurationSeconds = minimumChangeDurationSeconds;
    }

    @Override
    public Long getIsReplacedById() {
        return isReplacedById;
    }

    public void setIsReplacedById(Long isReplacedById) {
        this.isReplacedById = isReplacedById;
    }

    @Override
    public String toString() {
        return "DatedConnectionEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isBasedOnConnectionCandidateId=" + isBasedOnConnectionCandidateId
                + ", isFromFeederDatedVehicleJourneyId=" + isFromFeederDatedVehicleJourneyId
                + ", isToFetcherDatedVehicleJourneyId=" + isToFetcherDatedVehicleJourneyId
                + ", minimumChangeDurationSeconds=" + minimumChangeDurationSeconds
                + ", isReplacedById=" + isReplacedById
                + '}';
    }
}
