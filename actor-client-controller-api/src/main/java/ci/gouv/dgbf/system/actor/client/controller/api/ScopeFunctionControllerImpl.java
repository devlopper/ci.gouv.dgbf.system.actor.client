package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ScopeFunctionControllerImpl extends AbstractControllerEntityImpl<ScopeFunction> implements ScopeFunctionController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
