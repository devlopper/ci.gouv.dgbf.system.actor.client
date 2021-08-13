package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Profile extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ProfileType type;
	private String profileIdentifier;
	private Collection<String> privilegesAsStrings;
	private Collection<String> creatablePrivilegesIdentifiers;
	private Collection<String> deletablePrivilegesIdentifiers;
	private Byte orderNumber;
	private Boolean requestable;
	
	public static final String FIELD_PRIVILEGES_AS_STRINGS = "privilegesAsStrings";
}