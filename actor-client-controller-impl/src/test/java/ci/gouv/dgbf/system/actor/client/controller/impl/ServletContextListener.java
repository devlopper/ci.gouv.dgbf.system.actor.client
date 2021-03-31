package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.cyk.utility.server.deployment.AbstractServletContextListener;

public class ServletContextListener extends AbstractServletContextListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(ServletContext context) {
		super.__initialize__(context);
		ci.gouv.dgbf.system.actor.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
	}
}