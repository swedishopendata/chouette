package mobi.chouette.exchange.noptis.importer;

import mobi.chouette.dao.stip.LineDAO;

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
    private LineDAO lineDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Set<Long> loadLineIds(short dataSourceId) {
        List<Long> lineIds = lineDAO.findIdsByDataSourceId(dataSourceId);
        return new HashSet<>(lineIds);
    }

}
