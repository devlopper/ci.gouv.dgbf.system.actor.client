package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.ThemeManager;
import org.cyk.utility.__kernel__.AbstractApplicationScopeLifeCycleListener;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.representation.RepresentationClassNameGetter;
import org.cyk.utility.__kernel__.security.UserBuilder;
import org.cyk.utility.client.controller.component.menu.MenuBuilderMapInstantiator;
import org.cyk.utility.client.controller.web.FileServlet;

@ApplicationScoped
public class ApplicationScopeLifeCycleListener extends AbstractApplicationScopeLifeCycleListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(Object object) {
		__inject__(ci.gouv.dgbf.system.actor.client.controller.api.ApplicationScopeLifeCycleListener.class).initialize(null);
		__inject__(org.cyk.utility.security.keycloak.client.ApplicationScopeLifeCycleListener.class).initialize(null);
		__setQualifierClassTo__(ci.gouv.dgbf.system.actor.server.annotation.System.class, MenuBuilderMapInstantiator.class,EntitySaver.class
				,ThemeManager.class,UserBuilder.class);
		RepresentationClassNameGetter.AbstractImpl.SYSTEM_PACKAGE_NAME = "ci.gouv.dgbf.system.actor";
		
		FileServlet.LISTENER = new FileServletListenerImpl();
	}
	
	@Override
	public void __destroy__(Object object) {}
	
	/**/
}