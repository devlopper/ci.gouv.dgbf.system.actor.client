package ci.gouv.dgbf.system.actor.client.controller.api;
import java.util.Comparator;
import java.util.Map;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;

public class BudgetCategoryComparator implements Comparator<BudgetCategory> {

	@Override
	public int compare(BudgetCategory budgetCategory1, BudgetCategory budgetCategory2) {
		return ORDERS_INDEXES.get(budgetCategory1.getCode()).compareTo(ORDERS_INDEXES.get(budgetCategory2.getCode()));
	}
	
	public static final Map<String,Integer> ORDERS_INDEXES = Map.of(
			ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_GENERAL,0
			,ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_EPN,1
		);
}