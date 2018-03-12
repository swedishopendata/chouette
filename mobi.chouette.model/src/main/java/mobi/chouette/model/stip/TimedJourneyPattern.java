package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TimedJourneyPattern")
@NoArgsConstructor
@ToString(callSuper = true)
public class TimedJourneyPattern extends NoptisDataSourceObject {

    private static final long serialVersionUID = 1472087114349377230L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "IsBasedOnJourneyPatternId", nullable = false)
    private long isBasedOnJourneyPatternId;

}
