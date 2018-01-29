package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "NamedJourneyPattern")
public class NamedJourneyPatternEntity implements NamedJourneyPattern {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "ReferenceName")
    private String referenceName;

    @Column(name = "Gid")
    private long gid;

    @Column(name = "IsOnDirectionOfLineId")
    private Long isOnDirectionOfLineId;

    @Column(name = "IsInScopeOfContractorId")
    private Long isInScopeOfContractorId;

    @Column(name = "IsJourneyPatternId")
    private long isJourneyPatternId;

    @Column(name = "HasDestinationPatternId")
    private long hasDestinationPatternId;

    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Column(name = "ExistsUpToDate")
    private LocalDate existsUpToDate;

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
    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

    @Override
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    @Override
    public Long getIsOnDirectionOfLineId() {
        return isOnDirectionOfLineId;
    }

    public void setIsOnDirectionOfLineId(Long isOnDirectionOfLineId) {
        this.isOnDirectionOfLineId = isOnDirectionOfLineId;
    }

    @Override
    public Long getIsInScopeOfContractorId() {
        return isInScopeOfContractorId;
    }

    public void setIsInScopeOfContractorId(Long isInScopeOfContractorId) {
        this.isInScopeOfContractorId = isInScopeOfContractorId;
    }

    @Override
    public long getIsJourneyPatternId() {
        return isJourneyPatternId;
    }

    public void setIsJourneyPatternId(long isJourneyPatternId) {
        this.isJourneyPatternId = isJourneyPatternId;
    }

    @Override
    public long getHasDestinationPatternId() {
        return hasDestinationPatternId;
    }

    public void setHasDestinationPatternId(long hasDestinationPatternId) {
        this.hasDestinationPatternId = hasDestinationPatternId;
    }

    @Override
    public LocalDate getExistsFromDate() {
        return existsFromDate;
    }

    public void setExistsFromDate(LocalDate existsFromDate) {
        this.existsFromDate = existsFromDate;
    }

    @Override
    public LocalDate getExistsUpToDate() {
        return existsUpToDate;
    }

    public void setExistsUpToDate(LocalDate existsUpToDate) {
        this.existsUpToDate = existsUpToDate;
    }

    @Override
    public String toString() {
        return "NamedJourneyPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", referenceName='" + referenceName + '\''
                + ", gid=" + gid
                + ", isOnDirectionOfLineId=" + isOnDirectionOfLineId
                + ", isInScopeOfContractorId=" + isInScopeOfContractorId
                + ", isJourneyPatternId=" + isJourneyPatternId
                + ", hasDestinationPatternId=" + hasDestinationPatternId
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
