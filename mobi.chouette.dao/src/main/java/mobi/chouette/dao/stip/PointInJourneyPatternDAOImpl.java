package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.PointInJourneyPattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class PointInJourneyPatternDAOImpl extends GenericDAOImpl<PointInJourneyPattern> implements PointInJourneyPatternDAO {

    public PointInJourneyPatternDAOImpl() {
        super(PointInJourneyPattern.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<PointInJourneyPattern> findByJourneyPatternId(long journeyPatternId) {
        return em.createQuery("SELECT pjp FROM PointInJourneyPattern pjp " +
                "WHERE pjp.isInJourneyPatternId = :journeyPatternId", PointInJourneyPattern.class)
                .setParameter("journeyPatternId", journeyPatternId)
                .getResultList();
    }
}
