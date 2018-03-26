package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.ConnectionLink;
import mobi.chouette.model.stip.type.TransferModeCode;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name="StipConnectionLinkDAO")
public class ConnectionLinkDAOImpl extends GenericDAOImpl<ConnectionLink> implements ConnectionLinkDAO {

    public ConnectionLinkDAOImpl() {
        super(ConnectionLink.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ConnectionLink> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT cl FROM ConnectionLink cl " +
                "WHERE cl.isFromDataSourceId = :dataSourceId " +
                "AND cl.transferModeCode = :transferModeCode", ConnectionLink.class)
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("transferModeCode", TransferModeCode.WALK)
                .getResultList();
    }

}
