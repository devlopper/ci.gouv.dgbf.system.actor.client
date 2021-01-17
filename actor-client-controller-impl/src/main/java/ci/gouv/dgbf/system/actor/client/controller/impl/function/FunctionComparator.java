package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.util.Comparator;
import java.util.Map;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;

public class FunctionComparator implements Comparator<Function> {

	@Override
	public int compare(Function function1, Function function2) {
		return ORDERS_INDEXES.get(function1.getCode()).compareTo(ORDERS_INDEXES.get(function2.getCode()));
	}
	
	public static final Map<String,Integer> ORDERS_INDEXES = Map.of(
			ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER,0
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT,1
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER,2
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT,3
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER,4
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT,5
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER,6
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT,7
		);
}
