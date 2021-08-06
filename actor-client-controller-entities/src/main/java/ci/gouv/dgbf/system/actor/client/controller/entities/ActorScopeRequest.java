package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ActorScopeRequest extends AbstractActorRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Scope scope;
	private String scopeAsString;	
	private Collection<String> scopesIdentifiers;
	
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
	
	public static final String FIELD_SCOPE = "scope";
	public static final String FIELD_SCOPE_STRING = "scopeAsString";
}