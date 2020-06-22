package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorEditScopesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Actor actor;
	private ScopeType scopeType;
	private Collection<ActorScope> actorScopes;
	private Collection<ScopeType> scopeTypes;
	private DataTable actorScopesDataTable;
	private AutoComplete actorAutoComplete;
	private CommandButton saveCommandButton;
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntity(Actor.class);
		if(actor != null) {			
			scopeTypes = EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>()
					.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
							new QueryExecutorArguments.Dto().setQueryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
			
			actorScopesDataTable = ActorScopeListPage.instantiateDataTable(List.of(ActorScope.FIELD_SCOPE,ActorScope.FIELD_VISIBLE)
					,new DataTableListenerImpl(),new LazyDataModelListenerImpl());
			actorScopesDataTable.set__parentElement__(actor);
			actorScopesDataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE, "Ajouter",CommandButton.FIELD_PROCESS, "@this");
			actorScopesDataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		}
		super.__listenPostConstruct__();		
		
		actorAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto().addField(ActorQuerier.PARAMETER_NAME_STRING, autoComplete.get__queryString__());
			}
			@Override
			public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
				
			}
		},AutoComplete.FIELD_PLACEHOLDER,"rechercher par le nom d'utilisateur");
		
		actorAutoComplete.enableAjaxItemSelect();
		actorAutoComplete.getAjaxes().get("itemSelect").setListener(new Ajax.Listener.AbstractImpl() {
			@Override
			protected void run(AbstractAction action) {
				Actor actor = (Actor) FieldHelper.read(action.get__argument__(), "source.value");
				if(actor != null)
					JsfController.getInstance().redirect("actorEditScopesView",Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(actor.getIdentifier())));
			}			
		});
		actorAutoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
		actorAutoComplete.setReaderUsable(Boolean.TRUE);
		actorAutoComplete.setReadQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_STRING);
		actorAutoComplete.setCountQueryIdentifier(ActorQuerier.QUERY_NAME_COUNT_BY_STRING);
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL));
		saveCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@Override
			public void run(AbstractAction action) {				
														
			}
		});
		Collection<Map<?,?>> cellsMaps = new ArrayList<Map<?,?>>(); 
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Compte utilisateur"),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorAutoComplete,Cell.FIELD_WIDTH,11));	
		if(actor == null) {
			
		}else {
			actorAutoComplete.setValue(actor);
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorScopesDataTable,Cell.FIELD_WIDTH,12));
		}
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Affectation de domaine de visibilité";
	}
	
	/**/
	
	public class DataTableListenerImpl extends ActorScopeListPage.DataTableListenerImpl implements Serializable{
		
		public DataTableListenerImpl() {
			
		}
	}
	
	public class LazyDataModelListenerImpl extends ActorScopeListPage.LazyDataModelListenerImpl implements Serializable {
		
		@Override
		public Arguments<ActorScope> instantiateArguments(LazyDataModel<ActorScope> lazyDataModel) {
			Arguments<ActorScope> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ActorScopeQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_BY_SCOPE_TYPES_CODES);
			return arguments;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ActorScope> lazyDataModel) {
			return new Filter.Dto().addField(ActorScopeQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()))
					.addField(ActorScopeQuerier.PARAMETER_NAME_SCOPE_TYPES_CODES, List.of("SECTION"/*scopeType.getCode()*/));
		}
	}
}