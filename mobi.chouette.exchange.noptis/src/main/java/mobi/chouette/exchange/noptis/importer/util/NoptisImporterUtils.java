package mobi.chouette.exchange.noptis.importer.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class NoptisImporterUtils {

    private static final Map<String, Short> DATA_SOURCES = ImmutableMap.of(
            "OTR", (short) 5, "KUL", (short) 3, "VTR", (short) 14, "SLT", (short) 1);

    public static short getDataSourceId(String objectIdPrefix) {
        return DATA_SOURCES.get(objectIdPrefix);
    }

    private static final Map<String, String> DATA_SOURCE_NAMES = ImmutableMap.of(
            "OTR", "AB Östgötatrafiken", "KUL", "Kollektivtrafikförvaltningen UL", "VTR", "Västtrafik AB", "SLT", "Storstockholms Lokaltrafik");

    public static String getDataSourceName(String objectIdPrefix) {
        return DATA_SOURCE_NAMES.get(objectIdPrefix);
    }

}
