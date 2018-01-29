package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.ArrivalType;
import mobi.chouette.exchange.noptis.model.type.DepartureType;

public interface PointInJourneyPattern {
    long getId();

    short getIsFromDataSourceId();

    long getIsInJourneyPatternId();

    long getIsJourneyPatternPointGid();

    int getSequenceNumber();

    DepartureType getDepartureType();

    ArrivalType getArrivalType();
}
