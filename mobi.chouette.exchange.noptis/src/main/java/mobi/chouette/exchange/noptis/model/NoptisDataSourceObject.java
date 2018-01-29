package mobi.chouette.exchange.noptis.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;

@MappedSuperclass
@Access(AccessType.FIELD)
@ToString
public abstract class NoptisDataSourceObject implements Serializable {

    private static final long serialVersionUID = 8689120595445609109L;

    @Getter
    @Setter
    @Transient
    private boolean detached = false;

    public abstract Long getId();

    public abstract void setId(Long id);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        NoptisDataSourceObject other = (NoptisDataSourceObject) o;
        if (getId() == null) {
            return (other.getId() == null);
        }
        if (getId().equals(other.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return 0;
        }
        return getId().hashCode();
    }
}
