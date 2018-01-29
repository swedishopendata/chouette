package mobi.chouette.exchange.noptis.model.type;

public enum DepartureType {
    NO_STOP(0),
    STOP_NO_BOARDING(1),
    STOP_BOARDING_IF_NECESSARY(2),
    STOP_BOARDING_ALWAYS(3),
    FLEXIBLE_STOP(5);

    private int typeValue;

    DepartureType(int typeValue) {
        this.typeValue = typeValue;
    }

    public int getTypeValue() {
        return typeValue;
    }

    public static DepartureType getDepartureType(int typeValue) {
        for (DepartureType departureType : DepartureType.values()) {
            if (departureType.getTypeValue() == typeValue) {
                return departureType;
            }
        }
        throw new IllegalArgumentException("No departureType exists with typeValue: " + typeValue);
    }
}
