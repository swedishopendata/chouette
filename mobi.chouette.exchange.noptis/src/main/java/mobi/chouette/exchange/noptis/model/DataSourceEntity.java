package mobi.chouette.exchange.noptis.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DataSource")
public class DataSourceEntity implements DataSource {

    @Id
    @Column(name = "Id")
    private short id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "DoiDbUser", nullable = false)
    private String doiDbUser;

    @Column(name = "DoiDbPassword", nullable = false)
    private String doiDbPassword;

    @Column(name = "DoiDbUrl", nullable = false)
    private String doiDbUrl;

    @Override
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDoiDbUser() {
        return doiDbUser;
    }

    public void setDoiDbUser(String doiDbUser) {
        this.doiDbUser = doiDbUser;
    }

    @Override
    public String getDoiDbPassword() {
        return doiDbPassword;
    }

    public void setDoiDbPassword(String doiDbPassword) {
        this.doiDbPassword = doiDbPassword;
    }

    @Override
    public String getDoiDbUrl() {
        return doiDbUrl;
    }

    public void setDoiDbUrl(String doiDbUrl) {
        this.doiDbUrl = doiDbUrl;
    }

    @Override
    public String toString() {
        return "DataSourceEntity{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", doiDbUser='" + doiDbUser + '\''
                + ", doiDbPassword='" + doiDbPassword + '\''
                + ", doiDbUrl='" + doiDbUrl + '\''
                + '}';
    }
}
