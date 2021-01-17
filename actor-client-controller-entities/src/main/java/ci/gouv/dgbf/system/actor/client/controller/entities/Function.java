package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBoolean;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBooleanButton;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Function extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice @InputChoiceOne @InputChoiceOneRadio private FunctionType type;
	private String profileIdentifier;
	private Integer numberOfActorPerScope;
	private String profilesAsString;
	private String scopesAsString;
	private Integer numberOfScopes;
	private String scopeTypesAsString;
	private Collection<String> profilesAsStrings;
	private Collection<ScopeType> scopeTypes;
	@Input @InputBoolean @InputBooleanButton private Boolean shared;
	private String sharedAsString;
	//@Input @InputTextarea private String scopeFunctionCodeScript;
	//@Input @InputTextarea private String scopeFunctionNameScript;
	
	public Boolean isCreditManager() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER.equals(code)
				|| ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT.equals(code);
	}
	
	public Boolean isAuthorizingOfficer() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER.equals(code)
				|| ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT.equals(code);
	}
	
	public Boolean isFinancialController() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER.equals(code)
				|| ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT.equals(code);
	}
	
	public Boolean isAccounting() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER.equals(code)
				|| ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT.equals(code);
	}
	
	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_PROFILES_AS_STRINGS = "profilesAsStrings";
	public static final String FIELD_PROFILES_AS_STRING = "profilesAsString";
	public static final String FIELD_SCOPES_AS_STRING = "scopesAsString";
	public static final String FIELD_NUMBER_OF_SCOPES = "numberOfScopes";
	public static final String FIELD_SCOPE_TYPES_AS_STRING = "scopeTypesAsString";
	public static final String FIELD_NUMBER_OF_ACTOR_PER_SCOPE = "numberOfActorPerScope";
	public static final String FIELD_SHARED = "shared";
	public static final String FIELD_SHARED_AS_STRING = "sharedAsString";
	public static final String FIELD_SCOPE_FUNCTION_CODE_SCRIPT = "scopeFunctionCodeScript";
	public static final String FIELD_SCOPE_FUNCTION_NAME_SCRIPT = "scopeFunctionNameScript";
}