package ci.gouv.dgbf.system.actor.client.controller.api;

import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;

import java.util.Collection;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;

public interface SectionController extends ControllerEntity<Section> {

	default Collection<Section> readAllForUI() {
		return EntityReader.getInstance().readMany(Section.class, SectionQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}
	
	default Collection<Section> readVisiblesByActorCodeForUI(String actorCode) {
		if(StringHelper.isBlank(actorCode))
			return null;
		return EntityReader.getInstance().readMany(Section.class, SectionQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_ACTOR_CODE_FOR_UI
				,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, actorCode);
	}
	
	default Collection<Section> readVisiblesByLoggedInActorCodeForUI() {
		return readVisiblesByActorCodeForUI(SessionHelper.getUserName());
	}
}