package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionAssistantEditPage extends AbstractEntityEditPageContainerManagedImpl<ScopeFunction> implements Serializable {

	private ScopeFunction parent;
	private Function assistantFunction;
	
	@Override
	protected void __listenPostConstruct__() {
		parent = WebController.getInstance().getRequestParameterEntityAsParent(ScopeFunction.class);
		assistantFunction = EntityReader.getInstance().readOne(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_BY_CODE_FOR_UI
				,FunctionQuerier.PARAMETER_NAME_CODE, ci.gouv.dgbf.system.actor.server.persistence.entities.Function.formatAssistantCode(parent.getFunction().getCode()));
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION,ValueHelper.defaultToIfNull(WebController.getInstance().computeActionFromRequestParameter(),Action.CREATE)
				,"parent",parent,"assistantFunction",assistantFunction);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(parent == null)
			return super.__getWindowTitleValue__();
		if(Action.CREATE.equals(action))
			return "Création "+assistantFunction+" de "+parent;
		return (Action.UPDATE.equals(action) ? "Modification" : "Suppresion")+" de "+form.getEntity();
	}
	
	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		if(Action.CREATE.equals(MapHelper.readByKey(map, Form.FIELD_ACTION))) {
			ScopeFunction scopeFunction = (ScopeFunction) MapHelper.readByKey(map, Form.FIELD_ENTITY);
			ScopeFunction parent = (ScopeFunction) MapHelper.readByKey(map, "parent");
			Function assistantFunction = (Function) MapHelper.readByKey(map, "assistantFunction");
			if(scopeFunction == null) {
				scopeFunction = new ScopeFunction();
				scopeFunction.setParentIdentifier(parent.getIdentifier());
				map.put(Form.FIELD_ENTITY, scopeFunction);
			}
			scopeFunction.setScope(parent.getScope());
			scopeFunction.setFunction(assistantFunction);
		}
		
		
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(ScopeFunction.FIELD_CODE,ScopeFunction.FIELD_NAME));
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		Form form = ScopeFunctionEditPage.buildForm(map);
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	/**/

	public static class FormListenerImpl extends ScopeFunctionEditPage.FormListenerImpl implements Serializable {
		@Override
		public void act(Form form) {
			Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables((ScopeFunction)form.getEntity());
			arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ScopeFunctionBusiness.SAVE));
			EntitySaver.getInstance().save(ScopeFunction.class, arguments);
		}
	}

	public static class FormConfiguratorListenerImpl extends ScopeFunctionEditPage.FormConfiguratorListenerImpl implements Serializable {
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ScopeFunction.FIELD_CODE.equals(fieldName)) {
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.FALSE);
			}else if(ScopeFunction.FIELD_NAME.equals(fieldName)) {
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.FALSE);
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
	
	public static final String OUTCOME = "scopeFunctionAssistantEditView";
}