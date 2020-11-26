package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribut;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.AbstractRequestEditPage;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestInitializePage extends AbstractRequestEditPage implements MyAccountTheme,Serializable {

	@Override
	protected void setActionFromRequestParameter() {
		action = Action.CREATE;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(request == null)
			return "Nouvelle demande";
		return super.__getWindowTitleValue__();
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(request);
	}
	
	public static Form buildForm(Request request) {
		Form form = buildForm(Form.FIELD_ACTION,Action.CREATE,Form.FIELD_ENTITY,request,Form.FIELD_LISTENER,new FormListener(request)
				,Form.ConfiguratorImpl.FIELD_LISTENER,new FormConfiguratorListener(request));
		return form;
	}
	
	public static Form buildForm() {
		return buildForm(getRequestFromParameter(Action.CREATE));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractRequestEditPage.FormListener {
		
		public FormListener(Request request) {
			super(request);
		}
		
		@Override
		public void act(Form form) {
			if(request == null) {
				
			}else {
				if(request.getActOfAppointmentSignatureDate() == null)
					request.setActOfAppointmentSignatureDateAsTimestamp(null);
				else
					request.setActOfAppointmentSignatureDateAsTimestamp(request.getActOfAppointmentSignatureDate().getTime());
				EntitySaver.getInstance().save(Request.class, new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setActionIdentifier(RequestBusiness.INITIALIZE)).addCreatablesOrUpdatables(request));
			}		
		}
		
		@Override
		public void redirect(Form form, Object request) {
			if(this.request == null) {
				RequestType type = (RequestType) form.getInput(SelectOneCombo.class, Request.FIELD_TYPE).getValue();
				if(type == null || StringHelper.isBlank(type.getIdentifier()))
					return;
				Map<String,List<String>> map = new LinkedHashMap<>();
				map.put(ParameterName.ACTION_IDENTIFIER.getValue(),List.of(Action.CREATE.name()));
				map.put(ParameterName.stringify(RequestType.class),List.of(type.getIdentifier()));
				map.put(TabMenu.Tab.PARAMETER_NAME, List.of(UserRequestsPage.TAB_CREATE));
				Redirector.getInstance().redirect(UserRequestsPage.OUTCOME,map);
			}else {
				Redirector.getInstance().redirect(new Redirector.Arguments().setOutcome(UserRequestsPage.OUTCOME));
			}
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractRequestEditPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
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
				map.put(SelectBooleanButton.FIELD_OFF_LABEL, "Rejeter la demdande");
				map.put(SelectBooleanButton.FIELD_ON_LABEL, "Accepter la demdande");
			}else if(Request.FIELD_REJECTION_REASON.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Motif de rejet");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_REFERENCE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Référence de l'acte de nomination");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Date de signature de l'acte de nomination");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_SIGNATORY.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Signataire de l'acte de nomination");
			}
			return map;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKey(map,CommandButton.FIELD_VALUE, request == null ? "Choisir" : "Créer");
			return map;
		}
	}
	
	public static final String OUTCOME = "requestInitializeView";
}