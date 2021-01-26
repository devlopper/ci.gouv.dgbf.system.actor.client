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
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputLabel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import ci.gouv.dgbf.system.actor.server.business.api.RequestDispatchSlipBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestStatusQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipEditPage extends AbstractEntityEditPageContainerManagedImpl<RequestDispatchSlip> implements Serializable {
	
	private RequestsSelectionController requestsSelectionController;
	private String requestsListIdentifier = RandomHelper.getAlphabetic(4);
	private SelectOneCombo functionSelectOne;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		functionSelectOne = form.getInput(SelectOneCombo.class, RequestDispatchSlip.FIELD_FUNCTION);
		functionSelectOne.enableValueChangeListener(List.of());
	}
	
	@Override
	protected Form __buildForm__() {
		RequestStatus requestStatus = EntityReader.getInstance().readOne(RequestStatus.class, RequestStatusQuerier.QUERY_IDENTIFIER_READ_BY_CODE_FOR_UI
				, RequestStatusQuerier.PARAMETER_NAME_CODE,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.CODE_SUBMITTED);
		requestsSelectionController = new RequestsSelectionController(requestStatus);
		return buildForm(Form.FIELD_ACTION,action,RequestsSelectionController.class,requestsSelectionController);
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		super.setActionFromRequestParameter();
		action = getAction(action);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(Action.CREATE.equals(action))
			return "Nouveau bordereau de demandes";
		if(Action.UPDATE.equals(action))
			return "Modification du bordereau de demandes N° "+((RequestDispatchSlip)form.getEntity()).getCode();
		return super.__getWindowTitleValue__();
	}
	
	public static Action getAction(Action action) {
		return ValueHelper.defaultToIfNull(action,Action.CREATE);
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, RequestDispatchSlip.class);
		RequestsSelectionController requestsSelectionController = (RequestsSelectionController) MapHelper.readByKey(arguments, RequestsSelectionController.class);
		//requestDispatchSlip.setRequests(selectedRequests);
		Action action = (Action) MapHelper.readByKey(arguments, Form.FIELD_ACTION);
		RequestDispatchSlip requestDispatchSlip = (RequestDispatchSlip) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(Action.CREATE.equals(action)) {
			if(requestDispatchSlip == null) {
				requestDispatchSlip = new RequestDispatchSlip();
				MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY,requestDispatchSlip);
			}
		}else if(Action.UPDATE.equals(action)) {
			if(requestDispatchSlip == null) {
				requestDispatchSlip = EntityReader.getInstance().readOne(RequestDispatchSlip.class,RequestDispatchSlipQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_EDIT
					,RequestDispatchSlipQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
				MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY,requestDispatchSlip);
			}
			requestsSelectionController.setSelected(requestDispatchSlip.getRequests());
		}
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(RequestDispatchSlip.FIELD_FUNCTION
				,RequestDispatchSlip.FIELD_REQUESTS));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener().setRequestsSelectionController(requestsSelectionController));
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		private RequestsSelectionController requestsSelectionController;
		@Override
		public void act(Form form) {
			String actionIdentifier = getRepresentationActionIdentifier();
			RequestDispatchSlip requestDispatchSlip = (RequestDispatchSlip) form.getEntity();
			if(requestsSelectionController != null)
				requestDispatchSlip.setRequests(requestsSelectionController.getSelected());
			requestDispatchSlip.writeIdentifiers(actionIdentifier);
			Arguments<RequestDispatchSlip> arguments = new Arguments<RequestDispatchSlip>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
					.setActionIdentifier(actionIdentifier)).addCreatablesOrUpdatables(requestDispatchSlip);
			EntitySaver.getInstance().save(RequestDispatchSlip.class,arguments);
			
			List<Object> v = arguments.get__response__().getHeaders().get(Request.FIELD_IDENTIFIER);
			if(CollectionHelper.isNotEmpty(v)) {
				String identifier = (String) v.get(0);
				requestDispatchSlip.setIdentifier(identifier);
			}
		}
		
		protected String getRepresentationActionIdentifier() {
			return RequestDispatchSlipBusiness.RECORD;
		}
		
		@Override
		public void redirect(Form form, Object request) {
			Redirector.getInstance().redirect(RequestDispatchSlipReadPage.OUTCOME
					,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of( ((RequestDispatchSlip)form.getEntity()).getIdentifier())));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(RequestDispatchSlip.FIELD_CODE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Code");
			}else if(RequestDispatchSlip.FIELD_NAME.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Libellé");
			}else if(RequestDispatchSlip.FIELD_REQUESTS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Demandes");
			}else if(RequestDispatchSlip.FIELD_FUNCTION.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Catégorie fonction budgétaire");
				map.put(AbstractInputChoice.FIELD_CHOICES,__inject__(FunctionController.class).readCreditManagersAuthorizingOfficersFinancialControllersAssistants());
			}
			return map;
		}
		
		@Override
		public AbstractInput<?> buildInput(Form form, String fieldName) {
			AbstractInput<?> input = super.buildInput(form, fieldName);
			if(RequestDispatchSlip.FIELD_REQUESTS.equals(input.getField().getName())) {
				input.setOutputLabel(null);
			}
			return input;
		}
		
		@Override
		public Map<Object, Object> getInputLabelCellArguments(Form form, AbstractInput<?> input, OutputLabel label) {
			Map<Object, Object> arguments = super.getInputLabelCellArguments(form, input, label);
			arguments.put(Cell.FIELD_WIDTH, 3);
			if(RequestDispatchSlip.FIELD_REQUESTS.equals(input.getField().getName())) {
				arguments.put(Cell.FIELD_CONTROL, OutputText.buildFromValue(label.getValue()));
			}
			return arguments;
		}
		
		@Override
		public Map<Object, Object> getInputCellArguments(Form form, AbstractInput<?> input) {
			Map<Object, Object> arguments = super.getInputCellArguments(form, input);
			arguments.put(Cell.FIELD_WIDTH, 9);
			if(RequestDispatchSlip.FIELD_REQUESTS.equals(input.getField().getName())) {
				arguments.put(Cell.FIELD_WIDTH, 12);
				arguments.put(Cell.FIELD_CONTROL_TEMPLATE, __getRequestsControlTemplate__());
			}
			return arguments;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKeyDoNotOverride(map, CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
		
		protected String __getRequestsControlTemplate__() {
			return "/private/requestdispatchslip/edit/__requests__.xhtml";
		}
	}
	
	/**/
	
	public static final String OUTCOME = "requestDispatchSlipEditView";	
}