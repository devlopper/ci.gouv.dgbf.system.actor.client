package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
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
		Collection<String> columnsNames = CollectionHelper.listOf(Scope.FIELD_CODE,Scope.FIELD_NAME);
		if(ScopeType.isCodeEqualsUA(scopeType))
			columnsNames.add(Scope.FIELD_SECTION_AS_STRING);
		DataTable dataTable = buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl().setScopeType(scopeType)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setScopeType(scopeType)
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsNames);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des domaines";
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Scope.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(Scope.FIELD_CODE,Scope.FIELD_NAME));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	public static DataTable buildDataTableWithScopeType(ScopeType scopeType,Object...objects) {
		Collection<String> columnsNames = CollectionHelper.listOf(Scope.FIELD_CODE,Scope.FIELD_NAME);
		if(ScopeType.isCodeEqualsUA(scopeType))
			columnsNames.add(Scope.FIELD_SECTION_AS_STRING);
		return buildDataTable(DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsNames);
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private ScopeType scopeType;
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(ScopeType.isCodeEqualsSECTION(scopeType))
				dataTable.getOrderNumberColumn().setWidth("20");
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(Scope.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, ScopeType.isCodeEqualsSECTION(scopeType) ? "70" : "100");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_CODE);
			}else if(Scope.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_NAME);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
			}else if(Scope.FIELD_TYPE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_TYPE);
			}else if(Scope.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
			}else if(Scope.FIELD_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, scopeType == null ? "Domaine" : scopeType.getName());
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ScopeQuerier.PARAMETER_NAME_THIS);
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Scope> implements Serializable {
		protected ScopeType scopeType;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Scope> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Scope> lazyDataModel) {
			if(ScopeType.isCodeEqualsUA(scopeType))
				return ScopeQuerier.QUERY_IDENTIFIER_READ_WHERE_TYPE_IS_UA_AND_FILTER;
			return ScopeQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(filter == null)
				filter = new Filter.Dto();
			if(ScopeType.isCodeEqualsUA(scopeType)) {		
				
			}else {
				if(Boolean.TRUE.equals(isFieldFilterable(ScopeQuerier.PARAMETER_NAME_TYPE_CODE)))
					filter.addField(ScopeQuerier.PARAMETER_NAME_TYPE_CODE, scopeType.getCode());
				else
					filter.addField(ScopeQuerier.PARAMETER_NAME_TYPES_CODES, List.of(scopeType.getCode()));
			}
			if(Boolean.TRUE.equals(isFieldFilterable(ScopeQuerier.PARAMETER_NAME_CODE)))
				filter.addField(ScopeQuerier.PARAMETER_NAME_CODE, MapHelper.readByKey(lazyDataModel.get__filters__(), ScopeQuerier.PARAMETER_NAME_CODE));
			if(Boolean.TRUE.equals(isFieldFilterable(ScopeQuerier.PARAMETER_NAME_NAME)))
				filter.addField(ScopeQuerier.PARAMETER_NAME_NAME, MapHelper.readByKey(lazyDataModel.get__filters__(), ScopeQuerier.PARAMETER_NAME_NAME));
			return filter;
		}
		
		protected Boolean isFieldFilterable(String fieldName) {
			return Boolean.TRUE;
		}
	}
}