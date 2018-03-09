package mobi.chouette.dao.stip;

import mobi.chouette.dao.GenericQueryDAO;

import javax.persistence.EntityManager;

public abstract class GenericQueryDAOImpl implements GenericQueryDAO {

	protected EntityManager em;

	@Override
	public void flush() {
		em.flush();
	}

	@Override
	public void clear() {
		em.clear();
	}

}
