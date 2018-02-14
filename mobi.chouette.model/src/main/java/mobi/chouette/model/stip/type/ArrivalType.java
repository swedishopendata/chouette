package mobi.chouette.model.stip.type;

public enum ArrivalType {
    NO_STOP(0),
    STOP_NO_ALIGHTING(1),
    STOP_ALIGHTING_IF_NECESSARY(2),
    STOP_ALIGHTING_ALWAYS(3),
    FLEXIBLE_STOP(5);

    private int typeValue;

    ArrivalType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public static ArrivalType getArrivalType(int typeValue) {
        for (ArrivalType arrivalType : ArrivalType.values()) {
            if (arrivalType.getTypeValue() == typeValue) {
                return arrivalType;
            }
        }
        throw new IllegalArgumentException("No arrivalType exists with typeValue: " + typeValue);
    }
}
