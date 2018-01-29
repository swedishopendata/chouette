package mobi.chouette.exchange.noptis.model.type;

public enum PointOnLinkTypeCode {
    ACTION("ACTION"),
    MAPPING("MAPPING"),
    TIMING("TIMING");

    private String codeValue;

    PointOnLinkTypeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public static PointOnLinkTypeCode getPointOnLinkTypeCode(String codeValue) {
        for (PointOnLinkTypeCode pointOnLinkTypeCode : PointOnLinkTypeCode.values()) {
            if (pointOnLinkTypeCode.getCodeValue().equals(codeValue)) {
                return pointOnLinkTypeCode;
            }
        }
        throw new IllegalArgumentException("No PointOnLinkTypeCode exists with codeValue: " + codeValue);
    }
}
