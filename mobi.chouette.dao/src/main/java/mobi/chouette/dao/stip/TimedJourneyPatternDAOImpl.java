package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.TimedJourneyPattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class TimedJourneyPatternDAOImpl extends GenericDAOImpl<TimedJourneyPattern> implements TimedJourneyPatternDAO {

    public TimedJourneyPatternDAOImpl() {
        super(TimedJourneyPattern.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<TimedJourneyPattern> findTimedJourneyPatternsForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT t FROM TimedJourneyPatternEntity t INNER JOIN VehicleJourneyTemplateEntity vt " +
                "ON vt.isWorkedOnTimedJourneyPatternId = t.id WHERE vt.isFromDataSourceId = :dataSourceId " +
                "AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid", TimedJourneyPattern.class)
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }
}
