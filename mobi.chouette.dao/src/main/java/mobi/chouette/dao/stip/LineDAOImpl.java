package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.StopArea;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class LineDAOImpl extends GenericDAOImpl<Line> implements LineDAO {

	public LineDAOImpl() {
		super(Line.class);
	}

	@PersistenceContext(unitName = "stip")
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

    @Override
    public Line findByGid(long gid) {
        return em.createQuery("SELECT l FROM Line l WHERE l.gid = :gid", Line.class)
                .setParameter("gid", gid)
                .getSingleResult();
    }

    @Override
    public List<Line> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT l FROM Line l WHERE l.isFromDataSourceId = :dataSourceId", Line.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

    @Override
    public List<Long> findGidsByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT l.gid FROM Line l WHERE l.isFromDataSourceId = :dataSourceId", Long.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

}
