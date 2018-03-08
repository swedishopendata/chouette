package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.DirectionOfLine;

import java.util.List;

public interface DirectionOfLineDAO extends GenericDAO<DirectionOfLine> {

    List<DirectionOfLine> findByDataSourceId(short dataSourceId);
}
