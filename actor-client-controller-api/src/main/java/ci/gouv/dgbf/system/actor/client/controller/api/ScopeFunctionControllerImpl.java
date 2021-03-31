package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.RequestExecutor;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.representation.api.ScopeFunctionRepresentation;

@ApplicationScoped
public class ScopeFunctionControllerImpl extends AbstractControllerEntityImpl<ScopeFunction> implements ScopeFunctionController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<ScopeFunction> readByScopeIdentifierByFunctionIdentifier(String scopeIdentifier, String functionIdentifier) {
		return RequestExecutor.getInstance().executeList(new RequestExecutor.Request.AbstractImpl() {			
			@Override
			public Response execute() {
				return ScopeFunctionRepresentation.getProxy().getByScopeIdentifierByFunctionIdentifier(scopeIdentifier, functionIdentifier);
			}
		},ScopeFunction.class);
	}
	
	@Override
	public String computeCreditManagerHolderNameByAdministrativeUnitIdentifier(String administrativeUnitIdentifier) {
		return RequestExecutor.getInstance().executeString(new RequestExecutor.Request.AbstractImpl() {			
			@Override
			public Response execute() {
				return ScopeFunctionRepresentation.getProxy().computeCreditManagerHolderNameByAdministrativeUnitIdentifier(administrativeUnitIdentifier);
			}
		});
	}

	@Override
	public String computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier(
			String budgetSpecializationUnitIdentifier, String localityIdentifier) {
		return RequestExecutor.getInstance().executeString(new RequestExecutor.Request.AbstractImpl() {			
			@Override
			public Response execute() {
				return ScopeFunctionRepresentation.getProxy()
						.computeAuthorizingOfficerHolderNameByBudgetSpecializationUnitIdentifierByLocalityIdentifier(budgetSpecializationUnitIdentifier,localityIdentifier);
			}
		});
	}
	
}