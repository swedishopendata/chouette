package mobi.chouette.exchange.noptis.importer;

import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.model.stip.Line;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class NoptisDaoReader {

    @EJB
    private LineDAO lineDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Set<Long> loadLineIds() {
        return lineDAO.findAll().stream()
                .map(Line::getId)
                .collect(Collectors.toSet());
    }

}
