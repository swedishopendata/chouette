package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.DirectionOfLine;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class DirectionOfLineDAOImpl extends GenericDAOImpl<DirectionOfLine> implements DirectionOfLineDAO {

    public DirectionOfLineDAOImpl() {
        super(DirectionOfLine.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<DirectionOfLine> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT d FROM DirectionOfLine d WHERE d.isFromDataSourceId = :dataSourceId " +
                "ORDER BY d.gid, d.directionCode", DirectionOfLine.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

}
