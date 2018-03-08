package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.TimedJourneyPattern;

import java.util.List;

public interface TimedJourneyPatternDAO extends GenericDAO<TimedJourneyPattern> {

    public List<TimedJourneyPattern> findTimedJourneyPatternsForDirectionOfLine(short dataSourceId, long directionOfLineGid);
}
