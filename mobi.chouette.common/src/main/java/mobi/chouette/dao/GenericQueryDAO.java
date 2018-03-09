package mobi.chouette.dao;

import javax.ejb.Local;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

@Local
public interface GenericQueryDAO {
	void flush();
	void clear();
}
