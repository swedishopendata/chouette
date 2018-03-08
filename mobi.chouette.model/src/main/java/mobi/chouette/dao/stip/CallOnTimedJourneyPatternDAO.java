package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.CallOnTimedJourneyPattern;

import java.util.List;

public interface CallOnTimedJourneyPatternDAO extends GenericDAO<CallOnTimedJourneyPattern> {

    public List<Object[]> findCallsForTimedJourneyPattern(long timedJourneyPatternId);
}
