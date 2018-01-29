package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DestinationDisplay")
public class DestinationDisplayEntity implements DestinationDisplay {

    @Id
    @Column(name = "Id")
    private long id;

    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Column(name = "LineDesignation", nullable = false)
    private String lineDesignation;

    @Column(name = "PrimaryDestinationName", nullable = false)
    private String primaryDestinationName;

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
    public String getLineDesignation() {
        return lineDesignation;
    }

    public void setLineDesignation(String lineDesignation) {
        this.lineDesignation = lineDesignation;
    }

    @Override
    public String getPrimaryDestinationName() {
        return primaryDestinationName;
    }

    public void setPrimaryDestinationName(String primaryDestinationName) {
        this.primaryDestinationName = primaryDestinationName;
    }

    @Override
    public String toString() {
        return "DestinationDisplayEntity{"
                + "id=" + id
                + ", isFromDataSourceId=" + isFromDataSourceId
                + ", lineDesignation='" + lineDesignation + '\''
                + ", primaryDestinationName='" + primaryDestinationName + '\''
                + '}';
    }
}
