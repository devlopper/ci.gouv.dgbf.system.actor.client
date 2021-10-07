package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;

@ApplicationScoped
public class RequestStatusControllerImpl extends AbstractControllerEntityImpl<RequestStatus> implements RequestStatusController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public RequestStatus getByIdentifier(String identifier) {
		if(StringHelper.isBlank(identifier))
			return null;
		return EntityReader.getInstance().readOne(RequestStatus.class, new Arguments<RequestStatus>().queryIdentifierReadDynamicOne(RequestStatus.class)
				.filterByIdentifier(identifier));
	}
}