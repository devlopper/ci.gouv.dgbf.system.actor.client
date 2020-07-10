package ci.gouv.dgbf.system.actor.client.deployment;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.dgbf.DesktopDefault;
import org.cyk.utility.client.deployment.AbstractServletContextListener;

import ci.gouv.dgbf.system.actor.client.controller.impl.ApplicationScopeLifeCycleListener;

@WebListener
public class ServletContextListener extends AbstractServletContextListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(ServletContext context) {
		super.__initialize__(context);
		__inject__(ApplicationScopeLifeCycleListener.class).initialize(null);
		DesktopDefault.initialize();
		/*
		MenuGeneratorPortailApiService.HOST = "10.3.4.17";
		MenuGeneratorPortailApiService.PORT = 32300;
		DesktopDefault.IS_SHOW_USER_MENU = Boolean.FALSE;
		DesktopDefault.DYNAMIC_MENU = Boolean.TRUE;
		DesktopDefault.MENU_IDENTIFIER = "COLB";
		DesktopDefault.IS_SHOW_USER_MENU = Boolean.TRUE;
		*/
	}	
}