package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.NavigationCaseGetter;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.identification.PublicRequestReadPage;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestProcessPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	private ScopeFunctionSelectionController budgetaryScopeFunctionSelectionController;
	private RequestReadController readController;
	private String treatmentChoice;
	
	@Override
	protected void __listenPostConstruct__() {
		treatmentChoice = WebController.getInstance().getRequestParameter(PARAMETER_NAME_CHOICE);
		super.__listenPostConstruct__();
		readController = new RequestReadController(Boolean.FALSE,RequestEditPage.OUTCOME, OUTCOME,null,null,null,null);		
		SelectOneRadio treatmentChoiceSelectOneRadio = form.getInput(SelectOneRadio.class, Request.FIELD_TREATMENT);
		treatmentChoiceSelectOneRadio.enableChangeListener(List.of());
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.UPDATE;
	}
	
	@Override
	protected Form __buildForm__() {
		Request request = RequestEditPage.getRequestFromParameter(action, null);//TODO avoid duplicate read of request
		budgetaryScopeFunctionSelectionController = new ScopeFunctionSelectionController(request.getBudgetCategory());
		return buildForm(Form.FIELD_ACTION,action,PARAMETER_NAME_CHOICE,treatmentChoice,ScopeFunctionSelectionController.class,budgetaryScopeFunctionSelectionController);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Request request = RequestEditPage.getRequestFromParameter((Action) arguments.get(Form.FIELD_ACTION), null);//TODO avoid duplicate read of request
		request.setTreatment((String)arguments.get(PARAMETER_NAME_CHOICE));
		Collection<String> fieldsNames = new ArrayList<>();
		fieldsNames.addAll(List.of(Request.FIELD_TREATMENT));
		if(TREATMENT_CHOICE_ACCEPT.equals(request.getTreatment())) {
			fieldsNames.addAll(List.of(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS,Request.FIELD_ACCEPTATION_COMMENT));
		}else if(TREATMENT_CHOICE_REJECT.equals(request.getTreatment())) {
			fieldsNames.addAll(List.of(Request.FIELD_REJECTION_REASON));
		}
		MapHelper.writeByKeyDoNotOverride(arguments, Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments, Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, fieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request,(ScopeFunctionSelectionController) MapHelper.readByKey(arguments, ScopeFunctionSelectionController.class)));
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
		
		public FormListener(Request request,ScopeFunctionSelectionController scopeFunctionSelectionController) {
			super(request,scopeFunctionSelectionController);
		}
		
		@Override
		public void act(Form form) {
			String actionIdentifier;
			if(TREATMENT_CHOICE_ACCEPT.equals(form.getInput(SelectOneRadio.class, Request.FIELD_TREATMENT).getValue())) {
				actionIdentifier = RequestBusiness.ACCEPT;				
				writeBudgetariesScopeFunctionsIdentifiers();
				try {
					request.setSignatureSpecimenReadPageURL(NavigationCaseGetter.getInstance().get(PublicRequestReadPage.OUTCOME)
							.getRedirectURL(FacesContext.getCurrentInstance()).toString());
				} catch (MalformedURLException exception) {
					LogHelper.log(exception,RequestProcessPage.class);
				}
			}else
				actionIdentifier = RequestBusiness.REJECT;
			EntitySaver.getInstance().save(Request.class, new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setActionIdentifier(actionIdentifier)).addCreatablesOrUpdatables(request));
			Redirector.getInstance().redirect(RequestIndexPage.OUTCOME, null);
		}
		
		public Boolean isSubmitButtonShowable(Form form) {
			return StringHelper.isNotBlank(((Request)form.getEntity()).getTreatment());
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends RequestEditPage.FormConfiguratorListener {

		public FormConfiguratorListener(Request request) {
			super(request);
			blocksShowable = Boolean.FALSE;
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Request.FIELD_TREATMENT.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_CHOICES, TREATMENT_CHOICES);
				map.put(SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<String>() {
					@Override
					public void select(AbstractInputChoiceOne input, String treatment) {
						super.select(input, treatment);
						Redirector.getInstance().redirect(OUTCOME, Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of( ((Request)form.getEntity()).getIdentifier())
								,PARAMETER_NAME_CHOICE,List.of(treatment)));
					}
				});
			}else if(Request.FIELD_REJECTION_REASON.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Motif de rejet");
			}else if(Request.FIELD_ACCEPTATION_COMMENT.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Commentaire");
			}else if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS.equals(fieldName)) {
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.CODE_DEMANDE_POSTES_BUDGETAIRES.equals(request.getType().getCode())) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction(s) budgétaire(s) à accorder");
				}				
			}
			return map;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			//MapHelper.writeByKey(map,CommandButton.FIELD_VALUE, ((Request)form.getEntity()).getTreatment());
			MapHelper.writeByKey(map,CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	public static final String TREATMENT_CHOICE_ACCEPT = "Accepter la demande";
	public static final String TREATMENT_CHOICE_REJECT = "Rejeter la demande";
	public static final Collection<String> TREATMENT_CHOICES = List.of(TREATMENT_CHOICE_ACCEPT,TREATMENT_CHOICE_REJECT);
	
	public static final String OUTCOME = "requestProcessView";
	public static final String PARAMETER_NAME_CHOICE = "choice";
}