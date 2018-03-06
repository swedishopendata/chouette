package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.StopArea;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name="StipStopAreaDAO")
public class StopAreaDAOImpl extends GenericDAOImpl<StopArea> implements StopAreaDAO {

    public StopAreaDAOImpl() {
        super(StopArea.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public StopArea findByGid(long gid) {
        return em.createQuery("SELECT s FROM StopArea s WHERE s.gid = :gid", StopArea.class)
                .setParameter("gid", gid)
                .getSingleResult();
    }

    @Override
    public List<StopArea> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT s FROM StopArea s WHERE s.isFromDataSourceId = :dataSourceId", StopArea.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

}
