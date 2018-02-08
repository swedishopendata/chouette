package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JourneyPattern")
@NoArgsConstructor
public class JourneyPattern extends NoptisDataSourceObject {

    private static final long serialVersionUID = 2590038612110282325L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsOnDirectionOfLineId")
    private Long isOnDirectionOfLineId;

}
