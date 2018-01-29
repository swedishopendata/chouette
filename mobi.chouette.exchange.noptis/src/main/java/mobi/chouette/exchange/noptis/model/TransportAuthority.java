package mobi.chouette.exchange.noptis.model;

import java.time.LocalDate;

public interface TransportAuthority extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    int getNumber();

    String getCode();

    String getName();

    String getFormalName();

    LocalDate getTimetableReleasesForPublicUseUptoDate();
}
