package mobi.chouette.exchange.noptis.model;

import java.time.LocalDate;

public interface GidPeriodState {
    long getGid();

    LocalDate getExistsFromDate();

    LocalDate getExistsUpToDate();
}
