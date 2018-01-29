package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.DirectionCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DirectionOfLine")
public class DirectionOfLineEntity implements DirectionOfLine {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "DirectionCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private DirectionCode directionCode;

    @Column(name = "Name")
    private String name;

    @Column(name = "IsOnLineId", nullable = false)
    private long isOnLineId;

    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Column(name = "ExistsUpToDate")
    private LocalDate existsUpToDate;

    @Column(name = "DescriptionNote")
    private String descriptionNote;

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
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    @Override
    public DirectionCode getDirectionCode() {
        return directionCode;
    }

    public void setDirectionCode(DirectionCode directionCode) {
        this.directionCode = directionCode;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getIsOnLineId() {
        return isOnLineId;
    }

    public void setIsOnLineId(long isOnLineId) {
        this.isOnLineId = isOnLineId;
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
    public String getDescriptionNote() {
        return descriptionNote;
    }

    public void setDescriptionNote(String descriptionNote) {
        this.descriptionNote = descriptionNote;
    }

    @Override
    public String toString() {
        return "DirectionOfLineEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", directionCode=" + directionCode
                + ", name='" + name + '\''
                + ", isOnLineId=" + isOnLineId
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + ", descriptionNote='" + descriptionNote + '\''
                + '}';
    }
}
