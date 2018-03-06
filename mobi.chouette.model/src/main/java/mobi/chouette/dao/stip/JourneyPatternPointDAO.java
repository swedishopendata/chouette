package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.JourneyPatternPoint;

import java.util.List;

public interface JourneyPatternPointDAO extends GenericDAO<JourneyPatternPoint> {

    JourneyPatternPoint findByGid(long gid);

    List<JourneyPatternPoint> findByDataSourceId(short dataSourceId);
}
