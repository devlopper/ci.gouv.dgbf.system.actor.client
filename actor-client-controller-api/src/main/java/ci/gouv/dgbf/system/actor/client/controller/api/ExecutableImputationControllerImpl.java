package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ExecutableImputation;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ExecutableImputationControllerImpl extends AbstractControllerEntityImpl<ExecutableImputation> implements ExecutableImputationController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
