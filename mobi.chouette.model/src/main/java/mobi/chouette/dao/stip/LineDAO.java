package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.Line;

import java.util.List;

public interface LineDAO extends GenericDAO<Line> {

    List<Line> findByDataSourceId(short dataSourceId);
}
