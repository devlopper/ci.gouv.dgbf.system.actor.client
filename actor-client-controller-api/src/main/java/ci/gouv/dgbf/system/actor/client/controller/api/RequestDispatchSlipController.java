package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;

public interface RequestDispatchSlipController extends ControllerEntity<RequestDispatchSlip> {

	Collection<RequestDispatchSlip> read(String sectionIdentifier,String functionIdentifier);
	
}