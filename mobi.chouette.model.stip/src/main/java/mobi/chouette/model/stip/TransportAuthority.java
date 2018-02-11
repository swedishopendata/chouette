package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.converter.LocalDateConverter;

import javax.persistence.*;
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
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "TimeTableReleaseForPublicUseUptoDate", nullable = false)
    private LocalDate timetableReleseaseForPublicUseUptoDate;

    @Getter
    @Setter
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "ExistsUptoDate")
    private LocalDate existsUpToDate;

}
