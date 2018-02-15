package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.TransportAuthority;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class TransportAuthorityDAOImpl extends GenericDAOImpl<TransportAuthority> implements TransportAuthorityDAO {

    public TransportAuthorityDAOImpl() {
        super(TransportAuthority.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public TransportAuthority findByGid(long gid) {
        return em.createQuery("SELECT t FROM TransportAuthority t WHERE t.gid = :gid", TransportAuthority.class)
                .setParameter("gid", gid)
                .getSingleResult();
    }

}
