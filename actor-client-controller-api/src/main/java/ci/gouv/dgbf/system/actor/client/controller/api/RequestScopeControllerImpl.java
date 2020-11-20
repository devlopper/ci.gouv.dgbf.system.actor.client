package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestScope;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class RequestScopeControllerImpl extends AbstractControllerEntityImpl<RequestScope> implements RequestScopeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
