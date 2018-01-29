package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.exchange.noptis.model.type.TransferModeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ConnectionLink")
@NoArgsConstructor
public class ConnectionLink extends NoptisDataSourceObject {

    private static final long serialVersionUID = -7382750771644698538L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

    @Getter
    @Setter
    @Column(name = "StartsAtJourneyPatternPointGid", nullable = false)
    private long startsAtJourneyPatternPointGid;

    @Getter
    @Setter
    @Column(name = "EndsAtJourneyPatternPointGid", nullable = false)
    private long endsAtJourneyPatternPointGid;

    @Getter
    @Setter
    @Column(name = "TransferModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransferModeCode transferModeCode;

    @Getter
    @Setter
    @Column(name = "DistanceMeters")
    private Integer distanceMeters;

    @Getter
    @Setter
    @Column(name = "DefaultDurationSeconds", nullable = false)
    private int defaultDurationSeconds;

    @Getter
    @Setter
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Column(name = "ExistsUptoDate")
    private LocalDate existsUpToDate;

}
