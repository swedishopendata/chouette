package mobi.chouette.model.stip.type;

public enum StopPointTypeCode {
    BUSSTOP("BUSSTOP"),
    REFUGE("REFUGE"),
    PLATFORM("PLATFORM"),
    TRACK("TRACK"),
    GATE("GATE"),
    PIER("PIER"),
    ENTRANCE("ENTRANCE"),
    EXIT("EXIT"),
    UNKNOWN("UNKNOWN");

    private String codeValue;

    StopPointTypeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public static StopPointTypeCode getTypeCode(String codeValue) {
        for (StopPointTypeCode stopAreaTypeCode : StopPointTypeCode.values()) {
            if (stopAreaTypeCode.getCodeValue().equals(codeValue)) {
                return stopAreaTypeCode;
            }
        }
        throw new IllegalArgumentException("No typeCode exists with codeValue: " + codeValue);
    }
}
