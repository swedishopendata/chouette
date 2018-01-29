package mobi.chouette.exchange.noptis.model;

public interface Contractor extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    int getNumber();

    long getIsPromotedByTransportAuthorityId();

    String getCode();

    String getName();
}
