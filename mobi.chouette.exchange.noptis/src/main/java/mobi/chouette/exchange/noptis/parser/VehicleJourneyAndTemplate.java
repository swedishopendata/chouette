package mobi.chouette.exchange.noptis.parser;

import mobi.chouette.model.stip.VehicleJourney;
import mobi.chouette.model.stip.VehicleJourneyTemplate;

/**
 * This is a holder class containing VehicleJourney and VehicleJourneyTemplate.
 * It is returned from TimetableDao.
 *
 * @author Dick Zetterberg (dick@transitor.se)
 * @version 2017-10-31, 16:27
 */
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
