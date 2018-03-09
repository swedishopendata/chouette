package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.VehicleJourneyTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class VehicleJourneyTemplateDAOImpl extends GenericDAOImpl<VehicleJourneyTemplate> implements VehicleJourneyTemplateDAO {

    public VehicleJourneyTemplateDAOImpl() {
        super(VehicleJourneyTemplate.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT vt, v FROM VehicleJourneyTemplate vt " +
                "INNER JOIN VehicleJourney v ON v.isDescribedByVehicleJourneyTemplateId = vt.id " +
                "WHERE vt.isFromDataSourceId = :dataSourceId AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid")
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }
}

