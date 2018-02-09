package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.exchange.noptis.model.converter.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "NamedJourneyPattern")
@NoArgsConstructor
@ToString(callSuper = true)
public class NamedJourneyPattern extends NoptisIdentifiedObject {

    private static final long serialVersionUID = -6351023476346666640L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "ReferenceName")
    private String referenceName;

    @Getter
    @Setter
    @Column(name = "IsOnDirectionOfLineId")
    private Long isOnDirectionOfLineId;

    @Getter
    @Setter
    @Column(name = "IsInScopeOfContractorId")
    private Long isInScopeOfContractorId;

    @Getter
    @Setter
    @Column(name = "IsJourneyPatternId")
    private long isJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "HasDestinationPatternId")
    private long hasDestinationPatternId;

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
