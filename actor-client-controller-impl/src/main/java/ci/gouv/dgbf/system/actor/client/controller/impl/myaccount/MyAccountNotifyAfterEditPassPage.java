package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountNotifyAfterEditPassPage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Modification de mon mot de passe";
	}
}