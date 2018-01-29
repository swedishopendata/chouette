package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransferModeCode;

import java.time.LocalDate;

public interface ConnectionLink {
    long getId();

    short getIsFromDataSourceId();

    long getStartsAtJourneyPatternPointGid();

    long getEndsAtJourneyPatternPointGid();

    TransferModeCode getTransferModeCode();

    Integer getDistanceMeters();

    int getDefaultDurationSeconds();

    LocalDate getExistsFromDate();

    LocalDate getExistsUpToDate();
}
