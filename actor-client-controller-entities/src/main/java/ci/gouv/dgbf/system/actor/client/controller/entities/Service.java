package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Service extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private String moduleCodeName;
	private String moduleAsString;
	private Boolean defined;
	private Boolean secured;
	private String status;
	
	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_MODULE_CODE_NAME = "moduleCodeName";
	public static final String FIELD_MODULE_AS_STRING = "moduleAsString";
	public static final String FIELD_DEFINED = "defined";
	public static final String FIELD_SECURED = "secured";
	public static final String FIELD_STATUS = "status";
}