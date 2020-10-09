package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ScopeFunction extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceOneAutoComplete private Scope scope;
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneRadio private ScopeType scopeType;
	private String scopeAsString;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo private Function function;
	private String functionAsString;
	private Integer numberOfActor;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneRadio private Boolean shared;
	@Input @InputChoice @InputChoiceOne @InputChoiceOneRadio private String sharedAsString;
	
	public static final String FIELD_SCOPE = "scope";
	public static final String FIELD_FUNCTION = "function";
	public static final String FIELD_SCOPE_TYPE = "scopeType";
	public static final String FIELD_SCOPE_AS_STRING = "scopeAsString";
	public static final String FIELD_FUNCTION_AS_STRING = "functionAsString";
	public static final String FIELD_NUMBER_OF_ACTOR = "numberOfActor";
	public static final String FIELD_SHARED = "shared";
	public static final String FIELD_SHARED_AS_STRING = "sharedAsString";
}