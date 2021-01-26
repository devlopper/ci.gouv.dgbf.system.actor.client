package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class RequestDispatchSlipControllerImpl extends AbstractControllerEntityImpl<RequestDispatchSlip> implements RequestDispatchSlipController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
