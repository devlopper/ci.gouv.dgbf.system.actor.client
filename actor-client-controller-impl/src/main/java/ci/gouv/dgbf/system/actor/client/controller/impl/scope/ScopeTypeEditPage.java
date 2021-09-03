package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

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

import ci.gouv.dgbf.system.actor.client.controller.api.ScopeTypeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeTypeBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeTypeEditPage extends AbstractEntityEditPageContainerManagedImpl<ScopeType> implements Serializable {

	@Override
	protected Form __buildForm__() {		
		return buildForm();
	}
	
	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, ScopeType.class);
		Action action = (Action) MapHelper.readByKey(map, Form.FIELD_ACTION);
		if(action == null)
			action = ValueHelper.defaultToIfNull(WebController.getInstance().getRequestParameterAction(), Action.CREATE);
		ScopeType scopeType = (ScopeType) MapHelper.readByKey(map, Form.FIELD_ENTITY);
		if(scopeType == null)
			MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ENTITY, scopeType = __inject__(ScopeTypeController.class)
				.prepareEdit(WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER)));
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, action);
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());	
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(ScopeType.FIELD_CODE
				,ScopeType.FIELD_NAME,ScopeType.FIELD_ORDER_NUMBER,ScopeType.FIELD_REQUESTABLE));			
		Form form = Form.build(map);
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Enregistrement "+ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.LABEL;
	}
	
	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		@Override
		public void act(Form form) {
			ScopeType scopeType = (ScopeType) form.getEntity();
			Arguments<ScopeType> arguments = new Arguments<ScopeType>().addCreatablesOrUpdatables(scopeType);
			arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ScopeTypeBusiness.SAVE));
			EntitySaver.getInstance().save(ScopeType.class, arguments);
		}
	}
	
	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ScopeType.FIELD_CODE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Code");
				
			}else if(ScopeType.FIELD_NAME.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Libellé");
				
			}else if(ScopeType.FIELD_ORDER_NUMBER.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Numéro d'ordre");
				
			}else if(ScopeType.FIELD_REQUESTABLE.equals(fieldName)) {
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
	
	public static final String OUTCOME = "scopeTypeEditView";
}