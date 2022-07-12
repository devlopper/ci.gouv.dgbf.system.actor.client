package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;

public interface BudgetCategoryController extends ControllerEntity<BudgetCategory> {

	default Collection<BudgetCategory> readAllForUI() {
		return EntityReader.getInstance().readMany(BudgetCategory.class, BudgetCategoryQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}
	
	default Collection<BudgetCategory> readVisiblesByActorCodeForUI(String actorCode) {
		if(StringHelper.isBlank(actorCode))
			return null;
		return EntityReader.getInstance().readMany(BudgetCategory.class, BudgetCategoryQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_ACTOR_CODE_FOR_UI
				,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, actorCode);
	}
	
	default Collection<BudgetCategory> readVisiblesByLoggedInActorCodeForUI() {
		return readVisiblesByActorCodeForUI(SessionHelper.getUserName());
	}
	
}