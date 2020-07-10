package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	protected Actor actor;
	protected Layout layout;
	
}