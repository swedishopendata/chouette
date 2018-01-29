package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Line")
@NoArgsConstructor
@ToString(callSuper = true)
public class Line extends NoptisIdentifiedObject {

    private static final long serialVersionUID = -303098337520961027L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "Number", nullable = false)
    private short number;

    @Getter
    @Setter
    @Column(name = "Name")
    private String name;

    @Getter
    @Setter
    @Column(name = "Designation", nullable = false)
    private String designation;

    @Getter
    @Setter
    @Column(name = "DefaultTransportModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportModeCode defaultTransportModeCode;

    @Getter
    @Setter
    @Column(name = "IsDefinedByTransportAuthorityId", nullable = false)
    private long isDefinedByTransportAuthorityId;

    @Getter
    @Setter
    @Column(name = "Monitored")
    private boolean monitored;

    @Getter
    @Setter
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Column(name = "ExistsUpToDate")
    private LocalDate existsUpToDate;

}
