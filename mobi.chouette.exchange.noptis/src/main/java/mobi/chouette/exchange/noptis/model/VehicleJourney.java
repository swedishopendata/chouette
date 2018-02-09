package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.exchange.noptis.model.converter.OffsetDayTimeConverter;
import mobi.chouette.exchange.noptis.model.util.OffsetDayTime;

import javax.persistence.*;

@Entity
@Table(name = "VehicleJourney")
@NoArgsConstructor
public class VehicleJourney extends NoptisDataSourceObject {

    private static final long serialVersionUID = 4534614872750692222L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Convert(converter = OffsetDayTimeConverter.class)
    @Column(name = "PlannedStartOffsetDateTime", nullable = false)
    private OffsetDayTime plannedStartOffsetDayTime;

    @Getter
    @Setter
    @Convert(converter = OffsetDayTimeConverter.class)
    @Column(name = "PlannedEndOffsetDateTime", nullable = false)
    private OffsetDayTime plannedEndOffsetDayTime;

    @Getter
    @Setter
    @Column(name = "IsDescribedByVehicleJourneyTemplateId", nullable = false)
    private long isDescribedByVehicleJourneyTemplateId;

}
