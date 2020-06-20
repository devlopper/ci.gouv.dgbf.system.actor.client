package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyCheck;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputPass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Actor extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastNames;
	private String names;
	private String electronicMailAddress;	
	private String username;
	@Input @InputPass
	private String password;
	@Input @InputPass
	private String passwordConfirmation;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceMany @InputChoiceManyCheck
	private Collection<Function> functions;
	private Collection<Privilege> privileges;
	private Collection<Scope> scopes;
	
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAMES = "lastNames";
	public static final String FIELD_NAMES = "names";
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS = "electronicMailAddress";
	public static final String FIELD_USER_NAME = "username";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_PASSWORD_CONFIRMATION = "passwordConfirmation";
	public static final String FIELD_FUNCTIONS = "functions";
}