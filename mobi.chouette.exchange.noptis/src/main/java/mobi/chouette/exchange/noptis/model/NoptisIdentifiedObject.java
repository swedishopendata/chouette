package mobi.chouette.exchange.noptis.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
@EqualsAndHashCode(of = { "gid" }, callSuper = false)
@ToString(callSuper = true)
public abstract class NoptisIdentifiedObject extends NoptisDataSourceObject {

    @Getter
    @Setter
    @Column(name = "Gid", nullable = false)
    protected long gid;

}
