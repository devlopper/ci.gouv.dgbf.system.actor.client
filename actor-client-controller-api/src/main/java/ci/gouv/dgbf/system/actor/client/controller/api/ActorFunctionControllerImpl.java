package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorFunction;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ActorFunctionControllerImpl extends AbstractControllerEntityImpl<ActorFunction> implements ActorFunctionController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
