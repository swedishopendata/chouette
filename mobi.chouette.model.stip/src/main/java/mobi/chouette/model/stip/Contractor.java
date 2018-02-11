package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.converter.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Contractor")
@NoArgsConstructor
@ToString(callSuper = true)
public class Contractor extends NoptisIdentifiedObject {

    private static final long serialVersionUID = 832125517680054862L;

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
    @Column(name = "IsPromotedByTransportAuthorityId", nullable = false)
    private long isPromotedByTransportAuhthorityId;

    @Getter
    @Setter
    @Column(name = "Code", nullable = false)
    private String code;

    @Getter
    @Setter
    @Column(name = "Name", nullable = false)
    private String name;

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
