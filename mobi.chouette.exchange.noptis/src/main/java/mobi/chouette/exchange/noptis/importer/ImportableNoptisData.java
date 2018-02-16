package mobi.chouette.exchange.noptis.importer;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.TransportAuthority;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ImportableNoptisData {

    @Getter
    @Setter
    private Line line;

    @Getter
    @Setter
    private Set<TransportAuthority> transportAuthorities = new HashSet<>();

    @Getter
    @Setter
    private Map<String, TransportAuthority> sharedTransportAuthorities = new HashMap<>();

    public void clear() {
        line = null;
        transportAuthorities.clear();
    }

    public void dispose() {
        clear();
        sharedTransportAuthorities.clear();
    }

}
