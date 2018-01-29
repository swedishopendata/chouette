package mobi.chouette.exchange.noptis.model.type;

public enum DirectionCode {
    ODD('1'),
    EVEN('2');

    private final char codeValue;

    DirectionCode(char codeValue) {
        this.codeValue = codeValue;
    }

    public char getCodeValue() {
        return codeValue;
    }

    public static DirectionCode getDirectionCode(char codeValue) {
        for (DirectionCode directionCode : DirectionCode.values()) {
            if (directionCode.getCodeValue() == codeValue) {
                return directionCode;
            }
        }
        throw new IllegalArgumentException("No directionCode exists with codeValue: " + codeValue);
    }
}
