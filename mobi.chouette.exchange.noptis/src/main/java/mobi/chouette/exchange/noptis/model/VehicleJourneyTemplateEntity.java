package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.persistence.*;

@Entity
@Table(name = "VehicleJourneyTemplate")
public class VehicleJourneyTemplateEntity implements VehicleJourneyTemplate {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsWorkedOnTimedJourneyPatternId", nullable = false)
    private long isWorkedOnTimedJourneyPatternId;

    @Column(name = "UsesNamedJourneyPatternGid", nullable = false)
    private long usesNamedJourneyPatternGid;

    @Column(name = "IsWorkedOnDirectionOfLineGid", nullable = false)
    private Long isWorkedOnDirectionOfLineGid;

    @Column(name = "TransportModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportModeCode transportModeCode;

    @Column(name = "ContractorGid")
    private Long contractorGid;

    @Column(name = "IsWorkedAccordingToServiceCalendarId", nullable = false)
    private long isWorkedAccordingToServiceCalendarId;

    @Column(name = "ExposedInPrintMedia")
    private boolean exposedInPrintMedia;

    @Column(name = "UsesServiceRequirementPatternId", nullable = false)
    private long usesServiceRequirementPatternId;

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
    public long getIsWorkedOnTimedJourneyPatternId() {
        return isWorkedOnTimedJourneyPatternId;
    }

    public void setIsWorkedOnTimedJourneyPatternId(long isWorkedOnTimedJourneyPatternId) {
        this.isWorkedOnTimedJourneyPatternId = isWorkedOnTimedJourneyPatternId;
    }

    @Override
    public long getUsesNamedJourneyPatternGid() {
        return usesNamedJourneyPatternGid;
    }

    public void setUsesNamedJourneyPatternGid(long usesNamedJourneyPatternGid) {
        this.usesNamedJourneyPatternGid = usesNamedJourneyPatternGid;
    }

    @Override
    public Long getIsWorkedOnDirectionOfLineGid() {
        return isWorkedOnDirectionOfLineGid;
    }

    public void setIsWorkedOnDirectionOfLineGid(Long isWorkedOnDirectionOfLineGid) {
        this.isWorkedOnDirectionOfLineGid = isWorkedOnDirectionOfLineGid;
    }

    @Override
    public TransportModeCode getTransportModeCode() {
        return transportModeCode;
    }

    public void setTransportModeCode(TransportModeCode transportModeCode) {
        this.transportModeCode = transportModeCode;
    }

    @Override
    public Long getContractorGid() {
        return contractorGid;
    }

    public void setContractorGid(Long contractorGid) {
        this.contractorGid = contractorGid;
    }

    @Override
    public long getIsWorkedAccordingToServiceCalendarId() {
        return isWorkedAccordingToServiceCalendarId;
    }

    public void setIsWorkedAccordingToServiceCalendarId(long isWorkedAccordingToServiceCalendarId) {
        this.isWorkedAccordingToServiceCalendarId = isWorkedAccordingToServiceCalendarId;
    }

    @Override
    public boolean isExposedInPrintMedia() {
        return exposedInPrintMedia;
    }

    public void setExposedInPrintMedia(boolean exposedInPrintMedia) {
        this.exposedInPrintMedia = exposedInPrintMedia;
    }

    @Override
    public long getUsesServiceRequirementPatternId() {
        return usesServiceRequirementPatternId;
    }

    public void setUsesServiceRequirementPatternId(long usesServiceRequirementPatternId) {
        this.usesServiceRequirementPatternId = usesServiceRequirementPatternId;
    }

    @Override
    public String toString() {
        return "VehicleJourneyTemplateEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isWorkedOnTimedJourneyPatternId=" + isWorkedOnTimedJourneyPatternId
                + ", usesNamedJourneyPatternGid=" + usesNamedJourneyPatternGid
                + ", isWorkedOnDirectionOfLineGid=" + isWorkedOnDirectionOfLineGid
                + ", transportModeCode=" + transportModeCode
                + ", contractorGid=" + contractorGid
                + ", isWorkedAccordingToServiceCalendarId=" + isWorkedAccordingToServiceCalendarId
                + ", exposedInPrintMedia=" + exposedInPrintMedia
                + ", usesServiceRequirementPatternId=" + usesServiceRequirementPatternId
                + '}';
    }
}
