package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.converter.LocalDateConverter;
import mobi.chouette.model.stip.type.StopAreaTypeCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "StopArea")
@NoArgsConstructor
@ToString(callSuper = true)
public class StopArea extends NoptisIdentifiedObject {

    private static final long serialVersionUID = 4731642204508063552L;

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
    @Column(name = "Name", nullable = false)
    private String name;

    @Getter
    @Setter
    @Column(name = "ShortName")
    private String shortName;

    @Getter
    @Setter
    @Column(name = "TypeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private StopAreaTypeCode typeCode;

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
    @Column(name = "CentroidNorthingCoordinate")
    private String centroidNorthingCoordinate;

    @Getter
    @Setter
    @Column(name = "CentroidEastingCoordinate")
    private String centroidEastingCoordinate;

    @Getter
    @Setter
    @Column(name = "DefaultInterchangeDurationSeconds")
    private Integer defaultInterchangeDurationSeconds;

    @Getter
    @Setter
    @Column(name = "InterchangePriority")
    private Short interchangePriority;

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
