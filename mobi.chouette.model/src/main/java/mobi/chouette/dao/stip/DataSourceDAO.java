package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.DataSource;

public interface DataSourceDAO extends GenericDAO<DataSource> {

    DataSource findByShortName(String shortName);

}
