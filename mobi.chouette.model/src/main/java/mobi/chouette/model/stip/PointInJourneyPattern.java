package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.model.stip.type.ArrivalType;
import mobi.chouette.model.stip.type.DepartureType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PointInJourneyPattern")
@NoArgsConstructor
public class PointInJourneyPattern extends NoptisDataSourceObject {

    private static final long serialVersionUID = -4969226411596859858L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsInJourneyPatternId", nullable = false)
    private long isInJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "IsJourneyPatternPointGid", nullable = false)
    private long isJourneyPatternPointGid;

    @Getter
    @Setter
    @Column(name = "SequenceNumber", nullable = false)
    private int sequenceNumber;

    @Getter
    @Setter
    @Column(name = "DepartureType", nullable = false)
    private DepartureType departureType;

    @Getter
    @Setter
    @Column(name = "ArrivalType", nullable = false)
    private ArrivalType arrivalType;

}
