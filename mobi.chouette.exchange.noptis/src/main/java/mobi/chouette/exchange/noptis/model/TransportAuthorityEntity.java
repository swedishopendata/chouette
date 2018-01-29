package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "TransportAuthority")
public class TransportAuthorityEntity implements TransportAuthority {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId", nullable = false)
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "Number", nullable = false)
    private int number;

    @Column(name = "Code", nullable = false)
    private String code;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "FormalName")
    private String formalName;

    @Column(name = "TimeTableReleaseForPublicUseUptoDate", nullable = false)
    private LocalDate timetableReleseaseForPublicUseUptoDate;

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
    public long getGid() {
        return gid;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public DataSource getDataSource() {
        throw new RuntimeException("Implement this!");
    }

    @Override
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public LocalDate getTimetableReleasesForPublicUseUptoDate() {
        return timetableReleseaseForPublicUseUptoDate;
    }

    public void setTimetableReleseaseForPublicUseUptoDate(LocalDate timetableReleseaseForPublicUseUptoDate) {
        this.timetableReleseaseForPublicUseUptoDate = timetableReleseaseForPublicUseUptoDate;
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
    public String getFormalName() {
        return formalName;
    }

    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }

    @Override
    public String toString() {
        return "TransportAuthorityEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", number=" + number
                + ", code='" + code + '\''
                + ", name='" + name + '\''
                + ", formalName='" + formalName + '\''
                + ", timetableReleseaseForPublicUseUptoDate=" + timetableReleseaseForPublicUseUptoDate
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }

}
