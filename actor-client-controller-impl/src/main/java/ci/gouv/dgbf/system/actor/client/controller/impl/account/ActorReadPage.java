package ci.gouv.dgbf.system.actor.client.controller.impl.account;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.WebController;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorReadPage extends AbstractActorReadPage implements Serializable {

	private Profile profile;
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntityAsParent(Actor.class);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(actor == null)
			return super.__getWindowTitleValue__();
		return "Compte utilisateur de "+actor.getCode()+" : "+actor.getNames();
	}
}