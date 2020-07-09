package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorScopeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorListScopesPage extends AbstractActorListPrivilegesOrScopesPage<Scope> implements Serializable {

	private ScopeType scopeType;
	private MenuModel tabMenu;
	private Integer tabMenuActiveIndex;
	private DataTable dataTable;
	
	@Override
	protected void addOutputs(Collection<Map<?, ?>> cellsMaps) {
		scopeType = WebController.getInstance().getRequestParameterEntityAsParent(ScopeType.class);
		Collection<ScopeType> scopeTypes = EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		if(scopeType == null)
			scopeType = CollectionHelper.getFirst(scopeTypes);
		
		Collection<String> columnsNames = CollectionHelper.listOf(Scope.FIELD_CODE,Scope.FIELD_NAME);
		if(ScopeType.isCodeEqualsUA(scopeType))
			columnsNames.add(Scope.FIELD_SECTION_AS_STRING);
		
		dataTable = ScopeListPage.buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl()
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setActor(actor).setScopeType(scopeType)
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsNames);
		dataTable.set__parentElement__(actor);
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_LISTENER
				,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected String getOutcome(AbstractAction action) {
				return "actorCreateScopesView";
			}
			@Override
			protected Map<String, List<String>> getViewParameters(AbstractAction action) {
				Map<String, List<String>> parameters = super.getViewParameters(action);
				if(scopeType != null) {
					if(parameters == null)
						parameters = new HashMap<>();
					parameters.put(ParameterName.stringify(ScopeType.class), List.of(scopeType.getIdentifier()));
				}				
				return parameters;
			}
		});
		
		dataTable.addRecordMenuItemByArgumentsExecuteFunction("Supprimer", "fa fa-remove", new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				super.__runExecuteFunction__(action);
				Scope scope = (Scope) action.get__argument__();
				__inject__(ActorScopeController.class).deleteByActorByScopes(actor, List.of(scope));
				/*
				Arguments<Scope> arguments = new Arguments<Scope>();
				arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ActorScopeBusiness.DELETE_BY_ACTOR_BY_SCOPES));
				arguments.setDeletables(List.of(scope));
				EntitySaver.getInstance().save(Scope.class, arguments);
				*/
				return null;
			}
		});
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
		
		if(CollectionHelper.isNotEmpty(scopeTypes)) {		
			tabMenu = new DefaultMenuModel();
			tabMenuActiveIndex = ((List<ScopeType>)scopeTypes).indexOf(scopeType);	
			for(ScopeType index : scopeTypes) {
				DefaultMenuItem item = new DefaultMenuItem();
				item.setValue(index.getName());
				item.setOutcome(getListOutcome());
				if(actor!=null)
					item.setParam(ParameterName.ENTITY_IDENTIFIER.getValue(), actor.getIdentifier());
				item.setParam(ParameterName.stringify(ScopeType.class), index.getIdentifier());
				tabMenu.addElement(item);
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Layout buildLayout(Collection<Map<?, ?>> cellsMaps) {
		if(actor != null)
			((List)cellsMaps).add(isStatic ? 0 : 2,MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"tab_menu",Cell.FIELD_WIDTH,12));
		return super.buildLayout(cellsMaps);
	}
	
	@Override
	protected Boolean isShowEditCommandButton() {
		return Boolean.FALSE;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return Helper.formatActorListScopesWindowTitle(actor, isStatic);
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListScopesView";
	}
	
	@Override
	protected String getEditOutcome() {
		return "actorEditScopesView";
	}
	
	/**/
	
	public class DataTableListenerImpl extends ScopeListPage.DataTableListenerImpl implements Serializable{
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ScopeListPage.LazyDataModelListenerImpl implements Serializable {		
		private Actor actor;
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Scope> lazyDataModel) {
			if(ScopeType.isCodeEqualsSECTION(scopeType))
				return ScopeQuerier.QUERY_IDENTIFIER_READ_VISIBLE_SECTIONS_BY_ACTOR_CODE;
			else if(ScopeType.isCodeEqualsUA(scopeType))
				return ScopeQuerier.QUERY_IDENTIFIER_READ_VISIBLE_ADMINISTRATIVE_UNITS_WITH_SECTION_BY_ACTOR_CODE;		
			return ScopeQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_BY_TYPES_CODES;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(actor != null) {
				if(filter == null)
					filter = new Filter.Dto();
				if(ScopeType.isCodeEqualsSECTION(scopeType) || ScopeType.isCodeEqualsUA(scopeType)) {
					filter = new Filter.Dto();
					filter.addField(ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, actor.getCode());
				}else
					filter.addField(ScopeQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()));
			}
			return filter;
		}
	}
}