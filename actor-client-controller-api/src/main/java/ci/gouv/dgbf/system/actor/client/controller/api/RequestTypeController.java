package ci.gouv.dgbf.system.actor.client.controller.api;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;

public interface RequestTypeController extends ControllerEntity<RequestType> {

	RequestType getByIdentifier(String identifier);
}