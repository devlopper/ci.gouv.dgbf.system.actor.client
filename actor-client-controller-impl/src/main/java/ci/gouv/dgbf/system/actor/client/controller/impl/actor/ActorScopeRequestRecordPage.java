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
import org.cyk.utility.__kernel__.string.StringHelper;
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

import ci.gouv.dgbf.system.actor.client.controller.api.ActorScopeRequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Named @ViewScoped
public class ActorScopeRequestRecordPage extends AbstractEntityEditPageContainerManagedImpl<ActorScopeRequest> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		postConstruct(form);
	}
	
	public static void postConstruct(Form form) {
		form.getInput(SelectOneCombo.class, ActorScopeRequest.FIELD_SCOPE_TYPE)
		.enableValueChangeListener(List.of(form.getInput(SelectOneCombo.class, ActorScopeRequest.FIELD_SCOPES)));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Demande de domaines";
	}
	
	/**/
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.CREATE;
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action,ActorScopeRequestRecordPage.class,this);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, ActorScopeRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(ActorScopeRequest.FIELD_ACTORS
				,ActorScopeRequest.FIELD_SCOPE_TYPE,ActorScopeRequest.FIELD_SCOPES,ActorScopeRequest.FIELD_COMMENT));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener()
				.setPageContainerManaged(MapHelper.readByKey(arguments, ActorScopeRequestRecordPage.class)));		
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
			__inject__(ActorScopeRequestController.class).record((ActorScopeRequest) form.getEntity());
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {

		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ActorScopeRequest.FIELD_SCOPE_TYPE.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Type de domaine");
				map.put(SelectOneCombo.FIELD_CHOICE_CLASS,ScopeType.class);
				map.put(SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<ScopeType>() {
					@Override
					protected Collection<ScopeType> __computeChoices__(AbstractInputChoice<ScopeType> input,Class<?> entityClass) {
						Collection<ScopeType> choices = EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>()
								.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC));
						CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
						return choices;
					}
					@Override
					public void select(AbstractInputChoiceOne input, ScopeType scopeType) {
						super.select(input, scopeType);
						((AbstractEntityEditPageContainerManagedImpl<?>)pageContainerManaged).getForm().getInput(AutoComplete.class, ActorScopeRequest.FIELD_SCOPES)
							.setValue(null);
					}
				});
			}else if(ActorScopeRequest.FIELD_SCOPES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Domaines");
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Scope.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Scope>() {
					@Override
					public Collection<Scope> complete(AutoComplete autoComplete) {
						Arguments<Scope> arguments = new Arguments<Scope>()
								.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ScopeQuerier.FLAG_SEARCH)
								.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__());
						Object typeIdentifier = FieldHelper.readSystemIdentifier(AbstractInput.getValue(((AbstractEntityEditPageContainerManagedImpl<?>)pageContainerManaged)
								.getForm().getInput(SelectOneCombo.class, ActorScopeRequest.FIELD_SCOPE_TYPE)));
						if(typeIdentifier != null)
							arguments.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,typeIdentifier);
						Collection<Scope> choices = EntityReader.getInstance().readMany(Scope.class, arguments);
						return choices;
					}
								
				});
			}else if(ActorScopeRequest.FIELD_ACTORS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Acteurs");
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				map.put(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
				map.put(AutoComplete.FIELD_ENTITY_CLASS,Actor.class);
				map.put(AutoComplete.FIELD_LISTENER
						,new AutoComplete.Listener.AbstractImpl<Actor>() {
					@Override
					public Collection<Actor> complete(AutoComplete autoComplete) {
						Arguments<Actor> arguments = new Arguments<Actor>()
								.queryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ActorQuerier.FLAG_SEARCH)
								.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.FIELDS_CODE_NAMES_ELECTRONIC_MAIL_ADDRESS);
						if(StringHelper.isNotBlank(autoComplete.get__queryString__()))
							arguments.filterFieldsValues(ActorQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__());
						Collection<Actor> choices = EntityReader.getInstance().readMany(Actor.class, arguments);
						return choices;
					}
								
				});
			}else if(ActorScopeRequest.FIELD_COMMENT.equals(fieldName)) {
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
	
	public static final String OUTCOME = "actorScopeRequestRecordView";
}