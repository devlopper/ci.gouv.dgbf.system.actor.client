package ci.gouv.dgbf.system.actor.client.controller.api;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.mapping.MappingHelper;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.RequestDto;

public interface RequestController extends ControllerEntity<Request> {

	default Request getOneToBeCreatedByTypeIdentifier(String typeIdentifier) {
		Response response = RequestRepresentation.getProxy().getOneToBeCreatedByTypeIdentifier(typeIdentifier);
		if(response == null)
			return null;
		return MappingHelper.getSource(response.readEntity(RequestDto.class), Request.class);
	}
	
}
