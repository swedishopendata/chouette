package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.StopPoint;

import java.util.List;

public interface StopPointDAO extends GenericDAO<StopPoint> {

    StopPoint findByGid(long gid);

    List<StopPoint> findByDataSourceId(short dataSourceId);

    StopPoint findByJourneyPatternPointGid(long journeyPatternPointGid);
}
