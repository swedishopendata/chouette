package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DestinationDisplay")
@NoArgsConstructor
public class DestinationDisplay extends NoptisDataSourceObject {

    private static final long serialVersionUID = 1906461481038430787L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "LineDesignation", nullable = false)
    private String lineDesignation;

    @Getter
    @Setter
    @Column(name = "PrimaryDestinationName", nullable = false)
    private String primaryDestinationName;

}
