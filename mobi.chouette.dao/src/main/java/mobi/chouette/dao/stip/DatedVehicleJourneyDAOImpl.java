package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.DatedVehicleJourney;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class DatedVehicleJourneyDAOImpl extends GenericDAOImpl<DatedVehicleJourney> implements DatedVehicleJourneyDAO {

    public DatedVehicleJourneyDAOImpl() {
        super(DatedVehicleJourney.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<LocalDate> findDatedVehicleJourneyDates(long vehicleJourneyId) {
        return em.createQuery("SELECT d.operatingDayDate FROM DatedVehicleJourney d " +
                "WHERE d.isBasedOnVehicleJourneyId = :vehicleJourneyId " +
                "ORDER BY d.operatingDayDate", LocalDate.class)
                .setParameter("vehicleJourneyId", vehicleJourneyId)
                .getResultList();
    }
}
