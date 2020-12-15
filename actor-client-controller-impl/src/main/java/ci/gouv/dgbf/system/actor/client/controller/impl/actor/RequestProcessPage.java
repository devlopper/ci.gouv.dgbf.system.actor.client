package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.Calendar;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestProcessPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	@Override
	protected void setActionFromRequestParameter() {
		action = Action.UPDATE;
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION,action);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Request request = RequestEditPage.getRequestFromParameter((Action) arguments.get(Form.FIELD_ACTION), null);
		MapHelper.writeByKeyDoNotOverride(arguments, Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request));
		Form form = RequestEditPage.buildForm(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Traitement de demande";
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends RequestEditPage.FormListener {
		
		public FormListener(Request request) {
			super(request);
		}
		
		@Override
		public void act(Form form) {
			String actionIdentifier;
			if(TREATMENT_CHOICE_ACCEPT.equals(form.getInput(SelectOneRadio.class, Request.FIELD_TREATMENT).getValue()))
				actionIdentifier = RequestBusiness.ACCEPT;
			else
				actionIdentifier = RequestBusiness.REJECT;
			EntitySaver.getInstance().save(Request.class, new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setActionIdentifier(actionIdentifier)).addCreatablesOrUpdatables(request));		
		}
		
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.TRUE;
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends RequestEditPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
		}
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> fieldsNames = super.getFieldsNames(form);
			if(CollectionHelper.isEmpty(fieldsNames))
				return null;
			fieldsNames.addAll(List.of(Request.FIELD_TREATMENT,Request.FIELD_REJECTION_REASON));
			return fieldsNames;
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Request.FIELD_TREATMENT.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICES, TREATMENT_CHOICES);
			}else if(Request.FIELD_REJECTION_REASON.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Motif de rejet");
			}else if(Request.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE.equals(fieldName)) {
				map.put(Calendar.FIELD_SHOW_ON, ConstantEmpty.STRING);
			}
			return map;
		}
		
		@Override
		public AbstractInput<?> buildInput(Form form, String fieldName) {
			AbstractInput<?> input = super.buildInput(form, fieldName);
			if(!Request.FIELD_TREATMENT.equals(fieldName) && !Request.FIELD_REJECTION_REASON.equals(fieldName)) {
				input.setReadOnly(Boolean.TRUE);
			}			
			return input;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKey(map,CommandButton.FIELD_VALUE, "Traiter");
			return map;
		}
	}
	
	public static final String TREATMENT_CHOICE_ACCEPT = "Accepter la demande";
	public static final String TREATMENT_CHOICE_REJECT = "Rejeter la demande";
	public static final Collection<String> TREATMENT_CHOICES = List.of(TREATMENT_CHOICE_ACCEPT,TREATMENT_CHOICE_REJECT);
	
	public static final String OUTCOME = "requestProcessView";
}