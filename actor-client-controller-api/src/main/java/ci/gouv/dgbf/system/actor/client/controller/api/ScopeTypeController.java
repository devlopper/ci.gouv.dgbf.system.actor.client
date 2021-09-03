package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;

public interface ScopeTypeController extends ControllerEntity<ScopeType> {

	Collection<ScopeType> read();
	Collection<ScopeType> readRequestable();	
	ScopeType prepareEdit(String identifier);
}