package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.Line;
import mobi.chouette.model.stip.Line_;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

	public List<Line> findByDataSourceId(short dataSourceId) {
        List<Line> result;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Line> criteria = builder.createQuery(type);
        Root<Line> root = criteria.from(type);
        Predicate dataSourcePredicate = builder.equal(root.get(Line_.isFromDataSourceId), dataSourceId);
        criteria.where(dataSourcePredicate);
        criteria.orderBy(builder.asc(root.get(Line_.existsFromDate)));
        TypedQuery<Line> query = em.createQuery(criteria);
        result = query.getResultList();
        return result;
	}

}
