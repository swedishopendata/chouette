package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "Contractor")
public class ContractorEntity implements Contractor {
    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "Gid", nullable = false)
    private long gid;

    @Column(name = "Number", nullable = false)
    private int number;

    @Column(name = "IsPromotedByTransportAuthorityId", nullable = false)
    private long isPromotedByTransportAuhthorityId;

    @Column(name = "Code", nullable = false)
    private String code;

    @Column(name = "Name", nullable = false)
    private String name;

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

    @Override
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public long getIsPromotedByTransportAuthorityId() {
        return isPromotedByTransportAuhthorityId;
    }

    public void setIsPromotedByTransportAuhthorityId(long isPromotedByTransportAuhthorityId) {
        this.isPromotedByTransportAuhthorityId = isPromotedByTransportAuhthorityId;
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
        return "ContractorEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", gid=" + gid
                + ", number=" + number
                + ", isPromotedByTransportAuhthorityId=" + isPromotedByTransportAuhthorityId
                + ", code='" + code + '\''
                + ", name='" + name + '\''
                + ", existsFromDate=" + existsFromDate
                + ", existsUpToDate=" + existsUpToDate
                + '}';
    }
}
