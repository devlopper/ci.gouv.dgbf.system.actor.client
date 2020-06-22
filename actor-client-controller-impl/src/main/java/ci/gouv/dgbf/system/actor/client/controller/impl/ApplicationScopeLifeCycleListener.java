package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.client.controller.component.menu.MenuBuilderMapInstantiator;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(ci.gouv.dgbf.system.actor.client.controller.api.ApplicationScopeLifeCycleListener.class).initialize(null);
		__setQualifierClassTo__(ci.gouv.dgbf.system.actor.server.annotation.System.class, MenuBuilderMapInstantiator.class,EntitySaver.class);
	}
	
	@Override
	public void __destroy__(Object object) {}
	
	/**/
}
