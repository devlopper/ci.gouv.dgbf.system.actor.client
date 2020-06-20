package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfile;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ActorProfileControllerImpl extends AbstractControllerEntityImpl<ActorProfile> implements ActorProfileController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
