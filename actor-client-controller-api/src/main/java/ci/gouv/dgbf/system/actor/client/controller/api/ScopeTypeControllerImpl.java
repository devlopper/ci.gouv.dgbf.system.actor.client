package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ScopeTypeControllerImpl extends AbstractControllerEntityImpl<ScopeType> implements ScopeTypeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
