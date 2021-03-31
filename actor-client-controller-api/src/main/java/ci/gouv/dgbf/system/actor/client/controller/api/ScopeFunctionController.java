package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;

public interface ScopeFunctionController extends ControllerEntity<ScopeFunction> {

	Collection<ScopeFunction> readByScopeIdentifierByFunctionIdentifier(String scopeIdentifier,String functionIdentifier);
	
	String computeCreditManagerHolderNameByAdministrativeUnitIdentifier(String administrativeUnitIdentifier);
	
	String computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier(String budgetSpecializationUnitIdentifier,String localityIdentifier);
}