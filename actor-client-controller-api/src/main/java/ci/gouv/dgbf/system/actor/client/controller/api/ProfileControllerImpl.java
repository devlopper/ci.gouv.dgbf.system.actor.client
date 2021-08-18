package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ProfileControllerImpl extends AbstractControllerEntityImpl<Profile> implements ProfileController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Profile instantiate(String typeIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
