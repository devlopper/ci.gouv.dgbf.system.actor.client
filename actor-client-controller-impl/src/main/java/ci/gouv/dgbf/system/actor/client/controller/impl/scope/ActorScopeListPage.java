package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorScopeListPage extends AbstractEntityListPageContainerManagedImpl<ActorScope> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable();
		@SuppressWarnings("unchecked")
		LazyDataModel<ActorScope> lazyDataModel = (LazyDataModel<ActorScope>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(ActorScopeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ACTOR_CODE_ASCENDING_BY_SCOPE_CODE_ASCENDING);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des affectations";
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(ActorScope.FIELD_ACTOR,ActorScope.FIELD_SCOPE,ActorScope.FIELD_VISIBLE);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,ActorScope.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener/*,DataTable.FIELD_STICKY_HEADER,Boolean.TRUE*/);
		dataTable.getOrderNumberColumn().setWidth("20");
		
		LazyDataModel<ActorScope> lazyDataModel = (LazyDataModel<ActorScope>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(ActorScopeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ACTOR_CODE_ASCENDING_BY_SCOPE_CODE_ASCENDING);
		}
		lazyDataModel.setListener(lazyDataModelListener);
		return dataTable;
	}
	
	public static DataTable instantiateDataTable() {
		return instantiateDataTable(null, null, null);
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {

		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(ActorScope.FIELD_ACTOR.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Compte utilisateur");
			}else if(ActorScope.FIELD_SCOPE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Domaine");
			}else if(ActorScope.FIELD_VISIBLE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Visible ?");
				map.put(Column.FIELD_WIDTH, "150");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ActorScope> implements Serializable {
		protected Actor actor;
		protected ScopeType scopeType;
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ActorScope> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(actor != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(ActorScopeQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()));
			}
			if(scopeType != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(ActorScopeQuerier.PARAMETER_NAME_SCOPE_TYPES_CODES, List.of(scopeType.getCode()));
			}
			return filter;
		}
	}
}