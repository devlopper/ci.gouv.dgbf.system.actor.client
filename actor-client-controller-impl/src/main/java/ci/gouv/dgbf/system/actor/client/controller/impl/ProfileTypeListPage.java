package ci.gouv.dgbf.system.actor.client.controller.impl;

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

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ProfileTypeListPage extends AbstractEntityListPageContainerManagedImpl<ProfileType> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable();
		@SuppressWarnings("unchecked")
		LazyDataModel<Profile> lazyDataModel = (LazyDataModel<Profile>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des types de profiles";
	}
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(ProfileType.FIELD_CODE,ProfileType.FIELD_NAME);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,ProfileType.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener);
		
		//dataTable.addRecordMenuItemByArgumentsOpenViewInDialogRead();
		
		LazyDataModel<ProfileType> lazyDataModel = (LazyDataModel<ProfileType>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
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
			if(ProfileType.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ProfileType.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ProfileType> implements Serializable {
		
	}
}