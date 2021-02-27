package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
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

@Getter @Setter
public abstract class AbstractRequestEditPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	protected Request request;
	
	@Override
	protected void __listenPostConstruct__() {
		if(action == null)
			action = WebController.getInstance().computeActionFromRequestParameter();
		request = getRequestFromParameter(action,null);
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
			request = getRequestFromParameter((Action) MapHelper.readByKey(arguments, Form.FIELD_ACTION),(String)MapHelper.readByKey(arguments, Actor.class));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request));
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	public static Request getRequestFromParameter(Action action,String actorIdentifier) {
		System.out.println("AbstractRequestEditPage.getRequestFromParameter() ---- Action : "+action);
		if(Action.CREATE.equals(action)) {
			String requestTypeIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class));
			if(StringHelper.isBlank(requestTypeIdentifier))
				return null;			
			Request request = null;
			if(StringHelper.isBlank(actorIdentifier)) {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_INSTANTIATE_ONE_BY_TYPE_IDENTIFIER
						, RequestQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,requestTypeIdentifier);
				request.setActor(__inject__(ActorController.class).getLoggedIn());
			}else {
				request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_INSTANTIATE_ONE_BY_TYPE_IDENTIFIER_BY_ACTOR_IDENTIFIER
						, RequestQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,requestTypeIdentifier
						, RequestQuerier.PARAMETER_NAME_ACTOR_IDENTIFIER,actorIdentifier
						);
			}
			return request;
		}else {
			Request request = EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI
					, RequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));
			if(request.getActOfAppointmentSignatureDateAsTimestamp() != null && request.getActOfAppointmentSignatureDate() == null)
				request.setActOfAppointmentSignatureDate(new Date(request.getActOfAppointmentSignatureDateAsTimestamp()));
			return request;
		}
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		protected Request request;
		
		public FormListener(Request request) {
			this.request = request;
		}
		
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		protected Request request;
		protected Map<String,IdentificationAttribute> fieldsNames;
		
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
			List<String> collection = new ArrayList<>(fieldsNames.keySet());
			collection.add(0, Request.FIELD_TYPE);
			return collection;
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			IdentificationAttribute attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
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
				map.put(AbstractInputChoice.FIELD_CHOICES
						,request == null ? EntityReader.getInstance().readMany(RequestType.class,RequestTypeQuerier.QUERY_IDENTIFIER_READ_ALL) : List.of(request.getType()));
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
		
		@Override
		protected Map<Integer, Cell> getLayoutArgumentsRowCellModel(Form form) {
			Map<Integer, Cell> map = super.getLayoutArgumentsRowCellModel(form);
			map.get(0).setWidth(3);
			map.get(1).setWidth(9);
			return map;
		}
	}
	
	/**/
	
	public static final String TREATMENT_CHOICE_ACCEPT = "Accepter la demande";
	public static final String TREATMENT_CHOICE_REJECT = "Rejeter la demande";
	public static final Collection<String> TREATMENT_CHOICES = List.of(TREATMENT_CHOICE_ACCEPT,TREATMENT_CHOICE_REJECT);
}