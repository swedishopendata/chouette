package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;
import mobi.chouette.model.stip.VehicleJourneyTemplate;

import java.util.List;

public interface VehicleJourneyTemplateDAO extends GenericDAO<VehicleJourneyTemplate> {

    public List<Object[]> findVehicleJourneyAndTemplatesForDirectionOfLine(short dataSourceId, long directionOfLineGid);
}
