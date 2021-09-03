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
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.api.ProfileController;
import ci.gouv.dgbf.system.actor.client.controller.api.ScopeTypeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Named @ViewScoped
public class ActorRecordRequestsPage extends AbstractEntityEditPageContainerManagedImpl<Actor> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		postConstruct(form);
	}
	
	public static void postConstruct(Form form) {
		form.getInput(SelectOneCombo.class, Actor.FIELD_SCOPE_TYPE)
		.enableValueChangeListener(List.of(form.getInput(SelectOneCombo.class, Actor.FIELD_SCOPES)));
	}
	
	public static String getWindowTitle() {
		return "Demande";
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return getWindowTitle();
	}
	
	/**/
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.CREATE;
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action,ActorRecordRequestsPage.class,this);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Actor.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(Actor.FIELD_ACTORS
				,Actor.FIELD_SCOPE_TYPE,Actor.FIELD_SCOPES,Actor.FIELD_PROFILES,Actor.FIELD_COMMENT));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener()
				.setPageContainerManaged(MapHelper.readByKey(arguments, ActorRecordRequestsPage.class)));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			__inject__(ActorController.class).recordRequests((Actor) form.getEntity());
		}
		
		@Override
		public void redirect(Form form, Object request) {
			Redirector.getInstance().redirect(new Redirector.Arguments().outcome(OUTCOME));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {

		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Actor.FIELD_SCOPE_TYPE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.LABEL);
				map.put(SelectOneCombo.FIELD_CHOICE_CLASS,ScopeType.class);
				map.put(SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<ScopeType>() {
					@Override
					protected Collection<ScopeType> __computeChoices__(AbstractInputChoice<ScopeType> input,Class<?> entityClass) {
						Collection<ScopeType> choices = __inject__(ScopeTypeController.class).readRequestable();
						CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
						return choices;
					}
					@Override
					public void select(AbstractInputChoiceOne input, ScopeType scopeType) {
						super.select(input, scopeType);
						((AbstractEntityEditPageContainerManagedImpl<?>)pageContainerManaged).getForm().getInput(AutoComplete.class, Actor.FIELD_SCOPES)
							.setValue(null);
					}
				});
			}else if(Actor.FIELD_SCOPES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.LABEL);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Scope.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Scope>() {
					@Override
					public Collection<Scope> complete(AutoComplete autoComplete) {
						Object typeIdentifier = FieldHelper.readSystemIdentifier(AbstractInput.getValue(((AbstractEntityEditPageContainerManagedImpl<?>)pageContainerManaged)
								.getForm().getInput(SelectOneCombo.class, Actor.FIELD_SCOPE_TYPE)));
						if(typeIdentifier == null)
							return null;
						Arguments<Scope> arguments = new Arguments<Scope>()
								.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ScopeQuerier.FLAG_SEARCH)
								.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__()
										,ScopeQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,typeIdentifier);
						return EntityReader.getInstance().readMany(Scope.class, arguments);
					}
								
				});
			}else if(Actor.FIELD_PROFILES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.LABEL);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Profile.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Profile>() {
					@Override
					public Collection<Profile> complete(AutoComplete autoComplete) {
						return __inject__(ProfileController.class).readRequestable(null);
					}								
				});
			}else if(Actor.FIELD_ACTORS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.LABEL);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Actor.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Actor>() {
					@Override
					public Collection<Actor> complete(AutoComplete autoComplete) {
						return __inject__(ActorController.class).search(autoComplete.get__queryString__());
					}
								
				});
			}else if(Actor.FIELD_COMMENT.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Commentaire");
				
			}
			return map;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKey(map,CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	public static final String OUTCOME = "actorRecordRequestsView";
}