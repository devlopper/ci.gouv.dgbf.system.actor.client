package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;

import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorScopeCreateOrDeleteManySectionsPage extends AbstractActorScopeCreateOrDeleteManyPage<Section> implements Serializable {

	@Override
	protected Class<Section> getScopeClass() {
		return Section.class;
	}

	@Override
	protected String getScopeTypeCode() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION;
	}
}