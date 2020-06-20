package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.User;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class UserControllerImpl extends AbstractControllerEntityImpl<User> implements UserController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
