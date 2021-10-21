package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;

public interface ScopeController extends ControllerEntity<Scope> {

	Collection<Scope> getVisibleSectionsByActorCode(String actorCode);
	Collection<Scope> getLoggedInVisibleSections();
	
	Collection<String> getVisibleSectionsIdentifiersByActorCode(String actorCode);
	Collection<String> getLoggedInVisibleSectionsIdentifiers();
}