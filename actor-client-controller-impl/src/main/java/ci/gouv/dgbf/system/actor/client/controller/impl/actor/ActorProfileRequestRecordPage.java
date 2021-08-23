package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.api.ActorProfileRequestController;
import ci.gouv.dgbf.system.actor.client.controller.api.ProfileController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfileRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Named @ViewScoped
public class ActorProfileRequestRecordPage extends AbstractActorRequestRecordPage<ActorProfileRequest> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		postConstruct(form);
	}
	
	public static void postConstruct(Form form) {
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ActorProfileRequest.LABEL;
	}
	
	/**/
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.CREATE;
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action,ActorProfileRequestRecordPage.class,this);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, ActorProfileRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(ActorProfileRequest.FIELD_ACTORS
				,ActorProfileRequest.FIELD_PROFILES,ActorProfileRequest.FIELD_COMMENT));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener()
				.setPageContainerManaged(MapHelper.readByKey(arguments, ActorProfileRequestRecordPage.class)));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			ActorProfileRequest actorProfileRequest = (ActorProfileRequest) form.getEntity();
			__inject__(ActorProfileRequestController.class).record(actorProfileRequest);
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {

		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ActorProfileRequest.FIELD_PROFILES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.LABEL);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Profile.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Profile>() {
					@Override
					public Collection<Profile> complete(AutoComplete autoComplete) {
						return __inject__(ProfileController.class).readRequestable(null);
					}								
				});
			}else if(ActorScopeRequest.FIELD_ACTORS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.LABEL);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Actor.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Actor>() {
					@Override
					public Collection<Actor> complete(AutoComplete autoComplete) {
						return __inject__(ActorController.class).search(autoComplete.get__queryString__());
					}								
				});
			}else if(ActorScopeRequest.FIELD_COMMENT.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Commentaire");
				
			}
			return map;
		}
	}
	
	public static final String OUTCOME = "actorProfileRequestRecordView";
}