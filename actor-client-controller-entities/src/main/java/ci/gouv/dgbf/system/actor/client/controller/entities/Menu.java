package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Menu extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String moduleAsString;
	private String moduleCodeName;
	private String serviceAsString;
	private String serviceCodeName;
	private String uniformResourceIdentifier;
	private Boolean defined;
	private String status;
	private String profilesAsString;
	
	public static final String FIELD_MODULE_AS_STRING = "moduleAsString";
	public static final String FIELD_MODULE_CODE_NAME = "moduleCodeName";
	public static final String FIELD_SERVICE_AS_STRING = "serviceAsString";
	public static final String FIELD_SERVICE_CODE_NAME = "serviceCodeName";
	public static final String FIELD_UNIFORM_RESOURCE_IDENTIFIER = "uniformResourceIdentifier";
	public static final String FIELD_DEFINED = "defined";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_PROFILES_AS_STRING = "profilesAsString";
}