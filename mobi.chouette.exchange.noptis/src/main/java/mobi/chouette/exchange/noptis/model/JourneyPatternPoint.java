package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.exchange.noptis.model.converter.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "JourneyPatternPoint")
@NoArgsConstructor
@ToString(callSuper = true)
public class JourneyPatternPoint extends NoptisIdentifiedObject {

    private static final long serialVersionUID = -5709567812288937708L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "Number", nullable = false)
    private int number;

    @Getter
    @Setter
    @Column(name = "IsDefinedByTransportAuthorityId", nullable = false)
    private long isDefinedByTransportAuthorityId;

    @Getter
    @Setter
    @Column(name = "CoordinateSystemName")
    private String coordinateSystemName;

    @Getter
    @Setter
    @Column(name = "LocationNorthingCoordinate")
    private String locationNorthingCoordinate;

    @Getter
    @Setter
    @Column(name = "LocationEastingCoordinate")
    private String locationEastingCoordinate;

    @Getter
    @Setter
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Convert(converter = LocalDateConverter.class)
    @Column(name = "ExistsUptoDate")
    private LocalDate existsUpToDate;

}
