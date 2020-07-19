package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.RejectedAccountRequest;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RejectedAccountRequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RejectedAccountRequestListPage extends AbstractEntityListPageContainerManagedImpl<RejectedAccountRequest> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des demandes de comptes rejetées";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, RejectedAccountRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(RejectedAccountRequest.FIELD_FIRST_NAME
				,RejectedAccountRequest.FIELD_LAST_NAMES,RejectedAccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS,RejectedAccountRequest.FIELD_REQUEST_DATE_AS_STRING
				,RejectedAccountRequest.FIELD_DATE_AS_STRING));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(RejectedAccountRequest.FIELD_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RejectedAccountRequestQuerier.PARAMETER_NAME_NAMES);
			}else if(RejectedAccountRequest.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RejectedAccountRequestQuerier.PARAMETER_NAME_FIRST_NAME);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(RejectedAccountRequest.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prenom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RejectedAccountRequestQuerier.PARAMETER_NAME_LAST_NAMES);
			}else if(RejectedAccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RejectedAccountRequestQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS);
				map.put(Column.FIELD_WIDTH, "250");
			}else if(RejectedAccountRequest.FIELD_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Date de rejet");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(RejectedAccountRequest.FIELD_REQUEST_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Date de demande");
				map.put(Column.FIELD_WIDTH, "150");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<RejectedAccountRequest> implements Serializable {
		@Override
		public Boolean getReaderUsable(LazyDataModel<RejectedAccountRequest> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<RejectedAccountRequest> lazyDataModel) {
			return RejectedAccountRequestQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
		}
	}
}