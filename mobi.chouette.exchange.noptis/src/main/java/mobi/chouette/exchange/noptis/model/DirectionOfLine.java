package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.DirectionCode;

public interface DirectionOfLine extends GidPeriodState {
    long getId();

    short getIsFromDataSourceId();

    DirectionCode getDirectionCode();

    String getName();

    long getIsOnLineId();

    String getDescriptionNote();
}
