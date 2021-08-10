package ci.gouv.dgbf.system.actor.client.controller.impl.account;

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
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorScopeRequestRecordPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Named @ViewScoped
public class LoggedInUserActorScopeRequestRecordPage extends AbstractEntityEditPageContainerManagedImpl<ActorScopeRequest> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		ActorScopeRequestRecordPage.postConstruct(form);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Demande de domaines";
	}
	
	/**/
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.CREATE;
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action,LoggedInUserActorScopeRequestRecordPage.class,this);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(ActorScopeRequest.FIELD_SCOPE_TYPE
				,ActorScopeRequest.FIELD_SCOPES,ActorScopeRequest.FIELD_COMMENT));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener()
				.setPageContainerManaged(MapHelper.readByKey(arguments, LoggedInUserActorScopeRequestRecordPage.class)));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = ActorScopeRequestRecordPage.buildForm(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends ActorScopeRequestRecordPage.FormListener implements Serializable {
		public void act(Form form) {
			((ActorScopeRequest)form.getEntity()).setActors(CollectionHelper.listOf(Boolean.TRUE,__inject__(ActorController.class).getLoggedIn()));
			super.act(form);
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends ActorScopeRequestRecordPage.FormConfiguratorListener implements Serializable {

	}
	
	public static final String OUTCOME = "loggedInUserActorScopeRequestRecordView";
}