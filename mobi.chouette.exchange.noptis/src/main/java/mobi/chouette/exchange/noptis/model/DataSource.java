package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DataSource")
@NoArgsConstructor
public class DataSource extends NoptisObject {

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "Name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "DoiDbUser", nullable = false)
    private String doiDbUser;

    @Getter
    @Setter
    @Column(name = "DoiDbPassword", nullable = false)
    private String doiDbPassword;

    @Getter
    @Setter
    @Column(name = "DoiDbUrl", nullable = false)
    private String doiDbUrl;

}
