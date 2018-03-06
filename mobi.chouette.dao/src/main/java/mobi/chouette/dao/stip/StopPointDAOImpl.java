package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.StopPoint;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name="StipStopPointDAO")
public class StopPointDAOImpl extends GenericDAOImpl<StopPoint> implements StopPointDAO {

    public StopPointDAOImpl() {
        super(StopPoint.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public StopPoint findByGid(long gid) {
        return em.createQuery("SELECT s FROM StopPoint s WHERE s.gid = :gid", StopPoint.class)
                .setParameter("gid", gid)
                .getSingleResult();
    }

    @Override
    public List<StopPoint> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT s FROM StopPoint s WHERE s.isFromDataSourceId = :dataSourceId", StopPoint.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

}
