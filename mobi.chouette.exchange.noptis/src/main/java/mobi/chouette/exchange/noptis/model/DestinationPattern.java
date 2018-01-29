package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DestinationPattern")
@NoArgsConstructor
public class DestinationPattern extends NoptisDataSourceObject {

    private static final long serialVersionUID = 8931132694848117944L;

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
    @Column(name = "IsOnJourneyPatternId")
    private long isOnJourneyPatternId;

}
