package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;

@ApplicationScoped
public class RequestTypeControllerImpl extends AbstractControllerEntityImpl<RequestType> implements RequestTypeController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<RequestType> read() {
		return EntityReader.getInstance().readMany(RequestType.class, new Arguments<RequestType>().queryIdentifierReadDynamicMany(RequestType.class));
	}

	@Override
	public RequestType getByIdentifier(String identifier) {
		if(StringHelper.isBlank(identifier))
			return null;
		return EntityReader.getInstance().readOne(RequestType.class, new Arguments<RequestType>().queryIdentifierReadDynamicOne(RequestType.class)
				.filterByIdentifier(identifier));
	}
}