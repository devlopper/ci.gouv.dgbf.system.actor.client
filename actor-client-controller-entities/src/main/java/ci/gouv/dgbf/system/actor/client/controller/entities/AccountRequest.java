package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputDate;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputTextarea;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class AccountRequest extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Identity identity;
	@NotNull private String firstName;
	@NotNull private String lastNames;
	private String names;
	@NotNull private String electronicMailAddress;
	private String creationDateAsString;
	private String submissionDateAsString;
	private String accessToken;
	
	private String registrationNumber;
	private String postalBoxAddress;
	@NotNull private String mobilePhoneNumber;
	private String officePhoneNumber;
	private String officePhoneExtension;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete private AdministrativeUnit administrativeUnit;
	private String administrativeUnitAsString;
	private String administrativeFunction;
	private String sectionAsString;
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneRadio private Civility civility;
	private String civilityAsString;
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneRadio private IdentityGroup group;
	private String groupAsString;
	private String actOfAppointmentReference;
	private String actOfAppointmentSignatory;
	@Input @InputDate private Date actOfAppointmentSignatureDate;
	private String actOfAppointmentSignatureDateAsString;
	private Long actOfAppointmentSignatureDateAsTimestamp;
	@Input @InputChoice @InputChoiceOneRadio @NotNull private String treatment;
	@Input @InputTextarea private String rejectReason;
	
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceMany @InputChoiceManyAutoComplete 
	private Collection<Function> functions;
	
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceMany @InputChoiceManyAutoComplete 
	private Collection<BudgetaryFunction> budgetaryFunctions;
	
	public String getElectronicMailAddress() {
		if(electronicMailAddress == null && identity != null)
			electronicMailAddress = identity.getElectronicMailAddress();
		return electronicMailAddress;
	}
	
	public String getNames() {
		if(names == null) {
			if(identity == null) {
				if(StringHelper.isNotBlank(firstName))
					names = firstName;
				if(StringHelper.isNotBlank(lastNames))
					if(StringHelper.isBlank(names))
						names = lastNames;
					else
						names += " "+lastNames;
				if(names == null)
					names = ConstantEmpty.STRING;
			}else
				names = identity.getNames();
		}		
		return names;
	}
	
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAMES = "lastNames";
	public static final String FIELD_NAMES = "names";
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS = "electronicMailAddress";
	public static final String FIELD_CREATION_DATE_AS_STRING = "creationDateAsString";
	public static final String FIELD_ACCEPTATION_DATE_AS_STRING = "acceptationDateAsString";
	
	public static final String FIELD_REGISTRATION_NUMBER = "registrationNumber";
	public static final String FIELD_POSTAL_BOX_ADDRESS = "postalBoxAddress";
	public static final String FIELD_MOBILE_PHONE_NUMBER = "mobilePhoneNumber";
	public static final String FIELD_OFFICE_PHONE_NUMBER = "officePhoneNumber";
	public static final String FIELD_OFFICE_PHONE_EXTENSION = "officePhoneExtension";
	public static final String FIELD_ADMINISTRATIVE_UNIT = "administrativeUnit";
	public static final String FIELD_ADMINISTRATIVE_UNIT_AS_STRING = "administrativeUnitAsString";
	public static final String FIELD_ADMINISTRATIVE_FUNCTION = "administrativeFunction";
	public static final String FIELD_SERVICE = "service";
	public static final String FIELD_CIVILITY = "civility";
	public static final String FIELD_CIVILITY_AS_STRING = "civilityAsString";
	public static final String FIELD_GROUP = "group";
	public static final String FIELD_GROUP_AS_STRING = "groupAsString";
	public static final String FIELD_ACT_OF_APPOINTMENT_REFERENCE = "actOfAppointmentReference";
	public static final String FIELD_ACT_OF_APPOINTMENT_SIGNATORY = "actOfAppointmentSignatory";
	public static final String FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE = "actOfAppointmentSignatureDate";
	public static final String FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE_AS_STRING = "actOfAppointmentSignatureDateAsString";
	public static final String FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE_TIMESTAMP = "actOfAppointmentSignatureDateTimestamp";
	public static final String FIELD_FUNCTIONS = "functions";
	public static final String FIELD_BUDGETARY_FUNCTIONS = "budgetaryFunctions";
	public static final String FIELD_TREATMENT = "treatment";
	public static final String FIELD_REJECT_REASON = "rejectReason";
}