package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericQueryDAO;

import java.time.LocalDate;
import java.util.List;

public interface TimetableDAO extends GenericQueryDAO {

    List findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid);

    List findCallsForTimedJourneyPattern(long timedJourneyPatternId);

    List<LocalDate> findDatesForVehicleJourney(long vehicleJourneyId);
}
