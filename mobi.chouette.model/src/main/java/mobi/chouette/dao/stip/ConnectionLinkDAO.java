package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.ConnectionLink;

import java.util.List;

public interface ConnectionLinkDAO extends GenericDAO<ConnectionLink> {

    List<ConnectionLink> findByDataSourceId(short dataSourceId);
}
