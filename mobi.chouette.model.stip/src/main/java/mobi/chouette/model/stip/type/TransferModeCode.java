package mobi.chouette.model.stip.type;

public enum TransferModeCode {
    WALK("WALK"),
    TAXI("TAXI");

    private String codeValue;

    TransferModeCode(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public static TransferModeCode getTransferModeCode(String codeValue) {
        for (TransferModeCode transferModeCode : TransferModeCode.values()) {
            if (transferModeCode.getCodeValue().equals(codeValue)) {
                return transferModeCode;
            }
        }
        throw new IllegalArgumentException("No transferModeCode exists with codeValue: " + codeValue);
    }
}
