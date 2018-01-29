package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

import javax.persistence.*;

@Entity
@Table(name = "RouteLinkTraversableByTransportMode")
@NoArgsConstructor
public class RouteLinkTraversableByTransportMode extends NoptisDataSourceObject {

    private static final long serialVersionUID = -2974942732526624176L;

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
    @Column(name = "IsRouteLinkId", nullable = false)
    private long isRouteLinkId;

    @Getter
    @Setter
    @Column(name = "TransportModeCode", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransportModeCode transportModeCode;

}
