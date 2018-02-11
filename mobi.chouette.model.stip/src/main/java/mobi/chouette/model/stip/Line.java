package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Line")
@NoArgsConstructor
@ToString(callSuper = true)
public class Line {

    private static final long serialVersionUID = -303098337520961027L;

    @Getter
    @Setter
    @Id
    @Column(name = "Id")
    private Long id;

}
