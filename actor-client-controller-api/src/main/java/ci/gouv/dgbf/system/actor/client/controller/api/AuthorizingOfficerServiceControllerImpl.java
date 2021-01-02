package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.AuthorizingOfficerService;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class AuthorizingOfficerServiceControllerImpl extends AbstractControllerEntityImpl<AuthorizingOfficerService> implements AuthorizingOfficerServiceController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
