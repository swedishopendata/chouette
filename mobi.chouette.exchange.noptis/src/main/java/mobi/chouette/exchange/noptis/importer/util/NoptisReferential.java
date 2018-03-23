package mobi.chouette.exchange.noptis.importer.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mobi.chouette.model.stip.StopPoint;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@ToString
public class NoptisReferential implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Map<Long, String> contractorIdMapping = new HashMap<>();

    @Getter
    @Setter
    private Map<Long, String> contractorGidMapping = new HashMap<>();

    @Getter
    @Setter
    private Map<Long, StopPoint> sharedStopPoints = new HashMap<>();

    @Getter
    @Setter
    private Set<Long> lineIds = new HashSet<>();

    public void clear() {
        lineIds.clear();
        sharedStopPoints.clear();
    }

    public void dispose() {
    }

}
