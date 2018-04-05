package mobi.chouette.exchange.noptis.importer;

import mobi.chouette.dao.stip.DataSourceDAO;
import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.exchange.noptis.importer.util.NoptisImporterUtils;
import mobi.chouette.model.stip.DataSource;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class NoptisDaoReader {

    @EJB
    private DataSourceDAO dataSourceDAO;

    @EJB
    private LineDAO lineDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Set<Long> loadLineIds(short dataSourceId) {
        List<Long> lineIds = lineDAO.findIdsByDataSourceId(dataSourceId);
        return new HashSet<>(lineIds);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public DataSource loadDataSource(String objectIdPrefix) {
        return dataSourceDAO.findByShortName(NoptisImporterUtils.getDataSourceShortName(objectIdPrefix));
    }

}
