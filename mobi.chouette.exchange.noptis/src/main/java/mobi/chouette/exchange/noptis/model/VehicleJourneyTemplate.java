package mobi.chouette.exchange.noptis.model;

import mobi.chouette.exchange.noptis.model.type.TransportModeCode;

public interface VehicleJourneyTemplate {
    long getId();

    short getIsFromDataSourceId();

    long getIsWorkedOnTimedJourneyPatternId();

    long getUsesNamedJourneyPatternGid();

    Long getIsWorkedOnDirectionOfLineGid();

    TransportModeCode getTransportModeCode();

    Long getContractorGid();

    long getIsWorkedAccordingToServiceCalendarId();

    boolean isExposedInPrintMedia();

    long getUsesServiceRequirementPatternId();
}
