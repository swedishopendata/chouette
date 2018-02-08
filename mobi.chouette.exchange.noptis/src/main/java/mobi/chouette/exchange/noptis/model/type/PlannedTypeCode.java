package mobi.chouette.exchange.noptis.model.type;

public enum PlannedTypeCode {
    NORMAL("NORMAL"),
    EXTRA("EXTRA");

    private String codeValue;

    PlannedTypeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public static PlannedTypeCode getPlannedTypeCode(String codeValue) {
        for (PlannedTypeCode plannedTypeCode : PlannedTypeCode.values()) {
            if (plannedTypeCode.getCodeValue().equals(codeValue)) {
                return plannedTypeCode;
            }
        }
        throw new IllegalArgumentException("No plannedTypeCode exists with codeValue: " + codeValue);
    }
}
