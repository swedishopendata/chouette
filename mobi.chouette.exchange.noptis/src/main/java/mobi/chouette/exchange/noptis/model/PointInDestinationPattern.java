package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PointInDestinationPattern")
@NoArgsConstructor
public class PointInDestinationPattern extends NoptisDataSourceObject {

    private static final long serialVersionUID = 1250566725589921485L;

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
    @Column(name = "IsInDestinationPatternId", nullable = false)
    private long isInDestinationPatternId;

    @Getter
    @Setter
    @Column(name = "IsOnPointInJourneyPatternId", nullable = false)
    private long isOnPointInJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "hasDestinationDisplayId")
    private Long hasDestinationDisplayId;

}
