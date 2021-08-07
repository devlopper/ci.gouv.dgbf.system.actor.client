package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringAuditedImpl;

import ci.gouv.dgbf.system.actor.server.persistence.entities.Actor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public abstract class AbstractActorRequest extends AbstractDataIdentifiableSystemStringAuditedImpl implements Serializable {

	protected Actor actor;
	protected String actorAsString;	
	protected Collection<String> actorsIdentifiers;
	
	protected Boolean granted;
	protected String grantedAsString;	
	
	protected String comment;
	
	protected String processingComment;
	
	protected Boolean ignoreExisting;
	
	public static final String FIELD_ACTOR = "actor";
	public static final String FIELD_ACTOR_STRING = "actorAsString";
	public static final String FIELD_PROCESSED = "processed";
	public static final String FIELD_GRANTED = "granted";
	public static final String FIELD_GRANTED_AS_STRING = "grantedAsString";
	public static final String FIELD_COMMENT = "comment";	
}