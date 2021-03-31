package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.cyk.utility.__kernel__.identifier.resource.ProxyUniformResourceIdentifierGetter;

public class ClientServletContextListener implements ServletContextListener,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContextListener.super.contextInitialized(sce);
		ProxyUniformResourceIdentifierGetter.UNIFORM_RESOURCE_IDENTIFIER_STRING.set("http://127.0.0.1:8080/test_server/api");
	}
}