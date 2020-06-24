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
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ScopeListPage extends AbstractEntityListPageContainerManagedImpl<Scope> implements Serializable {

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
				item.setOutcome("scopeListView");
				item.setParam(ParameterName.stringify(ScopeType.class), index.getIdentifier());
				tabMenu.addElement(item);
			}
		}
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable(null,null,new LazyDataModelListenerImpl().setScopeType(scopeType));
		@SuppressWarnings("unchecked")
		LazyDataModel<Scope> lazyDataModel = (LazyDataModel<Scope>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des domaines";
	}
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(Scope.FIELD_CODE,Scope.FIELD_NAME);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Scope.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener);
		
		//dataTable.addRecordMenuItemByArgumentsOpenViewInDialogRead();
		
		LazyDataModel<Scope> lazyDataModel = (LazyDataModel<Scope>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES);
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
			if(Scope.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Scope.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(Scope.FIELD_TYPE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Scope> implements Serializable {
		protected ScopeType scopeType;
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(scopeType != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(ScopeQuerier.PARAMETER_NAME_TYPES_CODES, List.of(scopeType.getCode()));
			}
			return filter;
		}
	}
	
}