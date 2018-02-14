package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DatedConnection")
@NoArgsConstructor
public class DatedConnection extends NoptisDataSourceObject {

    private static final long serialVersionUID = 1192912440755238029L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsBasedOnConnectionCandidateId", nullable = false)
    private long isBasedOnConnectionCandidateId;

    @Getter
    @Setter
    @Column(name = "IsFromFeederDatedVehicleJourneyId", nullable = false)
    private long isFromFeederDatedVehicleJourneyId;

    @Getter
    @Setter
    @Column(name = "IsToFetcherDatedVehicleJourneyId", nullable = false)
    private long isToFetcherDatedVehicleJourneyId;

    @Getter
    @Setter
    @Column(name = "MinimumChangeDurationSeconds", nullable = true)
    private Integer minimumChangeDurationSeconds;

    @Getter
    @Setter
    @Column(name = "IsReplacedById", nullable = true)
    private Long isReplacedById;

}
