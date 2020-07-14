package ci.gouv.dgbf.system.actor.client.controller.api;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AccountRequestQuerier;

public interface AccountRequestController extends ControllerEntity<AccountRequest> {

	default AccountRequest readByElectronicMailAddress(String electronicMailAddress) {
		return EntityReader.getInstance().readOne(AccountRequest.class,new Arguments<AccountRequest>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(AccountRequestQuerier.QUERY_IDENTIFIER_READ_BY_ELECTRONIC_MAIL_ADDRESS)
						.addFilterField(AccountRequestQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS,electronicMailAddress)
						)));
	}
	
}
