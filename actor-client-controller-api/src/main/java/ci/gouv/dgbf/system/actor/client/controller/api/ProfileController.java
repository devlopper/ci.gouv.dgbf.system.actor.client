package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;

public interface ProfileController extends ControllerEntity<Profile> {

	Collection<Profile> read();
	Collection<Profile> readRequestable(String typeIdentifier);
	Profile prepareEdit(String identifier,String typeIdentifier);
}