package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericQueryDAO;
import mobi.chouette.model.stip.TimedJourneyPattern;

import java.util.List;

public interface TimetableDAO extends GenericQueryDAO {

    List findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid);

    List<TimedJourneyPattern> findTimedJourneyPatternsForDirectionOfLine(short dataSourceId, long directionOfLineGid);

}
