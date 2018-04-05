package mobi.chouette.exchange.noptis.importer.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class NoptisImporterUtils {

    private static final Map<String, String> DATA_SOURCE_NAMES = ImmutableMap.of(
            "OTR", "AB Östgötatrafiken", "KUL", "Kollektivtrafikförvaltningen UL", "VTR", "Västtrafik AB", "SLT", "Storstockholms Lokaltrafik");

    public static String getDataSourceName(String objectIdPrefix) {
        return DATA_SOURCE_NAMES.get(objectIdPrefix);
    }

    private static final Map<String, String> DATA_SOURCE_SHORT_NAMES = ImmutableMap.of(
            "OTR", "otraf", "KUL", "ul", "VTR", "vt", "SLT", "sl");

    public static String getDataSourceShortName(String objectIdPrefix) {
        return DATA_SOURCE_SHORT_NAMES.get(objectIdPrefix);
    }

}
