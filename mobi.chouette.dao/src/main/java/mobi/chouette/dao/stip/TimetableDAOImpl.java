package mobi.chouette.dao.stip;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.stip.TimedJourneyPattern;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Log4j
@SuppressWarnings("unchecked")
@Stateless(name="StipTimetableDAO")
public class TimetableDAOImpl extends GenericQueryDAOImpl implements TimetableDAO {

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT vt, v FROM VehicleJourneyTemplate vt, VehicleJourney v " +
                "WHERE VehicleJourneyTemplateEntity vt ON vt.isWorkedOnTimedJourneyPatternId = t.id " +
                "AND vt.isFromDataSourceId = :dataSourceId " +
                "AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid")
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }

    @Override
    public List<TimedJourneyPattern> findTimedJourneyPatternsForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT t FROM TimedJourneyPattern t, VehicleJourneyTemplate vt " +
                "WHERE vt.isWorkedOnTimedJourneyPatternId = t.id " +
                "AND vt.isFromDataSourceId = :dataSourceId " +
                "AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid")
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }

}
