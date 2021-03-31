package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.servlet.ServletContext;

import org.cyk.utility.__kernel__.variable.VariableHelper;
import org.cyk.utility.__kernel__.variable.VariableName;
import org.cyk.utility.server.deployment.AbstractServletContextListener;

public class ServerServletContextListener extends AbstractServletContextListener implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public void __initialize__(ServletContext context) {
		super.__initialize__(context);
		ci.gouv.dgbf.system.actor.server.persistence.impl.ApplicationScopeLifeCycleListener.initialize();
		VariableHelper.write(VariableName.SYSTEM_LOGGING_THROWABLE_PRINT_STACK_TRACE, Boolean.TRUE);
	}
}