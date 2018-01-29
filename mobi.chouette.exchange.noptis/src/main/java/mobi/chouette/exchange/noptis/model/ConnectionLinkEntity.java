package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransferModeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ConnectionLink")
public class ConnectionLinkEntity implements ConnectionLink {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "StartsAtJourneyPatternPointGid", nullable = false)
    private long startsAtJourneyPatternPointGid;

    @Column(name = "EndsAtJourneyPatternPointGid", nullable = false)
    private long endsAtJourneyPatternPointGid;

    @Column(name = "TransferModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferModeCode transferModeCode;

    @Column(name = "DistanceMeters")
    private Integer distanceMeters;

    @Column(name = "DefaultDurationSeconds", nullable = false)
    private int defaultDurationSeconds;

    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Column(name = "ExistsUptoDate")
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
    public long getStartsAtJourneyPatternPointGid() {
        return startsAtJourneyPatternPointGid;
    }

    public void setStartsAtJourneyPatternPointGid(long startsAtJourneyPatternPointGid) {
        this.startsAtJourneyPatternPointGid = startsAtJourneyPatternPointGid;
    }

    @Override
    public long getEndsAtJourneyPatternPointGid() {
        return endsAtJourneyPatternPointGid;
    }

    public void setEndsAtJourneyPatternPointGid(long endsAtJourneyPatternPointGid) {
        this.endsAtJourneyPatternPointGid = endsAtJourneyPatternPointGid;
    }

    @Override
    public TransferModeCode getTransferModeCode() {
        return transferModeCode;
    }

    public void setTransferModeCode(TransferModeCode transferModeCode) {
        this.transferModeCode = transferModeCode;
    }

    @Override
    public Integer getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Integer distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    @Override
    public int getDefaultDurationSeconds() {
        return defaultDurationSeconds;
    }

    public void setDefaultDurationSeconds(int defaultDurationSeconds) {
        this.defaultDurationSeconds = defaultDurationSeconds;
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
        return "ConnectionLinkEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", startsAtJourneyPatternPointGid=" + startsAtJourneyPatternPointGid
                + ", endsAtJourneyPatternPointGid=" + endsAtJourneyPatternPointGid
                + ", transferModeCode=" + transferModeCode
                + ", distanceMeters=" + distanceMeters
                + ", defaultDurationSeconds=" + defaultDurationSeconds
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
