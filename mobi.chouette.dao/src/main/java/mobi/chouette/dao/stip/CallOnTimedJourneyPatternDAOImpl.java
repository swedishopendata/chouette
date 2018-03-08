package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.CallOnTimedJourneyPattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class CallOnTimedJourneyPatternDAOImpl extends GenericDAOImpl<CallOnTimedJourneyPattern> implements CallOnTimedJourneyPatternDAO {

    public CallOnTimedJourneyPatternDAOImpl() {
        super(CallOnTimedJourneyPattern.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Object[]> findCallsForTimedJourneyPattern(long timedJourneyPatternId) {
        return em.createQuery("SELECT c, pj FROM CallOnTimedJourneyPatternEntity c " +
                "INNER JOIN PointInJourneyPatternEntity pj ON c.isOnPointInJourneyPatternId = pj.id " +
                "WHERE c.isOnTimedJourneyPatternId = :timedJourneyPatternId ", Object[].class)
                .setParameter("timedJourneyPatternId", timedJourneyPatternId)
                .getResultList();
    }
}
