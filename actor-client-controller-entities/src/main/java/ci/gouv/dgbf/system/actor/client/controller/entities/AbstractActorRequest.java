package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringAuditedImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneRadio;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputTextarea;

import ci.gouv.dgbf.system.actor.server.persistence.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter //@Accessors(chain=true)
public abstract class AbstractActorRequest extends AbstractDataIdentifiableSystemStringAuditedImpl implements Serializable {

	protected Actor actor;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceMany @InputChoiceManyAutoComplete
	protected Collection<Actor> actors;
	protected String actorAsString;	
	protected Collection<String> actorsIdentifiers;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOne @InputChoiceOneRadio
	@NotNull
	//@Input @InputBoolean @InputBooleanButton
	protected Boolean granted;
	protected String grantedAsString;	
	
	@Input @InputTextarea protected String comment;	
	@Input @InputTextarea protected String processingComment;
	
	protected Boolean ignoreExisting;
	
	public static final String FIELD_ACTOR = "actor";
	public static final String FIELD_ACTORS = "actors";
	public static final String FIELD_ACTOR_STRING = "actorAsString";
	public static final String FIELD_PROCESSED = "processed";
	public static final String FIELD_GRANTED = "granted";
	public static final String FIELD_GRANTED_AS_STRING = "grantedAsString";
	public static final String FIELD_COMMENT = "comment";
	public static final String FIELD_PROCESSING_COMMENT = "processingComment";
}