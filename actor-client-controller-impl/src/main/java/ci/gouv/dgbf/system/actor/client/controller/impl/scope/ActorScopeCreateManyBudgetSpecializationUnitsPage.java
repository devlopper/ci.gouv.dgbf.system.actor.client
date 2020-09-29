package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorScopeCreateManyBudgetSpecializationUnitsPage extends AbstractActorScopeCreateOrDeleteManyBudgetSpecializationUnitsPage implements Serializable {

	@Override
	protected String getActionIdentifier() {
		return ActorBusiness.CREATE_SCOPES;
	}
}