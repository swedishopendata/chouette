package mobi.chouette.model.stip;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
@ToString(callSuper = true)
public abstract class NoptisDataSourceObject extends NoptisObject {

    @Getter
    @Setter
    @Column(name = "IsFromDataSourceId")
    protected short isFromDataSourceId;

}
