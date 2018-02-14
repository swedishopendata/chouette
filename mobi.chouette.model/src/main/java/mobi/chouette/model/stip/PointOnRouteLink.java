package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.model.stip.converter.LocalDateConverter;
import mobi.chouette.model.stip.type.PointOnLinkTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PointOnRouteLink")
@NoArgsConstructor
public class PointOnRouteLink extends NoptisDataSourceObject {

    private static final long serialVersionUID = -6665416515286275873L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "PointOnLinkTypeCode")
    @Enumerated(EnumType.STRING)
    private PointOnLinkTypeCode pointOnLinkTypeCode;

    @Getter
    @Setter
    @Column(name = "AtOffsetMeters")
    private Double atOffsetMeters;

    @Getter
    @Setter
    @Column(name = "DurationFromBeginningSeconds")
    private Integer durationFromBeginningSeconds;

    @Getter
    @Setter
    @Column(name = "DurationBeforeLatestArrivalSeconds")
    private Integer durationFromLatestArrival;

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
    @Column(name = "IsOnRouteLinkId")
    private long isOnRouteLinkId;

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
