package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfileRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileRequestBusiness;

@ApplicationScoped
public class ActorProfileRequestControllerImpl extends AbstractActorRequestControllerImpl<ActorProfileRequest,Profile> implements ActorProfileRequestController,Serializable {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		recordActionIdentifier = ActorProfileRequestBusiness.RECORD;
		cancelActionIdentifier = ActorProfileRequestBusiness.CANCEL;
		processActionIdentifier = ActorProfileRequestBusiness.PROCESS;
	}
	
}