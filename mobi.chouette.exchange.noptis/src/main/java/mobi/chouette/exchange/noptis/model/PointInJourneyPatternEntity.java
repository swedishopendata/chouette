package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.ArrivalType;
import mobi.chouette.exchange.noptis.model.type.DepartureType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PointInJourneyPattern")
public class PointInJourneyPatternEntity implements PointInJourneyPattern {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "IsInJourneyPatternId", nullable = false)
    private long isInJourneyPatternId;

    @Column(name = "IsJourneyPatternPointGid", nullable = false)
    private long isJourneyPatternPointGid;

    @Column(name = "SequenceNumber", nullable = false)
    private int sequenceNumber;

    @Column(name = "DepartureType", nullable = false)
    private DepartureType departureType;

    @Column(name = "ArrivalType", nullable = false)
    private ArrivalType arrivalType;

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
    public long getIsInJourneyPatternId() {
        return isInJourneyPatternId;
    }

    public void setIsInJourneyPatternId(long isInJourneyPatternPointId) {
        this.isInJourneyPatternId = isInJourneyPatternPointId;
    }

    @Override
    public long getIsJourneyPatternPointGid() {
        return isJourneyPatternPointGid;
    }

    public void setIsJourneyPatternPointGid(long isJourneyPatternPointGid) {
        this.isJourneyPatternPointGid = isJourneyPatternPointGid;
    }

    @Override
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public DepartureType getDepartureType() {
        return departureType;
    }

    public void setDepartureType(DepartureType departureType) {
        this.departureType = departureType;
    }

    @Override
    public ArrivalType getArrivalType() {
        return arrivalType;
    }

    public void setArrivalType(ArrivalType arrivalType) {
        this.arrivalType = arrivalType;
    }

    @Override
    public String toString() {
        return "PointInJourneyPatternEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", isInJourneyPatternId=" + isInJourneyPatternId
                + ", isJourneyPatternPointGid=" + isJourneyPatternPointGid
                + ", sequenceNumber=" + sequenceNumber
                + ", departureType=" + departureType
                + ", arrivalType=" + arrivalType
                + '}';
    }
}
