package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.converter.OffsetDayTimeConverter;
import mobi.chouette.model.stip.util.OffsetDayTime;

import javax.persistence.*;

@Entity
@Table(name = "VehicleJourney")
@NoArgsConstructor
@ToString(callSuper = true)
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
