package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "TransportAuthority")
@NoArgsConstructor
@ToString(callSuper = true)
public class TransportAuthority extends NoptisIdentifiedObject {

    private static final long serialVersionUID = -8484555298832653955L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsFromDataSourceId", nullable = false)
    private short isFromDataSourceId;

    @Getter
    @Setter
    @Column(name = "Number", nullable = false)
    private int number;

    @Getter
    @Setter
    @Column(name = "Code", nullable = false)
    private String code;

    @Getter
    @Setter
    @Column(name = "Name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "FormalName")
    private String formalName;

    @Getter
    @Setter
    @Column(name = "TimeTableReleaseForPublicUseUptoDate", nullable = false)
    private LocalDate timetableReleseaseForPublicUseUptoDate;

    @Getter
    @Setter
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Column(name = "ExistsUptoDate")
    private LocalDate existsUpToDate;

}
