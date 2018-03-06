package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.StopArea;

import java.util.List;

public interface StopAreaDAO extends GenericDAO<StopArea> {

    StopArea findByGid(long gid);

    List<StopArea> findByDataSourceId(short dataSourceId);
}
