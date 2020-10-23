package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.FunctionType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class FunctionTypeListPage extends AbstractEntityListPageContainerManagedImpl<FunctionType> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable();
		@SuppressWarnings("unchecked")
		LazyDataModel<FunctionType> lazyDataModel = (LazyDataModel<FunctionType>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(FunctionTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des types de catégories de fonctions";
	}
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(FunctionType.FIELD_CODE,FunctionType.FIELD_NAME);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,FunctionType.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener
				,DataTable.FIELD_STYLE_CLASS,"cyk-ui-datatable-footer-visibility-hidden");
		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		
		LazyDataModel<FunctionType> lazyDataModel = (LazyDataModel<FunctionType>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(FunctionTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
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
			if(FunctionType.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(FunctionType.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<FunctionType> implements Serializable {
		
	}
}