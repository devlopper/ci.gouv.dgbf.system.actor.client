package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.server.business.api.AccountRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AccountRequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class AccountRequestListPage extends AbstractEntityListPageContainerManagedImpl<AccountRequest> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des demandes de comptes";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, AccountRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(AccountRequest.FIELD_FIRST_NAME
				,AccountRequest.FIELD_LAST_NAMES,AccountRequest.FIELD_SERVICE,AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS,AccountRequest.FIELD_CREATION_DATE_AS_STRING));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		dataTable.addRecordMenuItemByArgumentsExecuteFunction("Accepter", "fa fa-check", new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				AccountRequest accountRequest = (AccountRequest) action.get__argument__();
				EntitySaver.getInstance().save(AccountRequest.class, new Arguments<AccountRequest>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(AccountRequestBusiness.ACCEPT))
						.setRepresentation(AccountRequestRepresentation.getProxy())
						.addCreatablesOrUpdatables(accountRequest));
				return null;
			}
		});
		dataTable.addRecordMenuItemByArgumentsExecuteFunction("Rejecter", "fa fa-trash", new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				AccountRequest accountRequest = (AccountRequest) action.get__argument__();
				EntitySaver.getInstance().save(AccountRequest.class, new Arguments<AccountRequest>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(AccountRequestBusiness.REJECT))
						.setRepresentation(AccountRequestRepresentation.getProxy())
						.addCreatablesOrUpdatables(accountRequest));
				return null;
			}
		});
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
			if(AccountRequest.FIELD_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_NAMES);
			}else if(AccountRequest.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_FIRST_NAME);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(AccountRequest.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prenom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_LAST_NAMES);
			}else if(AccountRequest.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS);
				map.put(Column.FIELD_WIDTH, "250");
			}else if(AccountRequest.FIELD_CREATION_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Date");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(AccountRequest.FIELD_SERVICE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Service");
				
			}
			return map;
		}
		
		@Override
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(column != null && AccountRequest.FIELD_SERVICE.equals(column.getFieldName())) {
				AccountRequest accountRequest = (AccountRequest) record;
				return Helper.formatService(accountRequest.getAdministrativeUnitAsString(), accountRequest.getAdministrativeFunction(), accountRequest.getSectionAsString());
			}
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<AccountRequest> implements Serializable {
		@Override
		public Boolean getReaderUsable(LazyDataModel<AccountRequest> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<AccountRequest> lazyDataModel) {
			return AccountRequestQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
		}
	}
}