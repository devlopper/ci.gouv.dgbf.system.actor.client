package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class PrivilegeControllerImpl extends AbstractControllerEntityImpl<Privilege> implements PrivilegeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
