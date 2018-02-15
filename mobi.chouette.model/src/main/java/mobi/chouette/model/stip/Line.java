package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.converter.LocalDateConverter;
import mobi.chouette.model.stip.type.TransportModeCode;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    //private long isDefinedByTransportAuthorityId;
    private Long isDefinedByTransportAuthorityId;

    @Getter
    @Setter
    @Column(name = "Monitored")
    private boolean monitored;

    @Getter
    @Setter
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "ExistsUpToDate")
    private LocalDate existsUpToDate;

}
