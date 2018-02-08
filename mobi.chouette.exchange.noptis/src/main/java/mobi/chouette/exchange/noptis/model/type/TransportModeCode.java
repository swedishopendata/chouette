package mobi.chouette.exchange.noptis.model.type;

public enum TransportModeCode {
    BUS("BUS"),
    TRAM("TRAM"),
    METRO("METRO"),
    TRAIN("TRAIN"),
    FERRY("FERRY"),
    SHIP("SHIP"),
    TAXI("TAXI"),
    UNSPECIFIED(null);

    private String codeValue;

    TransportModeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public static TransportModeCode getTransportModeCode(String codeValue) {
        if (codeValue == null) {
            return UNSPECIFIED;
        }
        for (TransportModeCode transportModeCode : TransportModeCode.values()) {
            if (codeValue.equals(transportModeCode.getCodeValue())) {
                return transportModeCode;
            }
        }
        throw new IllegalArgumentException("No transportModeCode exists with codeValue: " + codeValue);
    }
}
