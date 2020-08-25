package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyCheck;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputPass;
import org.cyk.utility.__kernel__.string.StringHelper;

import ci.gouv.dgbf.system.actor.server.persistence.entities.Scope;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Actor extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Identity identity;
	private String creationDateAsString;
	@NotNull private String firstName;
	@NotNull private String lastNames;
	private String names;
	@NotNull private String electronicMailAddress;	
	private String registrationNumber;
	private String postalBoxAddress;
	@NotNull private String mobilePhoneNumber;
	private String officePhoneNumber;
	private String officePhoneExtension;
	private String username;
	@Input @InputPass @NotNull private String password;
	@Input @InputPass @NotNull private String passwordConfirmation;
	private Byte notation;
	private String color;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete private AdministrativeUnit administrativeUnit;	
	private String administrativeFunction;
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneRadio private Civility civility;
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneRadio private IdentityGroup group;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceMany @InputChoiceManyCheck
	private Collection<Function> functions;
	private Collection<String> functionsCodes;
	private Collection<Privilege> privileges;
	private Collection<Privilege> visibleModules;
	private Collection<Scope> scopes;
	private Collection<Scope> visibleSections;
	
	public String getNames() {
		if(names == null) {
			if(StringHelper.isNotBlank(firstName))
				names = firstName;
			if(StringHelper.isNotBlank(lastNames))
				if(StringHelper.isBlank(names))
					names = lastNames;
				else
					names += " "+lastNames;
			if(names == null)
				names = identity == null ? null : identity.getNames();
			if(names == null)
				names = ConstantEmpty.STRING;
		}
		return names;
	}
	
	@Override
	public String toString() {
		return code+" - "+getNames();
	}
	
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAMES = "lastNames";
	public static final String FIELD_NAMES = "names";
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS = "electronicMailAddress";
	public static final String FIELD_REGISTRATION_NUMBER = "registrationNumber";
	public static final String FIELD_POSTAL_BOX_ADDRESS = "postalBoxAddress";
	public static final String FIELD_MOBILE_PHONE_NUMBER = "mobilePhoneNumber";
	public static final String FIELD_OFFICE_PHONE_NUMBER = "officePhoneNumber";
	public static final String FIELD_OFFICE_PHONE_EXTENSION = "officePhoneExtension";
	public static final String FIELD_ADMINISTRATIVE_UNIT = "administrativeUnit";
	public static final String FIELD_ADMINISTRATIVE_UNIT_AS_STRING = "administrativeUnitAsString";
	public static final String FIELD_ADMINISTRATIVE_FUNCTION = "administrativeFunction";
	public static final String FIELD_CIVILITY = "civility";
	public static final String FIELD_GROUP = "group";
	public static final String FIELD_USER_NAME = "username";
	public static final String FIELD_PASSWORD = "password";
	public static final String FIELD_PASSWORD_CONFIRMATION = "passwordConfirmation";
	public static final String FIELD_FUNCTIONS = "functions";
	public static final String FIELD_VISIBLE_MODULES = "visibleModules";
	public static final String FIELD_VISIBLE_SECTIONS = "visibleSections";
}