package ci.gouv.dgbf.system.actor.client.controller.entities;

import static ci.gouv.dgbf.system.actor.server.persistence.entities.ExecutionImputation.buildScopeFunctionAssistantCodeNameFieldName;
import static ci.gouv.dgbf.system.actor.server.persistence.entities.ExecutionImputation.buildScopeFunctionHolderCodeNameFieldName;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ExecutionImputation extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private String sectionCodeName;	
	private String budgetSpecializationUnitCodeName;	
	private String actionCodeName;	
	private String activityCodeName;	
	private String economicNatureCodeName;
	private String administrativeUnitCodeName;
	private String activityCategoryCodeName;
	private String expenditureNatureCodeName;
	
	private String creditManagerHolderScopeFunctionIdentifier;
	private String creditManagerHolderScopeFunctionCodeName;
	private String creditManagerAssistantScopeFunctionIdentifier;
	private String creditManagerAssistantScopeFunctionCodeName;
	
	private String authorizingOfficerHolderScopeFunctionIdentifier;
	private String authorizingOfficerHolderScopeFunctionCodeName;
	private String authorizingOfficerAssistantScopeFunctionIdentifier;
	private String authorizingOfficerAssistantScopeFunctionCodeName;
	
	private String financialControllerHolderScopeFunctionIdentifier;
	private String financialControllerHolderScopeFunctionCodeName;
	private String financialControllerAssistantScopeFunctionIdentifier;
	private String financialControllerAssistantScopeFunctionCodeName;
	
	private String accountingHolderScopeFunctionIdentifier;
	private String accountingHolderScopeFunctionCodeName;
	private String accountingAssistantScopeFunctionIdentifier;
	private String accountingAssistantScopeFunctionCodeName;
	
	private ExecutionImputationScopeFunction creditManager;	
	private ExecutionImputationScopeFunction authorizingOfficer;	
	private ExecutionImputationScopeFunction financialController;
	private ExecutionImputationScopeFunction accounting;
	
	private Collection<Function> functions;
	
	private Filter.Dto filter;
	
	public ExecutionImputationScopeFunction getCreditManager(Boolean instantiateIfNull) {
		if(creditManager == null && Boolean.TRUE.equals(instantiateIfNull))
			creditManager = new ExecutionImputationScopeFunction();
		return creditManager;
	}
	
	public ExecutionImputationScopeFunction getAuthorizingOfficer(Boolean instantiateIfNull) {
		if(authorizingOfficer == null && Boolean.TRUE.equals(instantiateIfNull))
			authorizingOfficer = new ExecutionImputationScopeFunction();
		return authorizingOfficer;
	}
	
	public ExecutionImputationScopeFunction getFinancialController(Boolean instantiateIfNull) {
		if(financialController == null && Boolean.TRUE.equals(instantiateIfNull))
			financialController = new ExecutionImputationScopeFunction();
		return financialController;
	}
	
	public ExecutionImputationScopeFunction getAccounting(Boolean instantiateIfNull) {
		if(accounting == null && Boolean.TRUE.equals(instantiateIfNull))
			accounting = new ExecutionImputationScopeFunction();
		return accounting;
	}
	
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "budgetSpecializationUnitCodeName";
	public static final String FIELD_ACTION_CODE_NAME = "actionCodeName";
	public static final String FIELD_ACTIVITY_CODE_NAME = "activityCodeName";
	public static final String FIELD_ECONOMIC_NATURE_CODE_NAME = "economicNatureCodeName";
	public static final String FIELD_ADMINISTRATIVE_UNIT_CODE_NAME = "administrativeUnitCodeName";
	public static final String FIELD_ACTIVITY_CATEGORY_CODE_NAME = "activityCategoryCodeName";
	public static final String FIELD_EXPENDITURE_NATURE_CODE_NAME = "expenditureNatureCodeName";
	
	public static final String FIELD_CREDIT_MANAGER = "creditManager";
	public static final String FIELD_CREDIT_MANAGER_HOLDER = FieldHelper.join(FIELD_CREDIT_MANAGER,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_CREDIT_MANAGER_HOLDER_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionHolderCodeNameFieldName(FIELD_CREDIT_MANAGER);
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT = FieldHelper.join(FIELD_CREDIT_MANAGER,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionAssistantCodeNameFieldName(FIELD_CREDIT_MANAGER);
	
	public static final String FIELD_AUTHORIZING_OFFICER = "authorizingOfficer";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER = FieldHelper.join(FIELD_AUTHORIZING_OFFICER,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionHolderCodeNameFieldName(FIELD_AUTHORIZING_OFFICER);
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT = FieldHelper.join(FIELD_AUTHORIZING_OFFICER,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionAssistantCodeNameFieldName(FIELD_AUTHORIZING_OFFICER);
	
	public static final String FIELD_FINANCIAL_CONTROLLER = "financialController";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER = FieldHelper.join(FIELD_FINANCIAL_CONTROLLER,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionHolderCodeNameFieldName(FIELD_FINANCIAL_CONTROLLER);
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT = FieldHelper.join(FIELD_FINANCIAL_CONTROLLER,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionAssistantCodeNameFieldName(FIELD_FINANCIAL_CONTROLLER);
	
	public static final String FIELD_ACCOUNTING = "accounting";
	public static final String FIELD_ACCOUNTING_HOLDER = FieldHelper.join(FIELD_ACCOUNTING,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_ACCOUNTING_HOLDER_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionHolderCodeNameFieldName(FIELD_ACCOUNTING);
	public static final String FIELD_ACCOUNTING_ASSISTANT = FieldHelper.join(FIELD_ACCOUNTING,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	public static final String FIELD_ACCOUNTING_ASSISTANT_SCOPE_FUNCTION_CODE_NAME = buildScopeFunctionAssistantCodeNameFieldName(FIELD_ACCOUNTING);	
}