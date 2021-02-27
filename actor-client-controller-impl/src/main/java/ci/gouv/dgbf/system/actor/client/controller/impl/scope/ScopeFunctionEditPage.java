package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionEditPage extends AbstractEntityEditPageContainerManagedImpl<ScopeFunction> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		ScopeFunction scopeFunction = (ScopeFunction) form.getEntity();
		SelectOneCombo functionSelectOneCombo = form.getInput(SelectOneCombo.class, ScopeFunction.FIELD_FUNCTION);
		SelectOneRadio scopeTypeSelectOneRadio = form.getInput(SelectOneRadio.class, ScopeFunction.FIELD_SCOPE_TYPE);
		AutoComplete scopeAutocomplete = form.getInput(AutoComplete.class, Action.UPDATE.equals(form.getAction()) ? ScopeFunction.FIELD_SCOPE : ScopeFunction.FIELD_SCOPES);
		
		functionSelectOneCombo.enableValueChangeListener(List.of(scopeTypeSelectOneRadio,scopeAutocomplete));
		functionSelectOneCombo.getAjaxes().get("valueChange").addUpdatablesUsingStyleClass(scopeAutocomplete.getOutputLabel());
		
		functionSelectOneCombo.setListener(new SelectOneCombo.Listener.AbstractImpl<Function>() {
			@Override
			public void select(AbstractInputChoiceOne input, Function function) {
				super.select(input, function);
				if(function == null) {
					
				}else {
					scopeTypeSelectOneRadio.setValue(null);
					scopeTypeSelectOneRadio.updateChoices();
					scopeTypeSelectOneRadio.selectFirstChoice();
					//scopeTypeSelectOneRadio.setRendered(CollectionHelper.getSize(function.getScopeTypes()) > 1);
					//scopeTypeSelectOneRadio.getOutputLabel().setRendered(scopeTypeSelectOneRadio.getRendered());
					
					scopeAutocomplete.setValue(null);
					if(CollectionHelper.isNotEmpty(function.getScopeTypes())) {
						scopeAutocomplete.getOutputLabel().setValue(function.getScopeTypes().stream().map(scopeType -> scopeType.getName()).collect(Collectors.joining("/")));
						scopeAutocomplete.setPlaceholder(scopeAutocomplete.getOutputLabel().getValue());
					}
				}				
			}
		});
		
		scopeTypeSelectOneRadio.enableChangeListener(List.of(scopeAutocomplete));
		scopeTypeSelectOneRadio.setListener(new SelectOneRadio.Listener.AbstractImpl<ScopeType>() {
			@Override
			public Collection<ScopeType> computeChoices(AbstractInputChoice<ScopeType> input) {
				Function function = (Function) functionSelectOneCombo.getValue();
				if(function == null)
					return null;
				return function.getScopeTypes();
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, ScopeType choice) {
				super.select(input, choice);
				scopeAutocomplete.setValue(null);
			}
		});
		
		scopeAutocomplete.setListener(new AutoComplete.Listener.AbstractImpl<Scope>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				//Function function = (Function) functionSelectOneCombo.getValue();
				ScopeType scopeType = (ScopeType) scopeTypeSelectOneRadio.getValue();
				return new Filter.Dto()
						.addField(ScopeQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__())
						.addField(ScopeQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__())
						//.addField(ScopeQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER, function == null ? "" : function.getIdentifier())
						.addField(ScopeQuerier.PARAMETER_NAME_TYPE_IDENTIFIER, scopeType == null ? "" : scopeType.getIdentifier())
						;
			}
		});
		if(scopeFunction.getFunction() == null)
			functionSelectOneCombo.selectFirstChoice();
		else
			functionSelectOneCombo.selectBySystemIdentifier(scopeFunction.getFunction().getIdentifier());		
		scopeAutocomplete.setValue(scopeFunction.getScope());
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm();
	}

	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, ScopeFunction.class);
		Action action = (Action) MapHelper.readByKey(map, Form.FIELD_ACTION);
		if(action == null)
			action = ValueHelper.defaultToIfNull(WebController.getInstance().getRequestParameterAction(), Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, action);
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
		@Override
		public void act(Form form) {
			ScopeFunction scopeFunction = (ScopeFunction) form.getEntity();
			//scopeFunction.set__auditWho__(SessionHelper.getUserName());
			//scopeFunction.setShared(AbstractInputChoice.equalsChoiceYes(scopeFunction.getSharedAsString()));
			if(Action.CREATE.equals(form.getAction()) || Action.UPDATE.equals(form.getAction())) {
				Collection<ScopeFunction> scopeFunctions = Action.CREATE.equals(form.getAction()) 
						? scopeFunction.getScopes().stream().map(scope -> new ScopeFunction().setScope(scope).setFunction(scopeFunction.getFunction())
								.setShared(scopeFunction.getShared())).collect(Collectors.toList()) 
								: List.of(scopeFunction);
				Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(scopeFunctions);
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ScopeFunctionBusiness.SAVE));
				EntitySaver.getInstance().save(ScopeFunction.class, arguments);
			}else
				super.act(form);
		}
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.TRUE;
		}
	}

	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> collection = new ArrayList<>();
			if(Action.UPDATE.equals(form.getAction())) {
				collection.add(ScopeFunction.FIELD_CODE);
				collection.add(ScopeFunction.FIELD_NAME);
			}
			collection.addAll(CollectionHelper.listOf(ScopeFunction.FIELD_FUNCTION,ScopeFunction.FIELD_SCOPE_TYPE
					,Action.UPDATE.equals(form.getAction()) ? ScopeFunction.FIELD_SCOPE : ScopeFunction.FIELD_SCOPES
					,ScopeFunction.FIELD_SHARED));
			return collection;
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ScopeFunction.FIELD_FUNCTION.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction");
				map.put(AbstractInputChoice.FIELD_CHOICES, EntityReader.getInstance().readMany(Function.class
						, FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATED_TO_SCOPE_TYPE_FOR_UI_CREATE_SCOPE_FUNCTION));
			}else if(ScopeFunction.FIELD_SCOPE.equals(fieldName) || ScopeFunction.FIELD_SCOPES.equals(fieldName)) {
				map.put(AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Domaine");
				map.put(AutoComplete.FIELD_ENTITY_CLASS, Scope.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				//map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, ScopeQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_AND_NOT_ASSOCIATED_TO_FUNCTION_BY_TYPE_IDENTIFIER);
				map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, ScopeQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_BY_TYPE_IDENTIFIER);
				map.put(AutoComplete.FIELD_MULTIPLE, Action.CREATE.equals(form.getAction()));
			}else if(ScopeFunction.FIELD_SHARED.equals(fieldName)) {
				map.put(AbstractInputChoice.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Partageable par plusieurs acteurs ?");
			}else if(ScopeFunction.FIELD_SCOPE_TYPE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Type de domaine");
				map.put(AbstractInputChoice.FIELD_COLUMNS,4);
				
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

	/**/
	
	
}