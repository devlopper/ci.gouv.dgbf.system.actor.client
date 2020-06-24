package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
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
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorListScopesPage extends AbstractActorListPrivilegesOrScopesPage<ActorScope> implements Serializable {

	private ScopeType scopeType;
	private MenuModel tabMenu;
	private Integer tabMenuActiveIndex;
	
	@Override
	protected void __listenPostConstruct__() {		
		scopeType = WebController.getInstance().getRequestParameterEntityAsParent(ScopeType.class);
		Collection<ScopeType> scopeTypes = EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		if(scopeType == null)
			scopeType = CollectionHelper.getFirst(scopeTypes);
		super.__listenPostConstruct__();		
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
			((List)cellsMaps).add(2,MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"tab_menu",Cell.FIELD_WIDTH,12));
		return super.buildLayout(cellsMaps);
	}
	
	@Override
	protected DataTable instantiateDataTable() {
		return ActorScopeListPage.instantiateDataTable(List.of(ActorScope.FIELD_SCOPE,ActorScope.FIELD_VISIBLE),new DataTableListenerImpl(),new LazyDataModelListenerImpl()
				.setActor(actor).setScopeType(scopeType));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Affectation des domaines";
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListScopesView";
	}
	
	@Override
	protected String getCreateOutcome() {
		return "actorCreateScopesView";
	}
	
	@Override
	protected Map<String, List<String>> getCreateParameters() {
		return scopeType == null ? null : Map.of(ParameterName.stringify(ScopeType.class),List.of(scopeType.getCode()));
	}
	
	/**/
	
	public class DataTableListenerImpl extends ActorScopeListPage.DataTableListenerImpl implements Serializable{
		
	}
	
	public static class LazyDataModelListenerImpl extends ActorScopeListPage.LazyDataModelListenerImpl implements Serializable {		
		@Override
		public Arguments<ActorScope> instantiateArguments(LazyDataModel<ActorScope> lazyDataModel) {
			Arguments<ActorScope> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ActorScopeQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_BY_SCOPE_TYPES_CODES);
			return arguments;
		}
	}
}