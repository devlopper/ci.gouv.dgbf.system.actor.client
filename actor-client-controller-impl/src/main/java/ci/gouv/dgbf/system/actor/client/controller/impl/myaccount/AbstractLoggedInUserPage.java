package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractLoggedInUserPage extends AbstractPageContainerManagedImpl implements MyAccountTheme, Serializable {

	protected Actor actor;

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actor = __inject__(ActorController.class).getLoggedIn();
	}
}