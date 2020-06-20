package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.UserAccountProfile;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class UserAccountProfileControllerImpl extends AbstractControllerEntityImpl<UserAccountProfile> implements UserAccountProfileController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
