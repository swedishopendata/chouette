package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.PointInJourneyPattern;

import java.util.List;

public interface PointInJourneyPatternDAO extends GenericDAO<PointInJourneyPattern> {

    List<PointInJourneyPattern> findByJourneyPatternId(long journeyPatternId);
}
