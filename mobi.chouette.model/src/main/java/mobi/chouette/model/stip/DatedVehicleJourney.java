package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.type.PlannedTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DatedVehicleJourney")
@NoArgsConstructor
@ToString(callSuper = true)
public class DatedVehicleJourney extends NoptisIdentifiedObject {

    private static final long serialVersionUID = -1120186859203997846L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "OperatingDayDate")
    private LocalDate operatingDayDate;

    @Getter
    @Setter
    @Column(name = "IsBasedOnVehicleJourneyId")
    private long isBasedOnVehicleJourneyId;

    @Getter
    @Setter
    @Column(name = "PlannedTypeCode")
    @Enumerated(EnumType.STRING)
    private PlannedTypeCode plannedTypeCode;

    @Getter
    @Setter
    @Column(name = "IsReplacedById")
    private Long isReplacedById;

}
