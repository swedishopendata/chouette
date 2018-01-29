package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.PlannedTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DatedVehicleJourney")
public class DatedVehicleJourneyEntity implements DatedVehicleJourney {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "OperatingDayDate")
    private LocalDate operatingDayDate;

    @Column(name = "Gid")
    private long gid;

    @Column(name = "IsBasedOnVehicleJourneyId")
    private long isBasedOnVehicleJourneyId;

    @Column(name = "PlannedTypeCode")
    @Enumerated(EnumType.STRING)
    private PlannedTypeCode plannedTypeCode;

    @Column(name = "IsReplacedById")
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
    public LocalDate getOperatingDayDate() {
        return operatingDayDate;
    }

    public void setOperatingDayDate(LocalDate operatingDayDate) {
        this.operatingDayDate = operatingDayDate;
    }

    @Override
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    @Override
    public long getIsBasedOnVehicleJourneyId() {
        return isBasedOnVehicleJourneyId;
    }

    public void setIsBasedOnVehicleJourneyId(long isBasedOnVehicleJourneyId) {
        this.isBasedOnVehicleJourneyId = isBasedOnVehicleJourneyId;
    }

    @Override
    public PlannedTypeCode getPlannedTypeCode() {
        return plannedTypeCode;
    }

    public void setPlannedTypeCode(PlannedTypeCode plannedTypeCode) {
        this.plannedTypeCode = plannedTypeCode;
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
        return "DatedVehicleJourneyEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", operatingDayDate=" + operatingDayDate
                + ", gid=" + gid
                + ", isBasedOnVehicleJourneyId=" + isBasedOnVehicleJourneyId
                + ", plannedTypeCode=" + plannedTypeCode
                + ", isReplacedById=" + isReplacedById
                + '}';
    }
}
