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
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.QueryIdentifierGetter;
import org.cyk.utility.__kernel__.persistence.query.QueryName;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
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
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetaryFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestCreatePage extends AbstractEntityEditPageContainerManagedImpl<AccountRequest> implements IdentificationTheme,Serializable {

	@Override
	protected void __listenPostConstruct__() {
		action = Action.CREATE;
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm();
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.FIELD_ACTION, Action.CREATE);
		arguments.put(Form.FIELD_ENTITY, new AccountRequest());
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(AccountRequest.FIELD_FIRST_NAME,AccountRequest.FIELD_LAST_NAMES,AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS);
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
							throwValidatorExceptionIf(accountRequest != null, "cette addresse est déja liée à une demande en cours");
						}
					});
				}
				return map;
			}
			
			@Override
			public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
				Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
				map.put(CommandButton.FIELD_VALUE, "Enregistrer");
				return map;
			}
		});
		
		arguments.put(Form.FIELD_LISTENER, new Form.Listener.AbstractImpl() {
			@Override
			public void redirect(Form form, Object request) {
				JsfController.getInstance().redirect("accountRequestNotifyAfterCreateView");
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Demande de compte";
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
		@Override
		public void redirect(Form form, Object request) {
			JsfController.getInstance().redirect("accountRequestNotifyAfterCreateView");
		}
	}
	
	public static class FormConfiguratorListenerImpl extends Form.ConfiguratorImpl.Listener.AbstractImpl implements Serializable {
		@Override
		public Collection<String> getFieldsNames(Form form) {
			return CollectionHelper.listOf(AccountRequest.FIELD_CIVILITY,AccountRequest.FIELD_FIRST_NAME,AccountRequest.FIELD_LAST_NAMES,AccountRequest.FIELD_GROUP
					,AccountRequest.FIELD_REGISTRATION_NUMBER,AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS,AccountRequest.FIELD_POSTAL_BOX
					,AccountRequest.FIELD_MOBILE_PHONE_NUMBER,AccountRequest.FIELD_OFFICE_PHONE_NUMBER,AccountRequest.FIELD_OFFICE_PHONE_EXTENSION
					,AccountRequest.FIELD_ADMINISTRATIVE_UNIT,AccountRequest.FIELD_ADMINISTRATIVE_FUNCTION
					,AccountRequest.FIELD_ACT_OF_APPOINTMENT_REFERENCE,AccountRequest.FIELD_ACT_OF_APPOINTMENT_SIGNATORY
					,AccountRequest.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE
					,AccountRequest.FIELD_BUDGETARY_FUNCTIONS
					,AccountRequest.FIELD_FUNCTIONS
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
						throwValidatorExceptionIf(accountRequest != null, "cette addresse est déja liée à une demande en cours");
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
			}else if(AccountRequest.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				/*map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE);
				map.put(AutoComplete.FIELD_LISTENER, new AutoComplete.Listener.AbstractImpl<AdministrativeUnit>() {
					@Override
					public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
						Filter.Dto filter = new Filter.Dto();
						filter.addField(AdministrativeUnitQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__());
						filter.addField(AdministrativeUnitQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__());
						return filter;
					}
				});*/
			}else if(AccountRequest.FIELD_BUDGETARY_FUNCTIONS.equals(fieldName)) {
				map.put(AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction(s) budgétaire(s)");
				map.put(AutoComplete.FIELD_ENTITY_CLASS, BudgetaryFunction.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE, Boolean.TRUE);
				/*map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, QueryIdentifierGetter.getInstance().get(BudgetaryFunction.class, QueryName.READ_WHERE_CODE_OR_NAME_LIKE));
				map.put(AutoComplete.FIELD_LISTENER, new AutoComplete.Listener.AbstractImpl<BudgetaryFunction>() {
					@Override
					public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
						Filter.Dto filter = new Filter.Dto();
						filter.addField(BudgetaryFunctionQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__());
						filter.addField(BudgetaryFunctionQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__());
						return filter;
					}
				});*/
			}else if(AccountRequest.FIELD_FUNCTIONS.equals(fieldName)) {
				map.put(AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Fonction(s) applicative(s) demandée(s)");
				map.put(AutoComplete.FIELD_ENTITY_CLASS, Function.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE, Boolean.TRUE);
				/*map.put(AutoComplete.FIELD_READ_QUERY_IDENTIFIER, QueryIdentifierGetter.getInstance().get(Function.class, QueryName.READ_WHERE_CODE_OR_NAME_LIKE));
				map.put(AutoComplete.FIELD_LISTENER, new AutoComplete.Listener.AbstractImpl<Function>() {
					@Override
					public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
						Filter.Dto filter = new Filter.Dto();
						filter.addField(FunctionQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__());
						filter.addField(FunctionQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__());
						return filter;
					}
				});*/
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