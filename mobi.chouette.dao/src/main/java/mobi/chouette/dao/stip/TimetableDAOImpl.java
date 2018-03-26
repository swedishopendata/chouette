package mobi.chouette.dao.stip;

import lombok.extern.log4j.Log4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Log4j
@SuppressWarnings("unchecked")
@Stateless(name = "StipTimetableDAO")
public class TimetableDAOImpl extends GenericQueryDAOImpl implements TimetableDAO {

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT vt, v FROM VehicleJourneyTemplate vt, VehicleJourney v " +
                "WHERE v.isDescribedByVehicleJourneyTemplateId = vt.id " +
                "AND vt.isFromDataSourceId = :dataSourceId " +
                "AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid")
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }

    @Override
    public List findCallsForTimedJourneyPattern(long timedJourneyPatternId) {
        return em.createQuery("SELECT c, pj FROM CallOnTimedJourneyPattern c, PointInJourneyPattern pj " +
                "WHERE c.isOnPointInJourneyPatternId = pj.id " +
                "AND c.isOnTimedJourneyPatternId = :timedJourneyPatternId ")
                .setParameter("timedJourneyPatternId", timedJourneyPatternId)
                .getResultList();
    }

    @Override
    public List<LocalDate> findDatesForVehicleJourney(long vehicleJourneyId) {
        return em.createQuery("SELECT d.operatingDayDate FROM DatedVehicleJourney d " +
                "WHERE d.isBasedOnVehicleJourneyId = :vehicleJourneyId " +
                "ORDER BY d.operatingDayDate", LocalDate.class)
                .setParameter("vehicleJourneyId", vehicleJourneyId)
                .getResultList();
    }

}
