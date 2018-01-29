package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.exchange.noptis.model.type.DirectionCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DirectionOfLine")
@NoArgsConstructor
@ToString(callSuper = true)
public class DirectionOfLine extends NoptisIdentifiedObject {

    private static final long serialVersionUID = -1068853786349408960L;

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
    @Column(name = "DirectionCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private DirectionCode directionCode;

    @Getter
    @Setter
    @Column(name = "Name")
    private String name;

    @Getter
    @Setter
    @Column(name = "IsOnLineId", nullable = false)
    private long isOnLineId;

    @Getter
    @Setter
    @Column(name = "ExistsFromDate", nullable = false)
    private LocalDate existsFromDate;

    @Getter
    @Setter
    @Column(name = "ExistsUpToDate")
    private LocalDate existsUpToDate;

    @Getter
    @Setter
    @Column(name = "DescriptionNote")
    private String descriptionNote;

}
