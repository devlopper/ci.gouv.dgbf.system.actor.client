package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityCounter;

@ApplicationScoped
public class RequestControllerImpl extends AbstractControllerEntityImpl<Request> implements RequestController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Long countByProcessed(Boolean processed) {
		Arguments<Request> arguments = new Arguments<>();
		arguments.queryIdentifierCountDynamic(Request.class);
		if(processed != null)
			arguments.filterFieldsValues(RequestQuerier.PARAMETER_NAME_PROCESSED,processed);
		return EntityCounter.getInstance().count(Request.class,arguments);
	}
}