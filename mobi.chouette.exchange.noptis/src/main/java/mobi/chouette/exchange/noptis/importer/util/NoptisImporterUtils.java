package mobi.chouette.exchange.noptis.importer.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class NoptisImporterUtils {

    private static final Map<String, Short> DATA_SOURCES = ImmutableMap.of(
            "OTR", (short) 4, "ULA", (short) 3, "VTK", (short) 14, "SLT", (short) 3);

    public static short getDataSourceId(String objectIdPrefix) {
        return DATA_SOURCES.get(objectIdPrefix);
    }

    private static final Map<String, String> DATA_SOURCE_NAMES = ImmutableMap.of(
            "OTR", "AB Östgötatrafiken", "ULA", "Kollektivtrafikförvaltningen UL", "VTK", "Västtrafik AB", "SLT", "Stockholms Lokaltrafik");

    public static String getDataSourceName(String objectIdPrefix) {
        return DATA_SOURCE_NAMES.get(objectIdPrefix);
    }

}
