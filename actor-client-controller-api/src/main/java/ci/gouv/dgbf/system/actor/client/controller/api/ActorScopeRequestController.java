package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;

public interface ActorScopeRequestController extends ControllerEntity<ActorScopeRequest> {

	Object record(ActorScopeRequest actorScopeRequest);
	
	Object process(Collection<ActorScopeRequest> actorScopeRequest,String ignoreChoice,String yesChoice);
	Object processOne(ActorScopeRequest actorScopeRequest);
	
	Object cancel(ActorScopeRequest actorScopeRequest);
}
