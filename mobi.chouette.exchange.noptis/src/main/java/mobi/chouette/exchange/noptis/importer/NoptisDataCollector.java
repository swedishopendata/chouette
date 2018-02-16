package mobi.chouette.exchange.noptis.importer;

import mobi.chouette.dao.stip.LineDAO;
import mobi.chouette.dao.stip.TransportAuthorityDAO;
import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.TransportAuthority;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class NoptisDataCollector {

    @EJB
    protected LineDAO lineDAO;

    @EJB
    protected TransportAuthorityDAO transportAuthorityDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean collect(ImportableNoptisData collection, Line line, String objectIdPrefix) {
        boolean validLine = false;
        collection.setLine(line);

        Long transportAuthorityId = line.getIsDefinedByTransportAuthorityId();
        TransportAuthority transportAuthority = transportAuthorityDAO.find(transportAuthorityId);
        //String transportAuthorityObjectId = NoptisConverter.composeObjectId(objectIdPrefix, TransportAuthority.TRANSPORTAUTHORITY_KEY, String.valueOf(transportAuthority.getGid()));
        String transportAuthorityObjectId = String.valueOf(transportAuthorityId);
        collection.getSharedTransportAuthorities().put(transportAuthorityObjectId, transportAuthority);

        validLine = true;
        return validLine;
    }

}
