package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CallOnTimedJourneyPattern")
@NoArgsConstructor
public class CallOnTimedJourneyPattern extends NoptisDataSourceObject {

    private static final long serialVersionUID = -6790615358189307391L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsOnTimedJourneyPatternId", nullable = false)
    private long isOnTimedJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "IsOnPointInJourneyPatternId", nullable = false)
    private long isOnPointInJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "EarliestDepartureTimeOffsetSeconds", nullable = false)
    private int earliestDepartureTimeOffsetSeconds;

    @Getter
    @Setter
    @Column(name = "LatestArrivalTimeOffsetSeconds", nullable = false)
    private int latestArrivalTimeOffsetSeconds;

    @Getter
    @Setter
    @Column(name = "TimingPoint")
    private boolean timingPoint;

}
