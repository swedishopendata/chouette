package mobi.chouette.exchange.noptis.importer.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@ToString()
public class NoptisReferential implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private Set<Long> lineIds = new HashSet<>();

    public void clear() {
        lineIds.clear();
    }

    public void dispose() {
    }

}
