package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorScopeCreateOrDeleteManyBudgetSpecializationUnitsPage extends AbstractActorScopeCreateOrDeleteManyPage<BudgetSpecializationUnit> implements Serializable {

	@Override
	protected Class<BudgetSpecializationUnit> getScopeClass() {
		return BudgetSpecializationUnit.class;
	}

	@Override
	protected String getScopeTypeCode() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_USB;
	}
}