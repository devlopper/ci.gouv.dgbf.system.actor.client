package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor //@Accessors(chain=true)
public class Assignments extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	/* Imputation */
	
	private String sectionAsString;
	private String budgetSpecializationUnitAsString;
	private String actionAsString;
	private String activityAsString;
	private String economicNatureAsString;
	private String administrativeUnitAsString;
	private String activityCategoryAsString;
	private String expenditureNatureAsString;
	
	/* Affectations */
	
	private ScopeFunction creditManagerHolder;
	private ScopeFunction creditManagerAssistant;	
	private ScopeFunction authorizingOfficerHolder;
	private ScopeFunction authorizingOfficerAssistant;	
	private ScopeFunction financialControllerHolder;
	private ScopeFunction financialControllerAssistant;	
	private ScopeFunction accountingHolder;
	private ScopeFunction accountingAssistant;
	
	private String creditManagerHolderAsString;
	private String creditManagerAssistantAsString;	
	private String authorizingOfficerHolderAsString;
	private String authorizingOfficerAssistantAsString;	
	private String financialControllerHolderAsString;
	private String financialControllerAssistantAsString;	
	private String accountingHolderAsString;
	private String accountingAssistantAsString;
	
	/**/
	
	private Filter.Dto filter;
	private Collection<String> overridablesFieldsNames;
	private Collection<Function> functions;
	
	/**/
	
	public Collection<String> getOverridablesFieldsNames(Boolean injectIfNull) {
		if(overridablesFieldsNames == null && Boolean.TRUE.equals(injectIfNull))
			overridablesFieldsNames = new ArrayList<>();
		return overridablesFieldsNames;
	}
	
	/**/
	
	public static final String FIELD_EXECUTION_IMPUTATION = "executionImputation";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING = "budgetSpecializationUnitAsString";
	public static final String FIELD_ACTION_AS_STRING = "actionAsString";
	public static final String FIELD_ACTIVITY_AS_STRING = "activityAsString";
	public static final String FIELD_ECONOMIC_NATURE_AS_STRING = "economicNatureAsString";
	public static final String FIELD_ADMINISTRATIVE_UNIT_AS_STRING = "administrativeUnitAsString";
	public static final String FIELD_ACTIVITY_CATEGORY_AS_STRING = "activityCategoryAsString";
	public static final String FIELD_EXPENDITURE_NATURE_AS_STRING = "expenditureNatureAsString";
	
	public static final String FIELD_CREDIT_MANAGER_HOLDER = "creditManagerHolder";
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT = "creditManagerAssistant";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER = "authorizingOfficerHolder";
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT = "authorizingOfficerAssistant";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER = "financialControllerHolder";
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT = "financialControllerAssistant";
	public static final String FIELD_ACCOUNTING_HOLDER = "accountingHolder";
	public static final String FIELD_ACCOUNTING_ASSISTANT = "accountingAssistant";
	
	public static final String FIELD_CREDIT_MANAGER_HOLDER_AS_STRING = "creditManagerHolderAsString";
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT_AS_STRING = "creditManagerAssistantAsString";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING = "authorizingOfficerHolderAsString";
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT_AS_STRING = "authorizingOfficerAssistantAsString";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING = "financialControllerHolderAsString";
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AS_STRING = "financialControllerAssistantAsString";
	public static final String FIELD_ACCOUNTING_HOLDER_AS_STRING = "accountingHolderAsString";
	public static final String FIELD_ACCOUNTING_ASSISTANT_AS_STRING = "accountingAssistantAsString";
}