package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.persistence.*;

@Entity
@Table(name = "VehicleJourneyTemplate")
@NoArgsConstructor
public class VehicleJourneyTemplate extends NoptisDataSourceObject {

    private static final long serialVersionUID = 2013669579604706128L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsWorkedOnTimedJourneyPatternId", nullable = false)
    private long isWorkedOnTimedJourneyPatternId;

    @Getter
    @Setter
    @Column(name = "UsesNamedJourneyPatternGid", nullable = false)
    private long usesNamedJourneyPatternGid;

    @Getter
    @Setter
    @Column(name = "IsWorkedOnDirectionOfLineGid", nullable = false)
    private Long isWorkedOnDirectionOfLineGid;

    @Getter
    @Setter
    @Column(name = "TransportModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportModeCode transportModeCode;

    @Getter
    @Setter
    @Column(name = "ContractorGid")
    private Long contractorGid;

    @Getter
    @Setter
    @Column(name = "IsWorkedAccordingToServiceCalendarId", nullable = false)
    private long isWorkedAccordingToServiceCalendarId;

    @Getter
    @Setter
    @Column(name = "ExposedInPrintMedia")
    private boolean exposedInPrintMedia;

    @Getter
    @Setter
    @Column(name = "UsesServiceRequirementPatternId", nullable = false)
    private long usesServiceRequirementPatternId;

}
