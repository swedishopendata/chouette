package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.DatedVehicleJourney;

import java.time.LocalDate;
import java.util.List;

public interface DatedVehicleJourneyDAO extends GenericDAO<DatedVehicleJourney> {

    public List<LocalDate> findDatedVehicleJourneyDates(long vehicleJourneyId);
}
