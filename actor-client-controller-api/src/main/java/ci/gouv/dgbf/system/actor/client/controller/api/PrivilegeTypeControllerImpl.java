package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class PrivilegeTypeControllerImpl extends AbstractControllerEntityImpl<PrivilegeType> implements PrivilegeTypeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
