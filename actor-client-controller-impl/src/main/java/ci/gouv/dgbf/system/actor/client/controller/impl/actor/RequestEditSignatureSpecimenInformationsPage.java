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
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestScopeFunctionQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestEditSignatureSpecimenInformationsPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		postConstruct(form);
	}
	
	public static void postConstruct(Form form) {
		RequestEditPage.postConstruct(form, null);		
	}

	@Override
	protected void setActionFromRequestParameter() {
		super.setActionFromRequestParameter();
		action = getAction(action);
	}
	
	public static Action getAction(Action action) {
		return ValueHelper.defaultToIfNull(action,Action.UPDATE);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return getWindowTitleValue(form,super.__getWindowTitleValue__());
	}
	
	public static String getWindowTitleValue(Form form,String default_) {
		return "Modification des informations à porter sur le spécimen de signature";
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action);
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		Request request = (Request) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(request == null) {
			request = getRequestFromParameter(Action.UPDATE,(String)MapHelper.readByKey(arguments, Actor.class));
		}
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request));
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	public static Request getRequestFromParameter(Action action,String actorIdentifier) {
		RequestScopeFunction requestScopeFunction = EntityReader.getInstance().readOne(RequestScopeFunction.class, RequestScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_REQUEST_EDIT
				, RequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue()));
		if(requestScopeFunction == null)
			throw new RuntimeException("Request Scope Function <<"+WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue())+">> has not been found");
		return EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_EDIT, RequestQuerier.PARAMETER_NAME_IDENTIFIER,requestScopeFunction.getRequestIdentifier());
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends RequestEditPage.FormListener {
		
		public FormListener(Request request) {
			super(request,null);
		}
		
		protected void writeBudgetariesScopeFunctionsIdentifiers() {
			//nothing to do
		}
		
		@Override
		protected String getRecordActionIdentifier() {
			return RequestBusiness.RECORD_SIGNATURE_SPECIMEN_INFORMATIONS;
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends RequestEditPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
			blocksShowable = Boolean.FALSE;
		}
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			List<String> collection = (List<String>) super.getFieldsNames(form);
			if(request.getType() != null 
					&& ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.IDENTIFIER_DEMANDE_POSTES_BUDGETAIRES.equals(request.getType().getIdentifier())) {
				collection.removeAll(List.of(Request.FIELD_ELECTRONIC_MAIL_ADDRESS,Request.FIELD_SECTION,Request.FIELD_ADMINISTRATIVE_UNIT,Request.FIELD_ADMINISTRATIVE_FUNCTION
						,Request.FIELD_ACT_OF_APPOINTMENT_REFERENCE,Request.FIELD_ACT_OF_APPOINTMENT_SIGNATORY,Request.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE
						,Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS));
			}
			return collection;
		}
	}
	
	/**/
	
	public static final String OUTCOME = "requestEditSignatureSpecimenInformationsView";	
}