package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribut;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestEditPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	//private RequestType requestType;
	private Request request;
	private Map<String,IdentificationAttribut> fieldsNames;
	
	@Override
	protected void __listenPostConstruct__() {
		action = WebController.getInstance().getRequestParameterAction();
		if(action == null)
			action = Action.CREATE;
		if(Action.CREATE.equals(action)) {
			String requestTypeIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class));
			if(StringHelper.isBlank(requestTypeIdentifier)) {
			
			}else {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_INSTANTIATE_ONE_BY_TYPE_IDENTIFIER
						, RequestQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,requestTypeIdentifier);
				if(request != null)
					request.setActor(__inject__(ActorController.class).getLoggedIn());
			}	
		}else {
			request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI
					, RequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));
		}	
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {
		Form form = buildForm(Form.FIELD_ACTION,action,Form.FIELD_ENTITY,request,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request)
				,Form.FIELD_LISTENER, new FormListener().setRequest(request));
		if(request == null) {
			SelectOneCombo selectOneCombo = form.getInput(SelectOneCombo.class, Request.FIELD_TYPE);
			selectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
				@Override
				protected void select(AbstractAction action, Object value) {
					RequestType type = null;
					if(value instanceof RequestType)
						type = (RequestType) value;
					if(type == null || StringHelper.isBlank(type.getIdentifier()))
						return;
					Redirector.getInstance().redirect("requestEditView", Map.of(
						ParameterName.ACTION_IDENTIFIER.getValue(),List.of(Action.CREATE.name())
						,ParameterName.stringify(RequestType.class),List.of(type.getIdentifier())
						,"windowrendertype",List.of(ValueHelper.defaultToIfBlank(WebController.getInstance().getRequestParameter("windowrendertype"),"windowrendertypenormal"))
					));
				}
			}, null);
		}
		return form;
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		if(request == null)
			return "Nouvelle demande";
		if(request.getType().getForm() == null)
			return "Nouvelle demande de type "+request.getType().getName();
		return request.getType().getForm().getName();
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class FormListener extends Form.Listener.AbstractImpl {
		private Request request;
		
		@Override
		public void act(Form form) {
			super.act(form);
		}
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return request == null ? Boolean.FALSE : super.isSubmitButtonShowable(form);
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		private Request request;
		private Map<String,IdentificationAttribut> fieldsNames;
		
		public FormConfiguratorListener(Request request) {
			this.request = request;
			if(request != null && request.getType() != null && request.getType().getForm() != null) {
				fieldsNames = IdentificationForm.computeFieldsNames(request.getType().getForm(), Request.class);
			}
		}
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			if(request == null)
				return List.of(Request.FIELD_TYPE);
			if(fieldsNames == null)
				return null;
			return fieldsNames.keySet();
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			IdentificationAttribut attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
			if(attribut != null) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, attribut.getName());
				map.put(AbstractInput.FIELD_REQUIRED, attribut.getRequired());
			}
			if(Request.FIELD_ACTOR.equals(fieldName)) {
				
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				
			}else if(Request.FIELD_TYPE.equals(fieldName)) {
				Collection<RequestType> choices = new ArrayList<>();
				RequestType type = new RequestType();
				type.setName("-- Aucune sélection --");
				choices.add(type);
				Collection<RequestType> types = EntityReader.getInstance().readMany(RequestType.class,RequestTypeQuerier.QUERY_IDENTIFIER_READ_ALL);
				if(CollectionHelper.isNotEmpty(types))
					choices.addAll(types);				
				map.put(AbstractInputChoice.FIELD_CHOICES,choices);
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_BUDGET_SPECIALIZATION_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, BudgetSpecializationUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_SECTION.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, Section.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(Request.FIELD_ADMINISTRATIVE_FUNCTION.equals(fieldName)) {
				
			}
			return map;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKeyDoNotOverride(map,CommandButton.FIELD_VALUE, "Créer");
			return map;
		}
	}
	
}