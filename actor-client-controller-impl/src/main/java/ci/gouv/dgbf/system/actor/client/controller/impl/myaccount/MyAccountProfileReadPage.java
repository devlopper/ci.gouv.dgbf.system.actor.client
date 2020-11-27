package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorAccountProfileReadPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountProfileReadPage extends AbstractLoggedInUserPage implements MyAccountTheme, Serializable {

	private Layout layout;
	
	@Override
	protected void __listenAfterPostConstruct__() {
		super.__listenAfterPostConstruct__();
		layout = buildLayout(Form.FIELD_ENTITY,actor == null ? null : ActorAccountProfileReadPage.getActor(actor.getIdentifier()));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Profile";
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = buildLayout();
	}
	
	/**/
	
	public static Layout buildLayout(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, ActorAccountProfileReadPage.FIELD_EDIT_OUTCOME, MyAccountProfileEditPage.OUTCOME);
		return ActorAccountProfileReadPage.buildLayout(arguments);
	}

	public static Layout buildLayout(Object...objects) {
		return buildLayout(MapHelper.instantiate(objects));
	}
}