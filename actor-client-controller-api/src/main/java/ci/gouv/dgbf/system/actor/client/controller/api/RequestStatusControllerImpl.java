package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestStatusQuerier;

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
	
	@Override
	public Collection<RequestStatus> read(Boolean processed) {
		if(processed == null)
			return read();
		return EntityReader.getInstance().readMany(RequestStatus.class, new Arguments<RequestStatus>().queryIdentifierReadDynamicMany(RequestStatus.class)
				.filterFieldsValues(RequestStatusQuerier.PARAMETER_NAME_PROCESSED,processed));
	}
}