package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.exchange.noptis.model.converter.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RouteLink")
@NoArgsConstructor
public class RouteLink extends NoptisDataSourceObject {

    private static final long serialVersionUID = 3241415831258603289L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "StartsAtJourneyPatternPointGid", nullable = false)
    private long startsAtJourneyPatternPointGid;

    @Getter
    @Setter
    @Column(name = "EndsAtJourneyPatternPointGid", nullable = false)
    private long endsAtJourneyPatternPointGid;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

    @Getter
    @Setter
    @Column(name = "DistanceMeters", nullable = false)
    private int distanceMeters;

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
