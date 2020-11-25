package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputDate;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputText;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputTextarea;
import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Request extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneCombo
	private RequestType type;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete
	private AdministrativeUnit administrativeUnit;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete
	private Section section;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete
	private BudgetSpecializationUnit budgetSpecializationUnit;
	
	private Actor actor;
	
	@Input @InputTextarea private String comment;	
	@Input @InputText private String administrativeFunction;
	@Input @InputText private String certificatReference;
	@Input @InputText private String certificatSignatory;
	@Input @InputDate private Date certificatSignatureDate;
	
	private Collection<Function> functions;
	
	private Collection<String> functionsAsStrings;
	private String typeAsString;
	private String actorAsString;
	private String actorCode;
	private String actorNames;
	private String creationDateAsString;
	private String processingDateAsString;
	
	private RequestStatus status;
	private String statusAsString;
	@Input @InputChoiceOneRadio @NotNull private String treatment;
	@Input @InputTextarea private String rejectionReason;
	
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_STATUS_AS_STRING = "statusAsString";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_TYPE_AS_STRING = "typeAsString";
	public static final String FIELD_ACTOR = "actor";
	public static final String FIELD_ACTOR_AS_STRING = "actorAsString";
	public static final String FIELD_ACTOR_CODE = "actorCode";
	public static final String FIELD_ACTOR_NAMES = "actorNames";
	public static final String FIELD_COMMENT = "comment";
	public static final String FIELD_CREATION_DATE = "creationDate";
	public static final String FIELD_CREATION_DATE_AS_STRING = "creationDateAsString";
	public static final String FIELD_PROCESSING_DATE = "processingDate";
	public static final String FIELD_PROCESSING_DATE_AS_STRING = "processingDateAsString";
	public static final String FIELD_FUNCTIONS = "functions";
	public static final String FIELD_FUNCTIONS_AS_STRINGS = "functionsAsStrings";
	
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
	public static final String FIELD_SERVICE = "service";
	public static final String FIELD_ADMINISTRATIVE_FUNCTION = "administrativeFunction";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_CIVILITY = "civility";
	public static final String FIELD_GROUP = "group";
	public static final String FIELD_SECTION = "section";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT = "budgetSpecializationUnit";
	public static final String FIELD_CREDIT_MANAGER_HOLDER = "creditMangerHolder";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER = "authorizingOfficerHolder";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER = "financialControllerHolder";
	public static final String FIELD_ACCOUNTING_HOLDER = "accountingHolder";
	
	public static final String FIELD_CERTIFICAT_REFERENCE = "certificatReference";
	public static final String FIELD_CERTIFICAT_SIGNATORY = "certificatSignatory";
	public static final String FIELD_CERTIFICAT_SIGNATURE_DATE = "certificatSignatureDate";
	
	public static final String FIELD_TREATMENT = "treatment";
	public static final String FIELD_REJECTION_REASON = "rejectionReason";
}