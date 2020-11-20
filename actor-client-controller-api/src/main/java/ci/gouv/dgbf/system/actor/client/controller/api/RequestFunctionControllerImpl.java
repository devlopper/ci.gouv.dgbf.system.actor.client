package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestFunction;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class RequestFunctionControllerImpl extends AbstractControllerEntityImpl<RequestFunction> implements RequestFunctionController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
