package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.util.OffsetDayTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VehicleJourney")
public class VehicleJourneyEntity implements VehicleJourney {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "PlannedStartOffsetDateTime", nullable = false)
    private OffsetDayTime plannedStartOffsetDayTime;

    @Column(name = "PlannedEndOffsetDateTime", nullable = false)
    private OffsetDayTime plannedEndOffsetDayTime;

    @Column(name = "IsDescribedByVehicleJourneyTemplateId", nullable = false)
    private long isDescribedByVehicleJourneyTemplateId;

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
    public OffsetDayTime getPlannedStartOffsetDayTime() {
        return plannedStartOffsetDayTime;
    }

    public void setPlannedStartOffsetDayTime(OffsetDayTime plannedStartOffsetDayTime) {
        this.plannedStartOffsetDayTime = plannedStartOffsetDayTime;
    }

    @Override
    public OffsetDayTime getPlannedEndOffsetDayTime() {
        return plannedEndOffsetDayTime;
    }

    public void setPlannedEndOffsetDayTime(OffsetDayTime plannedEndOffsetDayTime) {
        this.plannedEndOffsetDayTime = plannedEndOffsetDayTime;
    }

    @Override
    public long getIsDescribedByVehicleJourneyTemplateId() {
        return isDescribedByVehicleJourneyTemplateId;
    }

    public void setIsDescribedByVehicleJourneyTemplateId(long isDescribedByVehicleJourneyTemplateId) {
        this.isDescribedByVehicleJourneyTemplateId = isDescribedByVehicleJourneyTemplateId;
    }

    @Override
    public String toString() {
        return "VehicleJourneyEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", plannedStartOffsetDayTime=" + plannedStartOffsetDayTime
                + ", plannedEndOffsetDayTime=" + plannedEndOffsetDayTime
                + ", isDescribedByVehicleJourneyTemplateId=" + isDescribedByVehicleJourneyTemplateId
                + '}';
    }
}
