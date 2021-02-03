package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;

public interface BudgetSpecializationUnitController extends ControllerEntity<BudgetSpecializationUnit> {

	default Collection<BudgetSpecializationUnit> readVisiblesBySectionIdentifierByActorCodeForUI(String sectionIdentifier,String actorCode) {
		if(StringHelper.isBlank(actorCode))
			return null;
		return EntityReader.getInstance().readMany(BudgetSpecializationUnit.class
				, BudgetSpecializationUnitQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_SECTION_IDENTIFIER_BY_ACTOR_CODE_FOR_UI
				,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, actorCode,ScopeQuerier.PARAMETER_NAME_SECTION_IDENTIFIER, sectionIdentifier);
	}
	
	default Collection<BudgetSpecializationUnit> readVisiblesByLoggedInActorCodeForUI() {
		return readVisiblesBySectionIdentifierByActorCodeForUI(null,SessionHelper.getUserName());
	}
	
	default Collection<BudgetSpecializationUnit> readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(String sectionIdentifier) {
		return readVisiblesBySectionIdentifierByActorCodeForUI(sectionIdentifier,SessionHelper.getUserName());
	}
}