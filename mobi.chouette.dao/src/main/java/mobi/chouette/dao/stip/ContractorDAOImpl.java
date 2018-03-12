package mobi.chouette.dao.stip;

import mobi.chouette.model.stip.Contractor;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class ContractorDAOImpl extends GenericDAOImpl<Contractor> implements ContractorDAO {

    public ContractorDAOImpl() {
        super(Contractor.class);
    }

    @PersistenceContext(unitName = "stip")
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public Contractor findByGid(long gid) {
        return em.createQuery("SELECT c FROM Contractor c WHERE c.gid = :gid", Contractor.class)
                .setParameter("gid", gid)
                .getSingleResult();
    }

    @Override
    public List<Contractor> findByDataSourceId(short dataSourceId) {
        return em.createQuery("SELECT c FROM Contractor c WHERE c.isFromDataSourceId = :dataSourceId", Contractor.class)
                .setParameter("dataSourceId", dataSourceId)
                .getResultList();
    }

}
