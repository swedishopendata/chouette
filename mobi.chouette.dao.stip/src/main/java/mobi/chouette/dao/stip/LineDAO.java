package mobi.chouette.dao.stip;

import lombok.extern.log4j.Log4j;
import mobi.chouette.model.stip.Line;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@Stateless
@Log4j
public class LineDAO extends GenericDAOImpl<Line> {

    public LineDAO() {
        super(Line.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public List<Line> findByDataSourceId(short dataSourceId) {
/*
        List<Line> result;
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Line> criteria = builder.createQuery(type);
        Root<Job> root = criteria.from(type);
        Predicate dataSourcePredicate = builder.equal(root.get(Job_.isFromDataSourceId), dataSourceId);
        criteria.where(dataSourcePredicate);
        criteria.orderBy(builder.asc(root.get(Line_.existsFromDate)));
        TypedQuery<Line> query = em.createQuery(criteria);
        result = query.getResultList();
        return result;
*/
        return find("SELECT l FROM LineEntity l WHERE l.isFromDataSourceId = ?1", Collections.singletonList(dataSourceId));
    }

}
