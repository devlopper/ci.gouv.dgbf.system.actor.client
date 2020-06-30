package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;

@ApplicationScoped
public class ActorScopeControllerImpl extends AbstractControllerEntityImpl<ActorScope> implements ActorScopeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
