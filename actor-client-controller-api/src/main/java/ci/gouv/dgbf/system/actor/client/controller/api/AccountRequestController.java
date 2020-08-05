package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;

public interface AccountRequestController extends ControllerEntity<AccountRequest> {

	AccountRequest readByElectronicMailAddress(String electronicMailAddress);
	AccountRequest readByAccessToken(String electronicMailAddress);
	AccountRequest readProjection01ByAccessToken(String accessToken);
	AccountRequest readProjection01WithBudgetaryFunctionsAndFunctionsByAccessToken(String accessToken);
	AccountRequest readProjection01WithBudgetaryFunctionsAndFunctionsByIdentifier(String identifier);
	AccountRequest readProjection02WithBudgetaryFunctionsAndFunctionsByIdentifier(String identifier);
	
	void notifyAccessTokenByElectronicMailAddresses(Collection<String> electronicMailAddresses);
	
	void notifyAccessTokenByElectronicMailAddresses(String...electronicMailAddresses);
}
