package mobi.chouette.dao.stip;

import lombok.extern.log4j.Log4j;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Log4j
@Stateless(name="StipTimetableDAO")
public class TimetableDAOImpl extends GenericQueryDAOImpl implements TimetableDAO {

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @SuppressWarnings("unchecked")
    public List findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT vt, v FROM VehicleJourneyTemplate vt, VehicleJourney v " +
                "WHERE v.isDescribedByVehicleJourneyTemplateId = vt.id " +
                "AND vt.isFromDataSourceId = :dataSourceId " +
                "AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid")
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }

}
