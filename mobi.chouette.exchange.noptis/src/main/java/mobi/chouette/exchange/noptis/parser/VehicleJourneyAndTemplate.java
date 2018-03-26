package mobi.chouette.exchange.noptis.parser;

import mobi.chouette.model.stip.VehicleJourney;
import mobi.chouette.model.stip.VehicleJourneyTemplate;

public class VehicleJourneyAndTemplate {

    private final VehicleJourneyTemplate vehicleJourneyTemplate;
    private final VehicleJourney vehicleJourney;

    public VehicleJourneyAndTemplate(VehicleJourneyTemplate vehicleJourneyTemplate, VehicleJourney vehicleJourney) {
        this.vehicleJourneyTemplate = vehicleJourneyTemplate;
        this.vehicleJourney = vehicleJourney;
    }

    public VehicleJourney getVehicleJourney() {
        return vehicleJourney;
    }

    public VehicleJourneyTemplate getVehicleJourneyTemplate() {
        return vehicleJourneyTemplate;
    }
}
