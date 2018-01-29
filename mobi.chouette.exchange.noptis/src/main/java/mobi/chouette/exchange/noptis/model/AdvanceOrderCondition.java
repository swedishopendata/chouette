package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;

@Entity
@Table(name = "AdvanceOrderCondition")
@NoArgsConstructor
public class AdvanceOrderCondition extends NoptisDataSourceObject {

    private static final long serialVersionUID = -2822778947806455906L;

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
    @Column(name = "IsAppliedToVehicleJourneyId", nullable = false)
    private long isAppliedToVehicleJourneyId;

    @Getter
    @Setter
    @Column(name = "MinimumDaysInAdvanceCount")
    private Integer minimumDaysInAdvanceCount;

    @Getter
    @Setter
    @Column(name = "LatestAbsoluteTime")
    private LocalTime latestAbsoluteTime;

    @Getter
    @Setter
    @Column(name = "LatestTimeSpanInAdvanceDurationSeconds")
    private Integer latestTimeSpanInAdvanceDurationSeconds;

    @Getter
    @Setter
    @Column(name = "PublicYesNo")
    private boolean publicYesNo;

    @Getter
    @Setter
    @Column(name = "PublicNote")
    private String publicNote;

}
