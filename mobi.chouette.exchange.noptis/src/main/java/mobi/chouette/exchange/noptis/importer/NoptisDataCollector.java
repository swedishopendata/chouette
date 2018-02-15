package mobi.chouette.exchange.noptis.importer;

import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.model.stip.Line;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class NoptisDataCollector {

    @EJB
    protected LineDAO lineDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean collect(ImportableNoptisData collection, Line line) {
        boolean validLine = false;
        collection.setLine(line);

/*
        Network network = line.getNetwork();
        if(network != null) {
            collection.getNetworks().add(network);
            if(network.getCompany() != null) { // Authority
                collection.getCompanies().add(network.getCompany());
            }
        }
        if (line.getCompany() != null) { // Operator
            collection.getCompanies().add(line.getCompany());
        }
*/

        return false;
    }

}
