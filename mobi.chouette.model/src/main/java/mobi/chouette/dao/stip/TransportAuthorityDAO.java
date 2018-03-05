package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.TransportAuthority;

import java.util.List;

public interface TransportAuthorityDAO extends GenericDAO<TransportAuthority> {

    TransportAuthority findByGid(long gid);

    List<TransportAuthority> findByDataSourceId(short dataSourceId);
}
