package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.ReadListener;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeFilterController;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorScopeCreateOrDeletePage extends AbstractPageContainerManagedImpl implements Serializable {

	protected SelectOneCombo scopeTypeSelectOne;
	protected AutoComplete actorsAutoComplete,scopesAutoComplete;
	protected CommandButton saveCommandButton;
	protected Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		Scope scope = ScopeFilterController.getScopeFromRequestParameter();		
		scopeTypeSelectOne = buildScopeTypeSelectOne(ScopeFilterController.getScopeTypeFromRequestParameter(scope));
		actorsAutoComplete = buildActorsAutoComplete();
		scopesAutoComplete = buildScopesAutoComplete(CollectionHelper.listOf(scope));
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,ActorScopeBusiness.VISIBLE.equals(getActionIdentifier()) ? "Affecter" : "Retirer"
			,CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						@SuppressWarnings("unchecked")
						Collection<Actor> actors = (Collection<Actor>) actorsAutoComplete.getValue();
						if(CollectionHelper.isEmpty(actors))
							throw new RuntimeException("Sélectionner au moins un acteur");
						@SuppressWarnings("unchecked")
						Collection<Object> scopes = (Collection<Object>) scopesAutoComplete.getValue();
						if(CollectionHelper.isEmpty(scopes))
							throw new RuntimeException("Sélectionner au moins un domaine");												
						Arguments<ActorScope> arguments = new Arguments<ActorScope>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(new ActorScope()
								.setActorsIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(actors))
								.setScopesIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(scopes)));
						arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(getActionIdentifier()));							
						EntitySaver.getInstance().save(ActorScope.class, arguments);
						return arguments.get__responseEntity__();
					}
				});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne,Cell.FIELD_WIDTH,3)
				
				,MapHelper.instantiate(Cell.FIELD_CONTROL,scopesAutoComplete,Cell.FIELD_WIDTH,8)
				
				,MapHelper.instantiate(Cell.FIELD_CONTROL,actorsAutoComplete.getOutputLabel(),Cell.FIELD_WIDTH,1)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,actorsAutoComplete,Cell.FIELD_WIDTH,11)
				
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			));
	}
	
	protected abstract String getActionIdentifier();
	
	/**/
	
	private AutoComplete buildActorsAutoComplete() {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_MULTIPLE,Boolean.TRUE,AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER
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
						
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Acteur");
		input.setReadItemLabelListener(new ReadListener() {		
			@Override
			public Object read(Object object) {
				if(object == null)
					return null;
				return ((Actor)object).getCode()+" - "+((Actor)object).getNames();
			}
		});
		//input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private SelectOneCombo buildScopeTypeSelectOne(ScopeType scopeType) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,scopeType,SelectOneCombo.FIELD_CHOICE_CLASS,ScopeType.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ScopeType>() {
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
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Domaine");
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private AutoComplete buildScopesAutoComplete(Collection<Scope> scopes) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_VALUE,scopes,AutoComplete.FIELD_MULTIPLE,Boolean.TRUE,AutoComplete.FIELD_ENTITY_CLASS,Scope.class,AutoComplete.FIELD_LISTENER
				,new AutoComplete.Listener.AbstractImpl<Scope>() {
			@Override
			public Collection<Scope> complete(AutoComplete autoComplete) {
				Arguments<Scope> arguments = new Arguments<Scope>()
						.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ScopeQuerier.FLAG_SEARCH)
						.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__());
				Object typeIdentifier = FieldHelper.readSystemIdentifier(AbstractInput.getValue(scopeTypeSelectOne));
				if(typeIdentifier != null)
					arguments.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,typeIdentifier);
				Collection<Scope> choices = EntityReader.getInstance().readMany(Scope.class, arguments);
				return choices;
			}
						
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Domaine");
		return input;
	}
}