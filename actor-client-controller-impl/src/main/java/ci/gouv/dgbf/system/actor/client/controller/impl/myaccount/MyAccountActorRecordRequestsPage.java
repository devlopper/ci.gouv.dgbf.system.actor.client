package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorRecordRequestsPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Named @ViewScoped
public class MyAccountActorRecordRequestsPage extends AbstractEntityEditPageContainerManagedImpl<Actor> implements MyAccountTheme,Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		ActorRecordRequestsPage.postConstruct(form);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return ActorRecordRequestsPage.getWindowTitle();
	}
	
	/**/
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.CREATE;
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action,MyAccountActorRecordRequestsPage.class,this);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(Actor.FIELD_PROFILES,Actor.FIELD_SCOPE_TYPE,Actor.FIELD_SCOPES
				,Actor.FIELD_COMMENT));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener()
				.setPageContainerManaged(MapHelper.readByKey(arguments, MyAccountActorRecordRequestsPage.class)));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = ActorRecordRequestsPage.buildForm(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends ActorRecordRequestsPage.FormListener implements Serializable {
		@Override
		public void act(Form form) {
			((Actor)form.getEntity()).setActors(CollectionHelper.listOf(Boolean.TRUE,__inject__(ActorController.class).getLoggedIn()));
			super.act(form);
		}
		
		@Override
		public void redirect(Form form, Object request) {
			Redirector.getInstance().redirect(new Redirector.Arguments().outcome(MyAccountIndexPage.OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, MyAccountIndexPage.TAB_MY_REQUESTS));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends ActorRecordRequestsPage.FormConfiguratorListener implements Serializable {

	}
	
	public static final String OUTCOME = "myAccountActorRecordRequestsView";
}