package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericDAO;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class GenericDAOImpl<T> implements GenericDAO<T> {

	EntityManager em;

	protected Class<T> type;

	GenericDAOImpl(Class<T> type) {
		this.type = type;
	}

	@Override
	public T find(final Object id) {
		return em.find(type, id);
	}

	@Override
	public List<T> find(final String hql, final List<Object> values) {
		List<T> result = null;
		if (values.isEmpty()) {
			TypedQuery<T> query = em.createQuery(hql, type);
			result = query.getResultList();
		} else {
			TypedQuery<T> query = em.createQuery(hql, type);
			int pos = 0;
			for (Object value : values) {
				query.setParameter(pos++, value);
			}
			result = query.getResultList();
		}
		return result;
	}

	@Override
	public List<T> findAll(final Collection<Long> ids) {
		if (ids == null || ids.size() == 0){
			return Collections.emptyList();
		}
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		Root<T> root = criteria.from(type);
		Predicate predicate = builder.in(root.get("id")).value(ids);
		criteria.where(predicate);
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

	@Override
	public List<T> findAll() {
		List<T> result = null;
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(type);
		criteria.from(type);
		TypedQuery<T> query = em.createQuery(criteria);
		result = query.getResultList();
		return result;
	}

	@Override
	public T findByObjectId(final String objectId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<T> findByObjectId(final Collection<String> objectIds) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void create(T entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T update(T entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void delete(final T entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void detach(final T entity) {
		em.detach(entity);
	}

	@Override
	public int deleteAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int truncate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void evictAll() {
		EntityManagerFactory factory = em.getEntityManagerFactory();
		Cache cache = factory.getCache();
		cache.evictAll();
	}

	@Override
	public void flush() {
		em.flush();
	}

	@Override
	public void clear() {
		em.clear();
	}

	@Override
	public void detach(Collection<?> list) {
		for (Object object : list) {
			em.detach(object);
		}
	}


}
