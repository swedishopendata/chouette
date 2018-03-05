package mobi.chouette.exchange.noptis.importer.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class NoptisImporterUtils {

    private static final Map<String, Short> DATA_SOURCES = ImmutableMap.of(
            "OTR", (short) 4, "ULA", (short) 3, "VTK", (short) 14, "SLT", (short) 3);

    public static short getDataSourceId(String objectIdPrefix) {
        return DATA_SOURCES.get(objectIdPrefix);
    }

}
