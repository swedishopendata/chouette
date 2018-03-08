package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.StopArea;

import java.util.List;

public interface LineDAO extends GenericDAO<Line> {

    Line findByGid(long gid);

    List<Line> findByDataSourceId(short dataSourceId);

    List<Long> findIdsByDataSourceId(short dataSourceId);

    List<Long> findGidsByDataSourceId(short dataSourceId);

}
