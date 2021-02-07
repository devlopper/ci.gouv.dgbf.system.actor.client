package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;

public interface AdministrativeUnitController extends ControllerEntity<AdministrativeUnit> {

	default Collection<AdministrativeUnit> readVisiblesBySectionIdentifierByActorCodeForUI(String sectionIdentifier,String actorCode) {
		if(StringHelper.isBlank(actorCode))
			return null;
		return EntityReader.getInstance().readMany(AdministrativeUnit.class
				,AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_SECTION_IDENTIFIER_BY_ACTOR_CODE_FOR_UI
				,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, actorCode,ScopeQuerier.PARAMETER_NAME_SECTION_IDENTIFIER, sectionIdentifier);
	}
	
	default Collection<AdministrativeUnit> readVisiblesByLoggedInActorCodeForUI() {
		return EntityReader.getInstance().readMany(AdministrativeUnit.class
				, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_ACTOR_CODE_FOR_UI
				,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, SessionHelper.getUserName());
	}
	
	default Collection<AdministrativeUnit> readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(String sectionIdentifier) {
		return readVisiblesBySectionIdentifierByActorCodeForUI(sectionIdentifier,SessionHelper.getUserName());
	}
	
}
