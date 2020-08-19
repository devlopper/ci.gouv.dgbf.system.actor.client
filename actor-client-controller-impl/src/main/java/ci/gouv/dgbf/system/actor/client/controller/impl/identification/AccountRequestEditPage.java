package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
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
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
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
				accountRequest = __inject__(AccountRequestController.class).readProjection02WithBudgetaryFunctionsAndFunctionsByIdentifier(accountRequestIdentifier);
			}
		}
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION, action,Form.FIELD_ENTITY,accountRequest);
	}

	@Override
	protected String __getWindowTitleValue__() {
		if(Action.CREATE.equals(action))
			return "Demande de compte";
		if(Action.UPDATE.equals(action))
			return "Modification de la demande de compte de "+accountRequest.getElectronicMailAddress()+" | "+accountRequest.getNames();
		return super.__getWindowTitleValue__();
	}
	
	public static Form buildForm(Map<Object,Object> map) {
		if(map == null)
			map = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(map, Form.FIELD_ENTITY_CLASS, AccountRequest.class);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_ENTITY, new AccountRequest());
		MapHelper.writeByKeyDoNotOverride(map,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListenerImpl());		
		MapHelper.writeByKeyDoNotOverride(map,Form.FIELD_LISTENER, new FormListenerImpl());
		return Form.build(map);
	}
	
	public static Form buildForm(Object...arguments) {
		return buildForm(MapHelper.instantiate(arguments));
	}
	
	/**/

	public static class FormListenerImpl extends Form.Listener.AbstractImpl implements Serializable {
		public void act(Form form) {
			AccountRequest accountRequest = (AccountRequest) form.getEntity();
			if(accountRequest.getActOfAppointmentSignatureDate() != null) {
				accountRequest.setActOfAppointmentSignatureDateAsTimestamp(accountRequest.getActOfAppointmentSignatureDate().getTime());
				accountRequest.setActOfAppointmentSignatureDate(null);
			}
			//EntitySaver.getInstance().save(AccountRequest.class, new Arguments<AccountRequest>().setUpdatables(List.of(accountRequest))
			//		.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(AccountRequestBusiness.SAVE_INITIALS_FROM_COMPUTATION)));
			super.act(form);
		}
		
		@Override
		public void redirect(Form form, Object request) {
			JsfController.getInstance().redirect("accountRequestNotifyAfterCreateView");
		}
	}
	
	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			return CollectionHelper.listOf(AccountRequest.FIELD_CIVILITY,AccountRequest.FIELD_FIRST_NAME,AccountRequest.FIELD_LAST_NAMES,AccountRequest.FIELD_GROUP
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
				map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Civility>() {
					@Override
					public Collection<Civility> computeChoices(AbstractInputChoice<Civility> input) {
						return EntityReader.getInstance().readMany(Civility.class, new Arguments<Civility>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(CivilityQuerier.QUERY_IDENTIFIER_READ))));
					}
				});
			}else if(AccountRequest.FIELD_GROUP.equals(fieldName)) {
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
}