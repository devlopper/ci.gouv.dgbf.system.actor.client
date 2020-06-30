package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorScopeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeDto;

public interface ActorScopeController extends ControllerEntity<ActorScope> {

	default void deleteByActorByScopes(Actor actor, Collection<Scope> scopes) {
		if(actor == null || CollectionHelper.isEmpty(scopes))
			return;
		/*
		ActorScopeRepresentation.getProxy().deleteByActorByScopes(new ActorDto().setIdentifier(actor.getIdentifier())
				, scopes.stream().map(x -> new ScopeDto().setIdentifier(x.getIdentifier())).collect(Collectors.toList()));
		*/
		ActorScopeRepresentation.getProxy().deleteByScopes(scopes.stream().map(x -> new ScopeDto().setIdentifier(x.getIdentifier())
				.setActor(new ActorDto().setIdentifier(actor.getIdentifier()))).collect(Collectors.toList()));
	}
}