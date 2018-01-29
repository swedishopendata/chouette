package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "AdvanceOrderCondition")
public class AdvanceOrderConditionEntity implements AdvanceOrderCondition {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsAppliedToVehicleJourneyId", nullable = false)
    private long isAppliedToVehicleJourneyId;

    @Column(name = "MinimumDaysInAdvanceCount")
    private Integer minimumDaysInAdvanceCount;

    @Column(name = "LatestAbsoluteTime")
    private LocalTime latestAbsoluteTime;

    @Column(name = "LatestTimeSpanInAdvanceDurationSeconds")
    private Integer latestTimeSpanInAdvanceDurationSeconds;

    @Column(name = "PublicYesNo")
    private boolean publicYesNo;

    @Column(name = "PublicNote")
    private String publicNote;

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
    public long getIsAppliedToVehicleJourneyId() {
        return isAppliedToVehicleJourneyId;
    }

    public void setIsAppliedToVehicleJourneyId(long isAppliedToVehicleJourneyId) {
        this.isAppliedToVehicleJourneyId = isAppliedToVehicleJourneyId;
    }

    @Override
    public Integer getMinimumDaysInAdvanceCount() {
        return minimumDaysInAdvanceCount;
    }

    public void setMinimumDaysInAdvanceCount(Integer minimumDaysInAdvanceCount) {
        this.minimumDaysInAdvanceCount = minimumDaysInAdvanceCount;
    }

    @Override
    public LocalTime getLatestAbsoluteTime() {
        return latestAbsoluteTime;
    }

    public void setLatestAbsoluteTime(LocalTime latestAbsoluteTime) {
        this.latestAbsoluteTime = latestAbsoluteTime;
    }

    @Override
    public Integer getLatestTimeSpanInAdvanceDurationSeconds() {
        return latestTimeSpanInAdvanceDurationSeconds;
    }

    public void setLatestTimeSpanInAdvanceDurationSeconds(Integer latestTimeSpanInAdvanceDurationSeconds) {
        this.latestTimeSpanInAdvanceDurationSeconds = latestTimeSpanInAdvanceDurationSeconds;
    }

    public boolean isPublic() {
        return publicYesNo;
    }

    public void setPublic(boolean publicYesNo) {
        this.publicYesNo = publicYesNo;
    }

    @Override
    public String getPublicNote() {
        return publicNote;
    }

    public void setPublicNote(String publicNote) {
        this.publicNote = publicNote;
    }

    @Override
    public String toString() {
        return "AdvanceOrderConditionEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isAppliedToVehicleJourneyId=" + isAppliedToVehicleJourneyId
                + ", minimumDaysInAdvanceCount=" + minimumDaysInAdvanceCount
                + ", latestAbsoluteTime=" + latestAbsoluteTime
                + ", latestTimeSpanInAdvanceDurationSeconds=" + latestTimeSpanInAdvanceDurationSeconds
                + ", publicYesNo=" + publicYesNo
                + ", publicNote='" + publicNote + '\''
                + '}';
    }
}
