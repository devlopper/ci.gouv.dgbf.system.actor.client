package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.api.ProfileController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ProfileEditPage extends AbstractEntityEditPageContainerManagedImpl<Profile> implements Serializable {

	private ProfileType profileType;
	
	@Override
	protected void __listenPostConstruct__() {
		profileType = WebController.getInstance().getRequestParameterEntityAsParent(ProfileType.class);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Profile.FIELD_TYPE_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.stringify(ProfileType.class)));
	}

	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, Profile.class);
		Action action = (Action) MapHelper.readByKey(map, Form.FIELD_ACTION);
		if(action == null)
			action = ValueHelper.defaultToIfNull(WebController.getInstance().getRequestParameterAction(), Action.CREATE);
		Profile profile = (Profile) MapHelper.readByKey(map, Form.FIELD_ENTITY);
		if(profile == null)
			MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ENTITY, profile = __inject__(ProfileController.class)
				.prepareEdit(WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER), (String) MapHelper.readByKey(map, Profile.FIELD_TYPE_IDENTIFIER)));
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, action);
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());	
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(Profile.FIELD_TYPE,Profile.FIELD_CODE
				,Profile.FIELD_NAME,Profile.FIELD_ORDER_NUMBER,Profile.FIELD_REQUESTABLE));			
		Form form = Form.build(map);
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Enregistrement de profile";
	}
	
	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		@Override
		public void act(Form form) {
			Profile profile = (Profile) form.getEntity();
			if(profile.getType() != null)
				profile.setTypeIdentifier(profile.getType().getIdentifier());
			Arguments<Profile> arguments = new Arguments<Profile>().addCreatablesOrUpdatables(profile);
			arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ProfileBusiness.SAVE));
			EntitySaver.getInstance().save(Profile.class, arguments);
		}
	}
	
	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Profile.FIELD_CODE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Code");
				
			}else if(Profile.FIELD_NAME.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Libellé");
				
			}else if(Profile.FIELD_TYPE.equals(fieldName)) {
				
			}else if(Profile.FIELD_ORDER_NUMBER.equals(fieldName)) {
				
			}else if(Profile.FIELD_REQUESTABLE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Demandable");
				
			}
			return map;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	public static final String OUTCOME = "profileEditView";
}