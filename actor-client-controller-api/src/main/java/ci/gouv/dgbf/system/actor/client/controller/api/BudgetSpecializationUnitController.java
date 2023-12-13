package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;

public interface BudgetSpecializationUnitController extends ControllerEntity<BudgetSpecializationUnit> {

	default Collection<BudgetSpecializationUnit> readVisiblesBySectionIdentifierByActorCodeForUI(String sectionIdentifier,String actorCode) {
		if(StringHelper.isBlank(actorCode))
			return null;
		return readVisiblesByLoggedInActorCodeForUI().stream().filter(budgetSpecializationUnit -> budgetSpecializationUnit.getSectionIdentifier().equals(sectionIdentifier)).collect(Collectors.toList());
	}
	
	default Collection<BudgetSpecializationUnit> readVisiblesByLoggedInActorCodeForUI() {
		@SuppressWarnings("unchecked")
		Collection<BudgetSpecializationUnit> budgetSpecializationUnits = (Collection<BudgetSpecializationUnit>) SessionHelper.getAttributeValue("visibleBudgetSpecializationUnits");
		if(budgetSpecializationUnits == null) {
			budgetSpecializationUnits = EntityReader.getInstance().readMany(BudgetSpecializationUnit.class
					, BudgetSpecializationUnitQuerier.QUERY_IDENTIFIER_READ_VISIBLES_BY_ACTOR_CODE_FOR_UI
					,ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, SessionHelper.getUserName());
			if (budgetSpecializationUnits == null) 
				budgetSpecializationUnits = new ArrayList<>();
			SessionHelper.setAttributeValue("visibleBudgetSpecializationUnits", budgetSpecializationUnits);
		}
		return budgetSpecializationUnits;
	}
	
	default Collection<BudgetSpecializationUnit> readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(String sectionIdentifier) {
		return readVisiblesBySectionIdentifierByActorCodeForUI(sectionIdentifier,SessionHelper.getUserName());
	}
}