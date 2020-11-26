package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountProfilePage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = MyAccountProfileReadPage.buildLayout(Actor.class,actor);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Profile";
	}
}