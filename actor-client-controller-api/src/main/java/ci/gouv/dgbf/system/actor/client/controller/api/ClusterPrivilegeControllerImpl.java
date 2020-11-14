package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ClusterPrivilege;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ClusterPrivilegeControllerImpl extends AbstractControllerEntityImpl<ClusterPrivilege> implements ClusterPrivilegeController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
