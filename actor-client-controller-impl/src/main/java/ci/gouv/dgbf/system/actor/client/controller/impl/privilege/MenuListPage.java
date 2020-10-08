package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Menu;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.MenuQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class MenuListPage extends AbstractEntityListPageContainerManagedImpl<Menu> implements Serializable {

	
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();				
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des menus";
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		return dataTable;
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Menu.class);
		Collection<String> columnsFieldsNames = CollectionHelper.listOf(Menu.FIELD_MODULE_CODE_NAME,Menu.FIELD_SERVICE_CODE_NAME,Menu.FIELD_CODE,Menu.FIELD_NAME
				,Menu.FIELD_UNIFORM_RESOURCE_IDENTIFIER,Menu.FIELD_PROFILES_AS_STRING,Menu.FIELD_DEFINED);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(Menu.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(Menu.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(Menu.FIELD_SERVICE_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Service");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Menu.FIELD_MODULE_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Module");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Menu.FIELD_DEFINED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Défini ?");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Menu.FIELD_UNIFORM_RESOURCE_IDENTIFIER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Lien");
				
			}else if(Menu.FIELD_PROFILES_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Profile(s)");
				
			}
			return map;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			if(record instanceof Menu && Menu.FIELD_DEFINED.equals(column.getFieldName()))
				return Boolean.TRUE.equals(((Menu)record).getDefined()) ? "Oui" : "Non";
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Menu> implements Serializable {
		
		private String serviceIdentifier;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Menu> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Menu> lazyDataModel) {
			if(StringHelper.isNotBlank(serviceIdentifier))
				return MenuQuerier.QUERY_IDENTIFIER_READ_WITH_ALL_BY_SERVICE_IDENTIFIER;
			return MenuQuerier.QUERY_IDENTIFIER_READ_WITH_ALL;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Menu> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			if(StringHelper.isNotBlank(serviceIdentifier)) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(MenuQuerier.PARAMETER_NAME_SERVICE_IDENTIFIER, serviceIdentifier);
			}
			return filter;
		}
	}
}