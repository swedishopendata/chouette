package mobi.chouette.exchange.noptis.importer;

import lombok.Getter;
import lombok.Setter;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.TransportAuthority;

import java.util.HashMap;
import java.util.Map;

public class ImportableNoptisData {

    @Getter
    @Setter
    private Line line;

    @Getter
    @Setter
    private Map<String, TransportAuthority> sharedTransportAuthorities = new HashMap<>();

    public void clear() {
        // TODO : clear line specific data below
    }

    public void dispose() {
        clear();
        // TODO : clea shared data below
    }

}
