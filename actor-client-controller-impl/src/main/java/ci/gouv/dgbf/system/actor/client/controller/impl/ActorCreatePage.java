package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.Password;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Civility;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentityGroup;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorCreatePage extends AbstractEntityEditPageContainerManagedImpl<Actor> implements Serializable {

	private String passwordConfirmationIdentifier = Password.ConfiguratorImpl.generateIdentifier(Password.class);;
	
	@Override
	protected void __listenPostConstruct__() {
		action = Action.CREATE;
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.FIELD_ACTION, Action.CREATE);
		arguments.put(Form.FIELD_ENTITY, new Actor());
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(Actor.FIELD_CIVILITY,Actor.FIELD_FIRST_NAME,Actor.FIELD_LAST_NAMES,Actor.FIELD_GROUP,Actor.FIELD_REGISTRATION_NUMBER
						,Actor.FIELD_ELECTRONIC_MAIL_ADDRESS,Actor.FIELD_MOBILE_PHONE_NUMBER,Actor.FIELD_OFFICE_PHONE_NUMBER,Actor.FIELD_OFFICE_PHONE_EXTENSION
						,Actor.FIELD_ADMINISTRATIVE_UNIT,Actor.FIELD_ADMINISTRATIVE_FUNCTION
						,Actor.FIELD_CODE);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> map = super.getInputArguments(form, fieldName);
				if(Actor.FIELD_FUNCTIONS.equals(fieldName)) {
					map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Function>() {
						@Override
						public Collection<Function> computeChoices(AbstractInputChoice<Function> input) {
							Collection<Function> functions = EntityReader.getInstance().readMany(Function.class);
							return functions;
						}
					});
				}else if(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
					map.put(InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
						@Override
						public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
							super.validate(context, component, value);
							Actor actor =__inject__(ActorController.class).readByElectronicMailAddress((String) value);
							if(actor != null)
								throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"cette addresse est déja utilisée","cette addresse est déja utilisée"));
						}
					});
				}else if(Actor.FIELD_PASSWORD.equals(fieldName)) {
					map.put(Password.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Mot de passe");
					map.put(Password.FIELD_MATCH,passwordConfirmationIdentifier);
				}else if(Actor.FIELD_PASSWORD_CONFIRMATION.equals(fieldName)) {
					map.put(Password.FIELD_IDENTIFIER, passwordConfirmationIdentifier);
					map.put(Password.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Confirmation du mot de passe");
				}else if(Actor.FIELD_CODE.equals(fieldName)) {
					map.put(InputText.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Nom d'utilisateur");
					map.put(InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
						@Override
						public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
							super.validate(context, component, value);
							Actor actor =__inject__(ActorController.class).readByCode((String) value);
							if(actor != null)
								throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,"ce nom d'utilisateur existe déja","ce nom d'utilisateur existe déja"));
						}
					});
					map.put(InputText.FIELD_REQUIRED, Boolean.FALSE);
					map.put(InputText.ConfiguratorImpl.FIELD_MESSAGABLE, Boolean.TRUE);
				}else if(Actor.FIELD_CIVILITY.equals(fieldName)) {
					map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Civility>() {
						@Override
						public Collection<Civility> computeChoices(AbstractInputChoice<Civility> input) {
							return EntityReader.getInstance().readMany(Civility.class, new Arguments<Civility>()
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(CivilityQuerier.QUERY_IDENTIFIER_READ))));
						}
					});
				}else if(Actor.FIELD_GROUP.equals(fieldName)) {
					map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<IdentityGroup>() {
						@Override
						public Collection<IdentityGroup> computeChoices(AbstractInputChoice<IdentityGroup> input) {
							return EntityReader.getInstance().readMany(IdentityGroup.class, new Arguments<IdentityGroup>()
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(IdentityGroupQuerier.QUERY_IDENTIFIER_READ))));
						}
					});
				}else if(Actor.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
					map.put(InputText.FIELD_REQUIRED, Boolean.TRUE);
				}else if(Actor.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
					map.put(AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class);
					map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
				}
				return map;
			}
		});
		
		arguments.put(Form.FIELD_LISTENER, new Form.Listener.AbstractImpl() {
			@Override
			public void redirect(Form form, Object request) {
				JsfController.getInstance().redirect("actorListView");
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Création d'un compte utilisateur";
	}
}