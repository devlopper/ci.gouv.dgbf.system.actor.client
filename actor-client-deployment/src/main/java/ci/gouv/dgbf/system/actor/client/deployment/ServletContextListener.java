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
		DesktopDefault.initialize(ci.gouv.dgbf.system.actor.server.annotation.System.class,null);
		org.cyk.utility.security.keycloak.client.ApplicationScopeLifeCycleListener.enable(context, "/keycloak/*","/private/*","/mon_compte/*");
		/*
		if(Boolean.TRUE.equals(ConfigurationHelper.getValueAsBoolean(VariableName.KEYCLOAK_ENABLED))) {
			FilterRegistration filterRegistration = context.addFilter("Keycloak Filter", KeycloakOIDCFilter.class);
			filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD)
					, Boolean.TRUE, "/keycloak/*","/private/*","/mon_compte/*");
			LogHelper.logInfo("Keycloak Filter has been registered", getClass());
		}*/
	}
}