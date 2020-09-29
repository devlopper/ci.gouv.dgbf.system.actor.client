package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Menu;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class MenuControllerImpl extends AbstractControllerEntityImpl<Menu> implements MenuController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
