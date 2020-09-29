package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorScopeCreateOrDeleteManyActivityCategoriesPage extends AbstractActorScopeCreateOrDeleteManyPage<ActivityCategory> implements Serializable {

	@Override
	protected Class<ActivityCategory> getScopeClass() {
		return ActivityCategory.class;
	}

	@Override
	protected String getScopeTypeCode() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_CATEGORIE_ACTIVITE;
	}
}