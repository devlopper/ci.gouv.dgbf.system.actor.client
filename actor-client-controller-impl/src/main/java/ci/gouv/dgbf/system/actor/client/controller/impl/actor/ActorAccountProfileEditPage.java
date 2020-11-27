package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Civility;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentityGroup;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorAccountProfileEditPage extends AbstractEntityEditPageContainerManagedImpl<Actor> implements Serializable {

	private Actor actor;
	
	@Override
	protected void __listenPostConstruct__() {
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER);
		actor = getActor(identifier);
		if(actor != null) {
			
		}
		super.__listenPostConstruct__();
		form = buildForm(Form.FIELD_ENTITY,actor);
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.UPDATE;
	}
	
	@Override
	protected Form __buildForm__() {
		Form form = buildForm(Form.FIELD_ENTITY,actor);
		return form;
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		if(actor == null)
			return super.__getWindowTitleValue__();
		return "Modification de profile - "+actor.getCode();
	}
	
	/**/
	
	public static Actor getActor(String identifier) {
		if(StringHelper.isBlank(identifier))
			return null;
		return EntityReader.getInstance().readOne(Actor.class, ActorQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_EDIT, ActorQuerier.PARAMETER_NAME_IDENTIFIER,identifier);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Collection<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(
			Actor.FIELD_CIVILITY,Actor.FIELD_FIRST_NAME,Actor.FIELD_LAST_NAMES,Actor.FIELD_REGISTRATION_NUMBER,Actor.FIELD_GROUP				
				
			,Actor.FIELD_ELECTRONIC_MAIL_ADDRESS,Actor.FIELD_MOBILE_PHONE_NUMBER,Actor.FIELD_OFFICE_PHONE_NUMBER,Actor.FIELD_OFFICE_PHONE_EXTENSION
			,Actor.FIELD_POSTAL_BOX_ADDRESS
				
			,Actor.FIELD_ADMINISTRATIVE_UNIT,Actor.FIELD_ADMINISTRATIVE_FUNCTION,Actor.FIELD_ACT_OF_APPOINTMENT_REFERENCE,Actor.FIELD_ACT_OF_APPOINTMENT_SIGNATORY
			,Actor.FIELD_ACT_OF_APPOINTMENT_SIGNATURE_DATE
		));
		Actor actor = (Actor) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		FormConfiguratorListener formConfiguratorListener = (FormConfiguratorListener) MapHelper.readByKey(arguments, Form.ConfiguratorImpl.FIELD_LISTENER);
		if(formConfiguratorListener == null)
			formConfiguratorListener = new FormConfiguratorListener();
		formConfiguratorListener.setInitialElectronicMailAddress(actor.getElectronicMailAddress());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Actor.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, actor);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.UPDATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, formConfiguratorListener);		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, columnsFieldsNames);
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			Actor actor = (Actor) form.getEntity();
			if(actor.getActOfAppointmentSignatureDate() == null)
				actor.setActOfAppointmentSignatureDateAsTimestamp(null);
			else
				actor.setActOfAppointmentSignatureDateAsTimestamp(actor.getActOfAppointmentSignatureDate().getTime());
			EntitySaver.getInstance().save(Actor.class, new Arguments<Actor>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
					.setActionIdentifier(ActorBusiness.SAVE_PROFILE)).addCreatablesOrUpdatables(actor));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		private String initialElectronicMailAddress;
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(InputText.FIELD_LISTENER, new InputText.Listener.AbstractImpl() {
					@Override
					public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
						super.validate(context, component, value);
						if(initialElectronicMailAddress.equalsIgnoreCase((String)value))
							return;
						Actor actor =__inject__(ActorController.class).readByElectronicMailAddress((String) value);
						throwValidatorExceptionIf(actor != null, "cette addresse est déja liée à un compte");
					}
				});
			}else if(Actor.FIELD_CIVILITY.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_COLUMNS,6);
				map.put(AbstractInputChoice.FIELD_CHOICES,EntityReader.getInstance().readMany(Civility.class, CivilityQuerier.QUERY_IDENTIFIER_READ));
			}else if(Actor.FIELD_GROUP.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_COLUMNS,6);
				map.put(AbstractInputChoice.FIELD_CHOICES,EntityReader.getInstance().readMany(IdentityGroup.class, IdentityGroupQuerier.QUERY_IDENTIFIER_READ));
			}else if(Actor.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				
			}else if(Actor.FIELD_ADMINISTRATIVE_UNIT.equals(fieldName)) {
				map.put(AutoComplete.FIELD_ENTITY_CLASS, AdministrativeUnit.class);
				map.put(AutoComplete.FIELD_READER_USABLE, Boolean.TRUE);
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

	public static final String OUTCOME = "actorAccountProfileEditView";
}