package ci.gouv.dgbf.system.actor.client.controller.api;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import org.cyk.utility.client.controller.ControllerEntity;

public interface ProfileController extends ControllerEntity<Profile> {

	Profile prepareEdit(String identifier,String typeIdentifier);
}