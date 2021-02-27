package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;

public interface FunctionController extends ControllerEntity<Function> {

	default Collection<Function> readCreditManagersAuthorizingOfficersFinancialControllersAssistants() {
		List<Function> functions = (List<Function>) EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_BY_CODES_FOR_UI
				,FunctionQuerier.PARAMETER_NAME_CODES,List.of(
						ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT
						));
		if(CollectionHelper.isEmpty(functions))
			return null;
		Collections.sort(functions,new FunctionComparator());
		return functions;
	}
	
	default Collection<Function> readCreditManagersAuthorizingOfficers() {
		List<Function> functions = (List<Function>) EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_BY_CODES_FOR_UI
				,FunctionQuerier.PARAMETER_NAME_CODES,List.of(
						ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT
						));
		if(CollectionHelper.isEmpty(functions))
			return null;
		Collections.sort(functions,new FunctionComparator());
		return functions;
	}
	
	default Collection<Function> readCreditManagers() {
		List<Function> functions = (List<Function>) EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_BY_CODES_FOR_UI
				,FunctionQuerier.PARAMETER_NAME_CODES,List.of(
						ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT
						));
		if(CollectionHelper.isEmpty(functions))
			return null;
		Collections.sort(functions,new FunctionComparator());
		return functions;
	}
}