package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

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
	
	private ExecutionImputationScopeFunction creditManager;	
	private ExecutionImputationScopeFunction authorizingOfficer;	
	private ExecutionImputationScopeFunction financialController;
	private ExecutionImputationScopeFunction accounting;
	
	private Collection<Function> functions;
	
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
	
	public static final String FIELD_CREDIT_MANAGER = "creditManager";
	public static final String FIELD_CREDIT_MANAGER_HOLDER = FieldHelper.join(FIELD_CREDIT_MANAGER,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT = FieldHelper.join(FIELD_CREDIT_MANAGER,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	
	public static final String FIELD_AUTHORIZING_OFFICER = "authorizingOfficer";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER = FieldHelper.join(FIELD_AUTHORIZING_OFFICER,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT = FieldHelper.join(FIELD_AUTHORIZING_OFFICER,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	
	public static final String FIELD_FINANCIAL_CONTROLLER = "financialController";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER = FieldHelper.join(FIELD_FINANCIAL_CONTROLLER,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT = FieldHelper.join(FIELD_FINANCIAL_CONTROLLER,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
	
	public static final String FIELD_ACCOUNTING = "accounting";
	public static final String FIELD_ACCOUNTING_HOLDER = FieldHelper.join(FIELD_ACCOUNTING,ExecutionImputationScopeFunction.FIELD_HOLDER);
	public static final String FIELD_ACCOUNTING_ASSISTANT = FieldHelper.join(FIELD_ACCOUNTING,ExecutionImputationScopeFunction.FIELD_ASSISTANT);
}