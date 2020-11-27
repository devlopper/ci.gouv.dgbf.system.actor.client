package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;

import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorAccountProfileEditPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountProfileEditPage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	private Form form;
	
	@Override
	protected void __listenAfterPostConstruct__() {
		super.__listenAfterPostConstruct__();
		form = buildForm(Form.FIELD_ENTITY,ActorAccountProfileEditPage.getActor(actor.getIdentifier()));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Modification de profile";
	}
	
	/**/
	
	public static Form buildForm(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		return ActorAccountProfileEditPage.buildForm(arguments);
	}

	public static Form buildForm(Object...objects) {
		return buildForm(MapHelper.instantiate(objects));
	}
	
	public static final String OUTCOME = "myAccountProfileEditView";
}