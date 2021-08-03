package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import lombok.Setter;

import lombok.Getter;

@Getter @Setter @Named @ViewScoped
public class ActorScopeCreatePage extends AbstractActorScopeCreateOrDeletePage implements Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Assigner des domaines";
	}
	
	@Override
	protected String getActionIdentifier() {
		return ActorScopeBusiness.VISIBLE;
	}

	public static final String OUTCOME = "actorScopeCreateView";
}