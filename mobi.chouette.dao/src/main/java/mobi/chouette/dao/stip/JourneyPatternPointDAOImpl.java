package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.JourneyPatternPoint;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class JourneyPatternPointDAOImpl extends GenericDAOImpl<JourneyPatternPoint> implements JourneyPatternPointDAO {

    public JourneyPatternPointDAOImpl() {
        super(JourneyPatternPoint.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public JourneyPatternPoint findByGid(long gid) {
        return em.createQuery("SELECT j FROM JourneyPatternPoint j WHERE j.gid = :gid", JourneyPatternPoint.class)
                .setParameter("gid", gid)
                .getSingleResult();
    }

    @Override
    public List<JourneyPatternPoint> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT j FROM JourneyPatternPoint j WHERE j.isFromDataSourceId = :dataSourceId", JourneyPatternPoint.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

}
