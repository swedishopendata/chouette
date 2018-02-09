package mobi.chouette.exchange.noptis;

import lombok.extern.log4j.Log4j;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@Log4j
public class NoptisSqlQueryUtils {

    private EntityManagerFactory entityManagerFactory;

    public NoptisSqlQueryUtils(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public List query(String sqlQuery, Object... parameters) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createQuery(sqlQuery);
            setParameters(query, parameters);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public <T> List<T> query(String sqlQuery, Class<T> resultClass, Object... parameters) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            TypedQuery<T> query = entityManager.createQuery(sqlQuery, resultClass);
            setParameters(query, parameters);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Object queryOneRow(String sqlQuery, Object... parameters) {
        List resultList = query(sqlQuery, parameters);
        if (resultList.isEmpty()) {
            return null;
        }
        if (resultList.size() > 1) {
            log.warn("queryOneRow did not find one row but " + resultList.size() + " rows. Returning first one.");
        }
        return resultList.get(0);
    }

    public <T> T queryOneRow(String sqlQuery, Class<T> resultClass, Object... parameters) {
        List<T> resultList = query(sqlQuery, resultClass, parameters);
        if (resultList.isEmpty()) {
            return null;
        }
        if (resultList.size() > 1) {
            log.warn("queryOneRow did not find one row but " + resultList.size() + " rows. Returning first one.");
        }
        return resultList.get(0);
    }

    private void setParameters(Query query, Object... parameters) {
        if (parameters == null) {
            return;
        }
        int parameterIndex = 1;
        for (Object parameter : parameters) {
            query.setParameter(parameterIndex++, parameter);
        }
    }

}
