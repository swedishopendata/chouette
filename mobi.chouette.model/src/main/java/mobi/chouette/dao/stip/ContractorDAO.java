package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.Contractor;

import java.util.List;

public interface ContractorDAO extends GenericDAO<Contractor> {

    Contractor findByGid(long gid);

    List<Contractor> findByDataSourceId(short dataSourceId);

}
