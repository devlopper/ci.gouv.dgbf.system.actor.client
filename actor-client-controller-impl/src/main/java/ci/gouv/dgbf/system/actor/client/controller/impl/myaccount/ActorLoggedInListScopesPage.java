package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.impl.AbstractActorListScopesPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorLoggedInListScopesPage extends AbstractActorListScopesPage implements MyAccountTheme,Serializable {

	@Override
	protected Actor __getActor__() {
		return __inject__(ActorController.class).getLoggedIn();
	}
	
	protected Boolean __getIsStatic__() {
		return Boolean.TRUE;
	}
	
	@Override
	protected String getListOutcome() {
		return "actorLoggedInListScopesView";
	}
	
	@Override
	protected void addTabActorParameter(DefaultMenuItem item) {
		//no parameter actor for logged in user
	}
}