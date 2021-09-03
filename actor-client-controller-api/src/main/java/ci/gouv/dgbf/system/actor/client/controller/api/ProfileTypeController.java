package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;

public interface ProfileTypeController extends ControllerEntity<ProfileType> {

	Collection<ProfileType> read();
	ProfileType getByIdentifier(String identifier);
}