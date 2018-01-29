package mobi.chouette.exchange.noptis.model.type;

public enum StopAreaTypeCode {
    AIRPORT("AIRPORT"),
    BUSTERM("BUSTERM"),
    FERRYBER("FERRYBER"),
    METROSTN("METROSTN"),
    RAILWSTN("RAILWSTN"),
    TRAMSTN("TRAMSTN"),
    SHIPBER("SHIPBER"),
    TAXITERM("TAXITERM"),
    UNKNOWN("UNKNOWN");

    private String codeValue;

    StopAreaTypeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public static StopAreaTypeCode getTypeCode(String codeValue) {
        for (StopAreaTypeCode stopAreaTypeCode : StopAreaTypeCode.values()) {
            if (stopAreaTypeCode.getCodeValue().equals(codeValue)) {
                return stopAreaTypeCode;
            }
        }
        throw new IllegalArgumentException("No typeCode exists with codeValue: " + codeValue);
    }
}
