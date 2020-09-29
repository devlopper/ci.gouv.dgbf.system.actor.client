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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Service;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ServiceQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ServiceListPage extends AbstractEntityListPageContainerManagedImpl<Service> implements Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();				
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des services";
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();		
		return dataTable;
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Service.class);
		Collection<String> columnsFieldsNames = CollectionHelper.listOf(Service.FIELD_MODULE_CODE_NAME,Service.FIELD_CODE,Service.FIELD_NAME,Service.FIELD_DEFINED
				,Service.FIELD_SECURED);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
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
			if(Service.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "250");
			}else if(Service.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(Service.FIELD_MODULE_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Module");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Service.FIELD_DEFINED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Défini ?");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Service.FIELD_SECURED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Sécurisé ?");
				map.put(Column.FIELD_WIDTH, "100");
			}
			return map;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			if(record instanceof Service && Service.FIELD_DEFINED.equals(column.getFieldName()))
				return Boolean.TRUE.equals(((Service)record).getDefined()) ? "Oui" : "Non";
			if(record instanceof Service && Service.FIELD_SECURED.equals(column.getFieldName()))
				return Boolean.TRUE.equals(((Service)record).getSecured()) ? "Oui" : "Non";
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Service> implements Serializable {
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Service> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Service> lazyDataModel) {
			return ServiceQuerier.QUERY_IDENTIFIER_READ_WITH_EXTERNAL_INFOS;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Service> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			
			return filter;
		}
	}
}