package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.DataSource;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DataSourceDAOImpl extends GenericDAOImpl<DataSource> implements DataSourceDAO {

    public DataSourceDAOImpl() {
        super(DataSource.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public DataSource findByShortName(String shortName) {
        return em.createQuery("SELECT ds FROM DataSource ds WHERE ds.shortName = :shortName", DataSource.class)
                .setParameter("shortName", shortName)
                .getSingleResult();
    }
}
