package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeTypeFunction;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeTypeFunctionEditPage extends AbstractEntityEditPageContainerManagedImpl<ScopeTypeFunction> implements Serializable {

	@Override
	protected Form __buildForm__() {		
		return buildForm();
	}

	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ENTITY_CLASS, ScopeTypeFunction.class);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, ValueHelper.defaultToIfNull(WebController.getInstance().getRequestParameterAction(), Action.CREATE));
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());		
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		Form form = Form.build(map);
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	/**/

	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		
	}

	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			return CollectionHelper.listOf(ScopeTypeFunction.FIELD_SCOPE_TYPE,ScopeTypeFunction.FIELD_FUNCTION,ScopeTypeFunction.FIELD_SCOPE_FUNCTION_DERIVABLE);
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ScopeTypeFunction.FIELD_SCOPE_TYPE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Type de domaine");
				map.put(SelectOneCombo.FIELD_CHOICES, EntityReader.getInstance().readMany(ScopeType.class,ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING));
			}else if(ScopeTypeFunction.FIELD_FUNCTION.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction");
				map.put(SelectOneCombo.FIELD_CHOICES, EntityReader.getInstance().readMany(Function.class,FunctionQuerier.QUERY_IDENTIFIER_READ));
			}else if(ScopeTypeFunction.FIELD_SCOPE_FUNCTION_DERIVABLE.equals(fieldName)) {
				map.put(AbstractInputChoice.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Poste dérivable ?");
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
}