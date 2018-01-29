package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    @Column(name = "IsFromDataSourceId")
    private short isFromDataSourceId;

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
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Column(name = "ExistsUptoDate")
    private LocalDate existsUpToDate;

}
