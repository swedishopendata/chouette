package mobi.chouette.exchange.validator.parameters;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import lombok.Data;
import mobi.chouette.model.ConnectionLink;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ConnectionLinkParameters {

	@XmlTransient
	public static String[] fields = { "Objectid", "Name", "LinkDistance", "DefaultDuration"} ;
	
	static {
		ValidationParametersUtil.addFieldList(ConnectionLink.class.getSimpleName(), Arrays.asList(fields));
	}

	@XmlElement(name = "objectid")
	private FieldParameters objectid;

	@XmlElement(name = "name")
	private FieldParameters name;

	@XmlElement(name = "link_distance")
	private FieldParameters linkDistance;

	@XmlElement(name = "default_duration")
	private FieldParameters defaultDuration;

}