package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor //@Accessors(chain=true)
public class ActorScopeRequest extends AbstractActorRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneCombo
	private ScopeType scopeType;
	private Scope scope;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceMany @InputChoiceManyAutoComplete
	private Collection<Scope> scopes;
	private String scopeAsString;	
	private Collection<String> scopesIdentifiers;
	/*
	@Override
	public ActorScopeRequest setActorsIdentifiers(Collection<String> actorsIdentifiers) {
		return (ActorScopeRequest) super.setActorsIdentifiers(actorsIdentifiers);
	}
	
	@Override
	public ActorScopeRequest setIgnoreExisting(Boolean ignoreExisting) {
		return (ActorScopeRequest) super.setIgnoreExisting(ignoreExisting);
	}
	
	@Override
	public ActorScopeRequest setActorAsString(String actorAsString) {
		return (ActorScopeRequest) super.setActorAsString(actorAsString);
	}
	*/
	public static final String FIELD_SCOPE_TYPE = "scopeType";
	public static final String FIELD_SCOPE = "scope";
	public static final String FIELD_SCOPES = "scopes";
	public static final String FIELD_SCOPE_STRING = "scopeAsString";
}