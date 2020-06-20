package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;

@ApplicationScoped
public class ActorControllerImpl extends AbstractControllerEntityImpl<Actor> implements ActorController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
