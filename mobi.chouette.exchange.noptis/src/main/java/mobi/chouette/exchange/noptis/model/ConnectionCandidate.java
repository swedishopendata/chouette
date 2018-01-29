package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ConnectionCandidate")
@NoArgsConstructor
public class ConnectionCandidate extends NoptisDataSourceObject {

    private static final long serialVersionUID = 6478240773357553154L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsFromFeederVehicleJourneyId", nullable = false)
    private long isFromFeederVehicleJourneyId;

    @Getter
    @Setter
    @Column(name = "IsFromFeederCallOnTimedJourneyPatternId", nullable = false)
    private long isFromFeederCallOnTimedJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "IsToFetcherVehicleJourneyId", nullable = false)
    private long isToFetcherVehicleJourneyId;

    @Getter
    @Setter
    @Column(name = "IsToFetcherCallOnTimedJourneyPatternId", nullable = false)
    private long isToFetcherCallOnTimedJourneyPatternId;

}
