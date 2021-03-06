package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputBuilder;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputLabel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.AccountRequestController;
import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetaryFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Civility;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentityGroup;
import ci.gouv.dgbf.system.actor.server.business.api.AccountRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestEditPage extends AbstractEntityEditPageContainerManagedImpl<AccountRequest> implements IdentificationTheme,Serializable {

	private AccountRequest accountRequest;
	
	@Override
	protected void __listenPostConstruct__() {
		action = WebController.getInstance().getRequestParameterAction();
		if(action == null)
			action = Action.CREATE;
		if(Action.CREATE.equals(action)) {
			accountRequest = new AccountRequest();
		}else {
			String accountRequestIdentifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue());
			if(StringHelper.isBlank(accountRequestIdentifier)) {
				action = Action.CREATE;
				accountRequest = new AccountRequest();
			}else {
				//accountRequest = __inject__(AccountRequestController.class).readBySystemIdentifier(accountRequestIdentifier);
				//accountRequest = EntityReader.getInstance().readOne(AccountRequest.class, AccountRequestQuerier.QUERY_IDENTIFIER_READ_PROJECTION_01_BY_IDENTIFIER
				//		, AccountRequestQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
				accountRequest = __inject__(AccountRequestController.class).readProjection01WithBudgetaryFunctionsAndFunctionsByIdentifier(accountRequestIdentifier);
			}
		}
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION, action,Form.FIELD_ENTITY,accountRequest,"commandButtonObject",this);
	}

	@Override
	protected String __getWindowTitleValue__() {
		if(Action.CREATE.equals(action))
			return "Demande de compte";
		if(Action.READ.equals(action)) {
			return (isRejectable() ? "Rejet" : "Consultation")+" de la demande de compte de "+accountRequest.getElectronicMailAddress()+" | "+accountRequest.getNames();
		}
		return super.__getWindowTitleValue__();
	}
	
	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		//AccountRequest accountRequest = new AccountRequest();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, AccountRequest.class);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, Action.CREATE);
		//MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ENTITY, accountRequest);
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());		
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		//MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LAYOUT_BUILDABLE, Boolean.FALSE);
		//MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LAYOUT, buildLayout(AccountRequest.class,accountRequest,Action.class,MapHelper.readByKey(map, Form.FIELD_ACTION)
		//		,"commandButtonObject",MapHelper.readByKey(map, "commandButtonObject")));
		Form form = Form.build(map);
		//form.setLayout(buildLayout(AccountRequest.class,accountRequest,Action.class,MapHelper.readByKey(map, Form.FIELD_ACTION)
		//		,"commandButtonObject",MapHelper.readByKey(map, "commandButtonObject"),Form.class,form));
		return form;
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	/**/
	
	public static Layout buildLayout(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		Form form  = (Form) MapHelper.readByKey(map, Form.class);
		Action action = (Action) MapHelper.readByKey(map, Action.class);
		AccountRequest accountRequest = (AccountRequest) (Action.CREATE.equals(action) ? form.getEntity() : form.getEntity());/*MapHelper.readByKey(map, AccountRequest.class))*/;		
		MapHelper.writeByKeyDoNotOverride(map, Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G);
		@SuppressWarnings("unchecked")
		Collection<Map<Object,Object>> cellsMaps = (Collection<Map<Object, Object>>) MapHelper.readByKey(map, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS);
		CommandButton commandButton = null;
		if(cellsMaps == null) {
			cellsMaps = new ArrayList<>();
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_CIVILITY, "Civilité", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_FIRST_NAME, "Nom", 2, 3);
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_LAST_NAMES, "Prénom(s)", 1, 6);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_GROUP, "Groupe", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_REGISTRATION_NUMBER, "Matricule", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS, "Mail", 2, 3);//Adresse de courrier électronique
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_POSTAL_BOX_ADDRESS, "Boite postale", 1, 6);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_MOBILE_PHONE_NUMBER, "Tel. Mobile", 2, 3);
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_OFFICE_PHONE_NUMBER, "Tel. Bureau", 1, 3);
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_OFFICE_PHONE_EXTENSION, "Poste", 1, 2);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_ADMINISTRATIVE_UNIT, "Unité administrative", 2, 10);
			
			instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_ADMINISTRATIVE_FUNCTION, "Fonction administrative", 2, 10);
			
			if(Action.READ.equals(action)) {
				instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_TREATMENT, "Traitement", 2, 10);
				instantiateNameValue(cellsMaps, accountRequest, action, AccountRequest.FIELD_REJECT_REASON, "Motif de rejet", 2, 10);	
			}		
			
			if(Action.CREATE.equals(action) || Action.READ.equals(action)) {
				cellsMaps.stream().filter(x -> x.get(Cell.FIELD_CONTROL) instanceof AbstractInput).map(x -> x.get(Cell.FIELD_CONTROL)).collect(Collectors.toList());
				Collection<?> inputs = cellsMaps.stream().filter(x -> x.get(Cell.FIELD_CONTROL) instanceof AbstractInput)
						.map(x -> x.get(Cell.FIELD_CONTROL)).collect(Collectors.toList());
				
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,commandButton = CommandButton.build(MapHelper.instantiate(
						CommandButton.FIELD_VALUE,"Enregistrer"
						,CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.ConfiguratorImpl.FIELD_OBJECT,form
						,CommandButton.ConfiguratorImpl.FIELD_METHOD_NAME,Form.METHOD_EXECUTE
						,CommandButton.ConfiguratorImpl.FIELD_INPUTS,inputs
						,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right")),Cell.FIELD_WIDTH,12));	
			}
				
			MapHelper.writeByKeyDoNotOverride(map, Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
		}
		Layout layout = Layout.build(map);
		if(commandButton != null)
			commandButton.addUpdatables(layout);
		return layout;
	}
	
	public static Layout buildLayout(Object...objects) {
		return buildLayout(MapHelper.instantiate(objects));
	}
	
	private static Object instantiateName(AccountRequest accountRequest,Action action,String fieldName,String name) {
		if(Action.CREATE.equals(action)) {
			return OutputLabel.buildFromValueFor(name,fieldName);
		}else {
			return OutputText.buildFromValue(name);
		}
	}
	
	private static Object instantiateValue(AccountRequest accountRequest,Action action,String fieldName) {
		if(Action.CREATE.equals(action)) {
			if(AccountRequest.FIELD_CIVILITY.equals(fieldName))
				return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(AbstractInput.FIELD_IDENTIFIER,fieldName
						,AbstractInput.FIELD_REQUIRED,Boolean.FALSE
						,AbstractInputChoice.FIELD_COLUMNS,6
						,AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Civility>() {
							@Override
							public Collection<Civility> computeChoices(AbstractInputChoice<Civility> input) {
								return EntityReader.getInstance().readMany(Civility.class, new Arguments<Civility>()
									.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
										.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(CivilityQuerier.QUERY_IDENTIFIER_READ))));
							}
						})
				, SelectOneRadio.class);
			if(AccountRequest.FIELD_GROUP.equals(fieldName))
				return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(InputText.FIELD_IDENTIFIER,fieldName
						,AbstractInputChoice.FIELD_COLUMNS,6
						,AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<IdentityGroup>() {
					@Override
					public Collection<IdentityGroup> computeChoices(AbstractInputChoice<IdentityGroup> input) {
						return EntityReader.getInstance().readMany(IdentityGroup.class, new Arguments<IdentityGroup>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(IdentityGroupQuerier.QUERY_IDENTIFIER_READ))));
					}
						})
				, SelectOneRadio.class);
			if(AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName))
				return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(InputText.FIELD_IDENTIFIER,fieldName
						,AbstractInputChoice.FIELD_COLUMNS,6
						,InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
							@Override
							public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
								super.validate(context, component, value);
								Actor actor =__inject__(ActorController.class).readByElectronicMailAddress((String) value);
								throwValidatorExceptionIf(actor != null, "cette addresse est déja liée à un compte");
								AccountRequest accountRequest =__inject__(AccountRequestController.class).readByElectronicMailAddress((String) value);
								throwValidatorExceptionIf(accountRequest != null, "cette addresse est déja liée à une demande en cours de traitement");
							}
						})
				, InputText.class);
			if(AccountRequest.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName))
				return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(InputText.FIELD_IDENTIFIER,fieldName
						,AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class
						,AutoComplete.FIELD_READER_USABLE, Boolean.TRUE)
				, AutoComplete.class);
			
			/*  */
			
			return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(InputText.FIELD_IDENTIFIER,fieldName), InputText.class);
		}else {
			if(AccountRequest.FIELD_TREATMENT.equals(fieldName))
				return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(InputText.FIELD_IDENTIFIER,fieldName
						,AbstractInputChoice.FIELD_COLUMNS,2,AbstractInputChoice.FIELD_CHOICES, TREATMENT_CHOICES)
				, SelectOneRadio.class);
			
			if(AccountRequest.FIELD_REJECT_REASON.equals(fieldName))
				return InputBuilder.getInstance().build(accountRequest, fieldName, MapHelper.instantiate(InputText.FIELD_IDENTIFIER,fieldName), InputText.class);
			return OutputText.buildFromValue(StringHelper.get(FieldHelper.read(accountRequest, fieldName)));
		}
	}
	
	private static void instantiateNameValue(Collection<Map<Object,Object>> cellsMaps,AccountRequest accountRequest,Action action,String fieldName,String name,Integer nameWidth,Integer valueWidth) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,instantiateName(accountRequest, action, fieldName, name),Cell.FIELD_WIDTH,nameWidth));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,instantiateValue(accountRequest, action, fieldName),Cell.FIELD_WIDTH,valueWidth));
	}
	
	/**/

	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.TRUE;
		}
		
		public void act(Form form) {
			AccountRequest accountRequest = (AccountRequest) form.getEntity();
			if(Action.CREATE.equals(form.getAction())) {
				if(accountRequest.getActOfAppointmentSignatureDate() != null) {
					accountRequest.setActOfAppointmentSignatureDateAsTimestamp(accountRequest.getActOfAppointmentSignatureDate().getTime());
					accountRequest.setActOfAppointmentSignatureDate(null);
				}
				EntitySaver.getInstance().save(AccountRequest.class, new Arguments<AccountRequest>().setUpdatables(List.of(accountRequest))
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(AccountRequestBusiness.RECORD)));
			}else if(Action.READ.equals(form.getAction())) {
				String acceptOrReject = isRejectable() ? AccountRequestBusiness.REJECT : TREATMENT_CHOICE_ACCEPT.equals(accountRequest.getTreatment()) ? AccountRequestBusiness.ACCEPT : AccountRequestBusiness.REJECT;
				if(AccountRequestBusiness.REJECT.equals(acceptOrReject))
					AccountRequestBusiness.validateReject(accountRequest.getRejectReason());
				EntitySaver.getInstance().save(AccountRequest.class, new Arguments<AccountRequest>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setActionIdentifier(acceptOrReject))
						.setRepresentation(AccountRequestRepresentation.getProxy())
						.addCreatablesOrUpdatables(accountRequest));
			}
		}
		
		@Override
		public void redirect(Form form, Object request) {
			if(Action.CREATE.equals(form.getAction()))
				JsfController.getInstance().redirect("accountRequestNotifyAfterCreateView");
		}
	}
	

	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> fieldsNames = CollectionHelper.listOf(AccountRequest.FIELD_CIVILITY,AccountRequest.FIELD_FIRST_NAME,AccountRequest.FIELD_LAST_NAMES,AccountRequest.FIELD_GROUP
					,AccountRequest.FIELD_REGISTRATION_NUMBER,AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS,AccountRequest.FIELD_POSTAL_BOX_ADDRESS
					,AccountRequest.FIELD_MOBILE_PHONE_NUMBER,AccountRequest.FIELD_OFFICE_PHONE_NUMBER,AccountRequest.FIELD_OFFICE_PHONE_EXTENSION
					,AccountRequest.FIELD_ADMINISTRATIVE_UNIT,AccountRequest.FIELD_ADMINISTRATIVE_FUNCTION
					/*
					,AccountRequest.FIELD_ACT_OF_APPOINTMENT_REFERENCE,AccountRequest.FIELD_ACT_OF_APPOINTMENT_SIGNATORY
					,AccountRequest.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE
					*/
					//,AccountRequest.FIELD_BUDGETARY_FUNCTIONS
					//,AccountRequest.FIELD_FUNCTIONS
					);
			if(Action.READ.equals(form.getAction())) {
				if(isRejectable())
					fieldsNames.addAll(List.of(AccountRequest.FIELD_REJECT_REASON));
				else
					fieldsNames.addAll(List.of(AccountRequest.FIELD_TREATMENT,AccountRequest.FIELD_REJECT_REASON));
			}
			return fieldsNames;
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
					@Override
					public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
						super.validate(context, component, value);
						Actor actor =__inject__(ActorController.class).readByElectronicMailAddress((String) value);
						throwValidatorExceptionIf(actor != null, "cette addresse est déja liée à un compte");
						AccountRequest accountRequest =__inject__(AccountRequestController.class).readByElectronicMailAddress((String) value);
						throwValidatorExceptionIf(accountRequest != null, "cette addresse est déja liée à une demande en cours de traitement");
					}
				});
			}else if(AccountRequest.FIELD_CIVILITY.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_COLUMNS,6);
				map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Civility>() {
					@Override
					public Collection<Civility> computeChoices(AbstractInputChoice<Civility> input) {
						return EntityReader.getInstance().readMany(Civility.class, new Arguments<Civility>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(CivilityQuerier.QUERY_IDENTIFIER_READ))));
					}
				});
			}else if(AccountRequest.FIELD_GROUP.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_COLUMNS,6);
				map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<IdentityGroup>() {
					@Override
					public Collection<IdentityGroup> computeChoices(AbstractInputChoice<IdentityGroup> input) {
						return EntityReader.getInstance().readMany(IdentityGroup.class, new Arguments<IdentityGroup>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(IdentityGroupQuerier.QUERY_IDENTIFIER_READ))));
					}
				});
			}else if(AccountRequest.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				map.put(InputText.FIELD_REQUIRED, Boolean.TRUE);
			}else if(AccountRequest.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
			}else if(AccountRequest.FIELD_BUDGETARY_FUNCTIONS.equals(fieldName)) {
				map.put(AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction(s) budgétaire(s)");
				map.put(AutoComplete.FIELD_ENTITY_CLASS, BudgetaryFunction.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE, Boolean.TRUE);
			}else if(AccountRequest.FIELD_FUNCTIONS.equals(fieldName)) {
				map.put(AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction(s) applicative(s)");
				map.put(AutoComplete.FIELD_ENTITY_CLASS, Function.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE, Boolean.TRUE);
			}else if(AccountRequest.FIELD_TREATMENT.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_ACTION, Action.CREATE);
				map.put(AbstractInputChoice.FIELD_COLUMNS,2);
				map.put(AbstractInputChoice.FIELD_CHOICES, TREATMENT_CHOICES);
			}else if(AccountRequest.FIELD_REJECT_REASON.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_ACTION, Action.CREATE);
				map.put(AbstractInput.FIELD_REQUIRED, isRejectable());
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
	
	public static Boolean isRejectable() {
		return "rejeter".equals(WebController.getInstance().getRequestParameter("traitement"));
	}
	
	public static final String TREATMENT_CHOICE_ACCEPT = "Accepter la demande de compte";
	public static final String TREATMENT_CHOICE_REJECT = "Rejeter la demande de compte";
	public static final List<String> TREATMENT_CHOICES = List.of(TREATMENT_CHOICE_ACCEPT,TREATMENT_CHOICE_REJECT);
}