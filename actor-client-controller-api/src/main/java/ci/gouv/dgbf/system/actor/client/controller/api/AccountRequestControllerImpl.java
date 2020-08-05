package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AccountRequestQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;

@ApplicationScoped
public class AccountRequestControllerImpl extends AbstractControllerEntityImpl<AccountRequest> implements AccountRequestController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public AccountRequest readByElectronicMailAddress(String electronicMailAddress) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier.QUERY_IDENTIFIER_READ_BY_ELECTRONIC_MAIL_ADDRESS)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS,electronicMailAddress)
						)));
	}
	
	@Override
	public AccountRequest readByAccessToken(String accessToken) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier.QUERY_IDENTIFIER_READ_BY_ACCESS_TOKEN)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_ACCESS_TOKEN,accessToken)
						)));
	}
	
	@Override
	public AccountRequest readProjection01ByAccessToken(String accessToken) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier.QUERY_IDENTIFIER_READ_PROJECTION_01_BY_ACCESS_TOKEN)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_ACCESS_TOKEN,accessToken)
						)));
	}
	
	@Override
	public AccountRequest readProjection01WithBudgetaryFunctionsAndFunctionsByAccessToken(String accessToken) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier
						.QUERY_IDENTIFIER_READ_PROJECTION_01_WITH_BUDGETARIES_FUNCTIONS_AND_FUNCTIONS_BY_ACCESS_TOKEN)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_ACCESS_TOKEN,accessToken)
						)));
	}
	
	@Override
	public AccountRequest readProjection01WithBudgetaryFunctionsAndFunctionsByIdentifier(String identifier) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier
						.QUERY_IDENTIFIER_READ_PROJECTION_01_WITH_BUDGETARIES_FUNCTIONS_AND_FUNCTIONS_BY_IDENTIFIER)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_IDENTIFIER,identifier)
						)));
	}
	
	@Override
	public AccountRequest readProjection02WithBudgetaryFunctionsAndFunctionsByIdentifier(String identifier) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier
						.QUERY_IDENTIFIER_READ_PROJECTION_02_WITH_BUDGETARIES_FUNCTIONS_AND_FUNCTIONS_BY_IDENTIFIER)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_IDENTIFIER,identifier)
						)));
	}
	
	@Override
	public void notifyAccessTokenByElectronicMailAddresses(Collection<String> electronicMailAddresses) {
		if(CollectionHelper.isEmpty(electronicMailAddresses))
			return;
		AccountRequestRepresentation.getProxy().notifyAccessTokenByElectronicMailAddresses((List<String>) electronicMailAddresses);
	}

	@Override
	public void notifyAccessTokenByElectronicMailAddresses(String... electronicMailAddresses) {
		if(ArrayHelper.isEmpty(electronicMailAddresses))
			return;
		notifyAccessTokenByElectronicMailAddresses(CollectionHelper.listOf(electronicMailAddresses));
	}	
}