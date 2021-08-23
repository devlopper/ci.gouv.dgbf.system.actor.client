package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.AbstractActorRequest;

public interface AbstractActorRequestController<REQUEST extends AbstractActorRequest> extends ControllerEntity<REQUEST> {

	Object record(REQUEST request);
	
	Object process(Collection<REQUEST> request,String ignoreChoice,String yesChoice);
	Object grant(REQUEST request);
	
	Object cancel(REQUEST request);
}
