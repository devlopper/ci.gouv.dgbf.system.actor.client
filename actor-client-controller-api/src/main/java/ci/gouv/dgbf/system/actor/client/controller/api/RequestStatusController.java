package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;

public interface RequestStatusController extends ControllerEntity<RequestStatus> {

	RequestStatus getByIdentifier(String identifier);
	Collection<RequestStatus> read(Boolean processed);
}