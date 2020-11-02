package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBoolean;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBooleanButton;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputText;
import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ScopeTypeFunction extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private ScopeType scopeType;
	private String scopeTypeAsString;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private Function function;
	private String functionAsString;
	@Input @InputBoolean @InputBooleanButton private Boolean scopeFunctionDerivable;
	private String scopeFunctionDerivableAsString;
	@Input @InputText private String scopeFunctionCodeScript;
	@Input @InputText private String scopeFunctionNameScript;
	
	public static final String FIELD_SCOPE_TYPE = "scopeType";
	public static final String FIELD_SCOPE_TYPE_AS_STRING = "scopeTypeAsString";
	public static final String FIELD_FUNCTION = "function";
	public static final String FIELD_FUNCTION_AS_STRING = "functionAsString";
	public static final String FIELD_SCOPE_FUNCTION_DERIVABLE = "scopeFunctionDerivable";
	public static final String FIELD_SCOPE_FUNCTION_DERIVABLE_AS_STRING = "scopeFunctionDerivableAsString";
	public static final String FIELD_SCOPE_FUNCTION_CODE_SCRIPT = "scopeFunctionCodeScript";
	public static final String FIELD_SCOPE_FUNCTION_NAME_SCRIPT = "scopeFunctionNameScript";
}