package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
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
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.web.jsf.JsfController;
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
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribut;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentityGroup;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CivilityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityGroupQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorRepresentation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorCreateFromPublicPage extends AbstractEntityEditPageContainerManagedImpl<Actor> implements Serializable {

	private Actor actor;
	private Map<String,IdentificationAttribut> fieldsNames;
	
	@Override
	protected void __listenPostConstruct__() {
		action = Action.CREATE;
		actor = __inject__(ActorController.class).getOneToBeCreatedByPublic();
		fieldsNames = actor.computeFormFieldsNames();
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Form __buildForm__() {
		Form form = buildForm(Form.FIELD_ENTITY, actor,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener().setFieldsNames(fieldsNames));
		return form;
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.FIELD_ACTION, Action.CREATE);
		arguments.put(Form.FIELD_ENTITY, actor);
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return fieldsNames == null ? null : fieldsNames.keySet();
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> map = super.getInputArguments(form, fieldName);
				IdentificationAttribut attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
				if(attribut != null) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, attribut.getName());
					map.put(AbstractInput.FIELD_REQUIRED, attribut.getRequired());
				}
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
					map.put(AbstractInputChoice.FIELD_COLUMNS, 6);
					map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Civility>() {
						@Override
						public Collection<Civility> computeChoices(AbstractInputChoice<Civility> input) {
							return EntityReader.getInstance().readMany(Civility.class, new Arguments<Civility>()
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(CivilityQuerier.QUERY_IDENTIFIER_READ))));
						}
					});
				}else if(Actor.FIELD_GROUP.equals(fieldName)) {
					map.put(AbstractInputChoice.FIELD_COLUMNS, 6);
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
				JsfController.getInstance().redirect("actorNotifyAfterCreateView",Map.of(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS,List.of( ((Actor)form.getEntity()).getElectronicMailAddress() )));
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Création de mon compte";
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Actor.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			EntitySaver.getInstance().save(Actor.class, new Arguments<Actor>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
				.setActionIdentifier(ActorBusiness.CREATE_FROM_PUBLIC)).setRepresentation(ActorRepresentation.getProxy())
				.addCreatablesOrUpdatables((Actor)form.getEntity()));
		}
		
		@Override
		public void redirect(Form form, Object request) {
			JsfController.getInstance().redirect("actorNotifyAfterCreateView",Map.of(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS,List.of( ((Actor)form.getEntity()).getElectronicMailAddress() )));
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		private Map<String,IdentificationAttribut> fieldsNames;
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			return fieldsNames == null ? null : fieldsNames.keySet();
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			IdentificationAttribut attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
			if(attribut != null) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, attribut.getName());
				map.put(AbstractInput.FIELD_REQUIRED, attribut.getRequired());
			}
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
				map.put(AbstractInputChoice.FIELD_COLUMNS, 6);
				map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Civility>() {
					@Override
					public Collection<Civility> computeChoices(AbstractInputChoice<Civility> input) {
						return EntityReader.getInstance().readMany(Civility.class, new Arguments<Civility>()
							.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(CivilityQuerier.QUERY_IDENTIFIER_READ))));
					}
				});
			}else if(Actor.FIELD_GROUP.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_COLUMNS, 6);
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
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKeyDoNotOverride(map,CommandButton.FIELD_VALUE, "Créer");
			return map;
		}
	}
	
}