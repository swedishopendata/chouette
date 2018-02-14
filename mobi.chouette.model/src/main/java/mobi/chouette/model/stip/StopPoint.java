package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.converter.LocalDateConverter;
import mobi.chouette.model.stip.type.StopPointTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "StopPoint")
@NoArgsConstructor
@ToString(callSuper = true)
public class StopPoint extends NoptisIdentifiedObject {

    private static final long serialVersionUID = 7309390943720162102L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "Name")
    private String name;

    @Getter
    @Setter
    @Column(name = "ShortName")
    private String shortName;

    @Getter
    @Setter
    @Column(name = "Designation")
    private String designation;

    @Getter
    @Setter
    @Column(name = "LocalNumber", nullable = false)
    private short localNumber;

    @Getter
    @Setter
    @Column(name = "IsJourneyPatternPointGid", nullable = false)
    private long isJourneyPatternPointGid;

    @Getter
    @Setter
    @Column(name = "IsIncludedInStopAreaGid", nullable = false)
    private long isIncludedInStopAreaGid;

    @Getter
    @Setter
    @Column(name = "TypeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private StopPointTypeCode typeCode;

    @Getter
    @Setter
    @Column(name = "ForAlighting", nullable = false)
    private boolean forAlighting;

    @Getter
    @Setter
    @Column(name = "ForBoarding", nullable = false)
    private boolean forBoarding;

    @Getter
    @Setter
    @Column(name = "Fictitious", nullable = false)
    private boolean fictitious;

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
