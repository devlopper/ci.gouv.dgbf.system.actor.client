package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Locality;

public interface LocalityController extends ControllerEntity<Locality> {

	Collection<Locality> readRegions();
	
	Collection<Locality> readByParents(Collection<Locality> parents);
	Collection<Locality> readByParentsIdentifiers(Collection<String> parentsIdentifiers);
	
	Collection<Locality> readByParent(Locality parent);
	Collection<Locality> readByParentIdentifier(String parentIdentifier);
}