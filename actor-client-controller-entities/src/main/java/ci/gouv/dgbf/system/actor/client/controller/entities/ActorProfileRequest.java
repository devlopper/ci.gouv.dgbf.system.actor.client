package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyAutoComplete;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor //@Accessors(chain=true)
public class ActorProfileRequest extends AbstractActorRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOneCombo
	private ProfileType scopeType;
	private String profileTypeAsString;	
	
	private Profile profile;
	@Input @InputChoice(choices = @Choices(count = Count.AUTO_COMPLETE)) @InputChoiceMany @InputChoiceManyAutoComplete
	private Collection<Profile> profiles;
	private String profileAsString;	
	private Collection<String> profilesIdentifiers;
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
	public static final String FIELD_PROFILE_TYPE = "profileType";
	public static final String FIELD_PROFILE = "profile";
	public static final String FIELD_PROFILES = "profiles";
	public static final String FIELD_PROFILE_STRING = "profileAsString";
	
	public static final String FIELD_PROFILE_TYPE_AS_STRING = "profileTypeAsString";
	
}