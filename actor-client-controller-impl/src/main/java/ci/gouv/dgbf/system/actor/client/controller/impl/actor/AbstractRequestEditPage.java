package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribut;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.user.RequestInitializePage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
public abstract class AbstractRequestEditPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	protected Request request;
	
	@Override
	protected void __listenPostConstruct__() {
		if(action == null)
			action = WebController.getInstance().computeActionFromRequestParameter();
		request = getRequestFromParameter(action);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {		
		Form form = buildForm(Form.FIELD_ACTION,action,Form.FIELD_ENTITY,request);
		return form;
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return request.getType().getForm().getName();
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Request request = (Request) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(request == null)
			request = getRequestFromParameter((Action) MapHelper.readByKey(arguments, Form.FIELD_ACTION));		
		Class<?> pageClass = ValueHelper.defaultToIfBlank((Class<?>) MapHelper.readByKey(arguments, RequestInitializePage.class),RequestInitializePage.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request,pageClass));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request,pageClass));
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	public static Request getRequestFromParameter(Action action) {
		if(Action.CREATE.equals(action)) {
			String requestTypeIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class));
			if(StringHelper.isBlank(requestTypeIdentifier))
				return null;			
			Request request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_INSTANTIATE_ONE_BY_TYPE_IDENTIFIER
						, RequestQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,requestTypeIdentifier);
			request.setActor(__inject__(ActorController.class).getLoggedIn());
			return request;
		}else
			return EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI
					, RequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		protected Request request;
		protected Class<?> pageClass;
		
		public FormListener(Request request,Class<?> pageClass) {
			this.request = request;
		}
		
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		protected Request request;
		protected Map<String,IdentificationAttribut> fieldsNames;
		protected Class<?> pageClass;
		
		public FormConfiguratorListener(Request request,Class<?> pageClass) {
			this.request = request;
			this.pageClass = pageClass;
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
				
			}else if(Request.FIELD_ACTOR_CODE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom d'utilisateur");
			}else if(Request.FIELD_ACTOR_NAMES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom et prénoms");
			}else if(Request.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Type");
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				
			}else if(Request.FIELD_TYPE.equals(fieldName)) {
				Collection<RequestType> choices = new ArrayList<>();
				//RequestType type = new RequestType();
				//type.setName("-- Aucune sélection --");
				//choices.add(type);
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
				
			}else if(Request.FIELD_TREATMENT.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICES, TREATMENT_CHOICES);
			}else if(Request.FIELD_REJECTION_REASON.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Motif de rejet");
			}
			return map;
		}
		
		/*@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			String value;
			if(RequestProcessPage.class.equals(pageClass))
				value = "Traiter";
			else if(RequestInitializePage.class.equals(pageClass))
				value = request == null ? "Choisir" : "Créer";
			else
				value = "Exécuter";
			MapHelper.writeByKeyDoNotOverride(map,CommandButton.FIELD_VALUE, value);
			return map;
		}*/
	}
	
	/**/
	
	public static final String TREATMENT_CHOICE_ACCEPT = "Accepter la demande";
	public static final String TREATMENT_CHOICE_REJECT = "Rejeter la demande";
	public static final Collection<String> TREATMENT_CHOICES = List.of(TREATMENT_CHOICE_ACCEPT,TREATMENT_CHOICE_REJECT);
}