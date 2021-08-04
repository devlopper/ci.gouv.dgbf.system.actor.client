package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ActorScope extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Actor actor;
	private String actorAsString;
	private Scope scope;
	private String scopeAsString;
	private Boolean visible;
	private Boolean ignoreExisting;
	private Collection<String> actorsIdentifiers;
	private Collection<String> scopesIdentifiers;
	
	public static final String FIELD_ACTOR = "actor";
	public static final String FIELD_SCOPE = "scope";
	public static final String FIELD_ACTOR_STRING = "actorAsString";
	public static final String FIELD_SCOPE_STRING = "scopeAsString";
	public static final String FIELD_VISIBLE = "visible";
}