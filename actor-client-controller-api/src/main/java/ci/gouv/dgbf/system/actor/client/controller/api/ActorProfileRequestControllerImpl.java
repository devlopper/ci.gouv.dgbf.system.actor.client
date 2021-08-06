package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfileRequest;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ActorProfileRequestControllerImpl extends AbstractControllerEntityImpl<ActorProfileRequest> implements ActorProfileRequestController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
