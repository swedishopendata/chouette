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
    public List<Object[]> findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid) {
        return em.createQuery("SELECT vt, v FROM VehicleJourneyTemplateEntity vt " +
                "INNER JOIN VehicleJourneyEntity v ON v.isDescribedByVehicleJourneyTemplateId = vt.id " +
                "WHERE vt.isFromDataSourceId = :dataSourceId AND vt.isWorkedOnDirectionOfLineGid = :directionOfLineGid", Object[].class)
                .setParameter("dataSourceId", dataSourceId)
                .setParameter("directionOfLineGid", directionOfLineGid)
                .getResultList();
    }
}

