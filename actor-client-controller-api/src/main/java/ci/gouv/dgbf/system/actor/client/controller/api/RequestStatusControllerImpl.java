package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class RequestStatusControllerImpl extends AbstractControllerEntityImpl<RequestStatus> implements RequestStatusController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
