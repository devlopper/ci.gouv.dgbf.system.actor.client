package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableAuditedImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBoolean;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBooleanButton;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ScopeFunction extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableAuditedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private String codePrefix;
	
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete private Scope scope;
	private String scopeIdentifier;
	private String scopeAsString;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneRadio private ScopeType scopeType;	
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceManyAutoComplete private Collection<Scope> scopes;
	
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private Function function;
	private String functionIdentifier;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private String subFunctionCode;
	private String functionCode;
	private String functionAsString;
	private Collection<String> functionsIdentifiers;
	private Boolean isHolder;
	
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private Locality locality;
	private String localityIdentifier;
	private String localityAsString;
	
	private Integer numberOfActor;
	private Collection<String> actorsNames;
	private Collection<String> actorsCodes;
	private Collection<String> actorsAsStrings;
	
	@Input @InputBoolean @InputBooleanButton private Boolean shared;	
	private String sharedAsString;
	
	private String parentIdentifier;
	private String parentAsString;
	
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceManyAutoComplete private Collection<Scope> children;
	private Collection<String> childrenIdentifiers;
	private Collection<String> childrenCodesNames;
	
	private Boolean requested;
	private String requestedAsString;
	private Boolean granted;
	private String grantedAsString;	
	
	private String actorAsString;	
	private String assignmentToActorMessage;
	
	private String budgetCategoryAsString;
	private String budgetCategoryCode;
	
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private Section section;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private AdministrativeUnit administrativeUnit;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private BudgetSpecializationUnit budgetSpecializationUnit;
	
	/**/
	
	private String signatureSpecimenReadReportURIQuery;
	private Collection<ScopeFunction> __auditRecords__;
	
	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_CODE_PREFIX = "codePrefix";
	
	public static final String FIELD_SCOPE = "scope";
	public static final String FIELD_SCOPES = "scopes";
	public static final String FIELD_SCOPE_TYPE = "scopeType";
	public static final String FIELD_SCOPE_AS_STRING = "scopeAsString";
	
	public static final String FIELD_FUNCTION = "function";
	public static final String FIELD_FUNCTION_CODE = "functionCode";
	public static final String FIELD_FUNCTION_AS_STRING = "functionAsString";
	public static final String FIELD_IS_HOLDER = "isHolder";
	public static final String FIELD_SUB_FUNCTION_CODE = "subFunctionCode";
	
	public static final String FIELD_LOCALITY = "locality";
	public static final String FIELD_LOCALITY_AS_STRING = "localityAsString";
	
	public static final String FIELD_NUMBER_OF_ACTOR = "numberOfActor";
	public static final String FIELD_ACTORS_NAMES = "actorsNames";
	public static final String FIELD_ACTORS_CODES = "actorsCodes";
	public static final String FIELD_ACTORS_AS_STRINGS = "actorsAsStrings";
	
	public static final String FIELD_SHARED = "shared";
	public static final String FIELD_SHARED_AS_STRING = "sharedAsString";
	
	public static final String FIELD_PARENT_IDENTIFIER = "parentIdentifier";
	public static final String FIELD_PARENT_AS_STRING = "parentAsString";
	public static final String FIELD_CHILDREN = "children";
	public static final String FIELD_CHILDREN_IDENTIFIERS = "childrenIdentifiers";
	public static final String FIELD_CHILDREN_CODES_NAMES = "childrenCodesNames";
	
	public static final String FIELD_REQUESTED = "requested";
	public static final String FIELD_REQUESTED_AS_STRING = "requestedAsString";
	public static final String FIELD_GRANTED = "granted";
	public static final String FIELD_GRANTED_AS_STRING = "grantedAsString";
	
	public static final String FIELD_ACTOR_AS_STRING = "actorAsString";
	public static final String FIELD_ASSIGNMENT_TO_ACTOR_MESSAGE = "assignmentToActorMessage";
	
	public static final String FIELD_SECTION = "section";
	public static final String FIELD_ADMINISTRATIVE_UNIT = "administrativeUnit";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT = "budgetSpecializationUnit";
}