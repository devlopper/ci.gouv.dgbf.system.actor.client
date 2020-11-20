package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class RequestTypeControllerImpl extends AbstractControllerEntityImpl<RequestType> implements RequestTypeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
