package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestListPage extends AbstractEntityListPageContainerManagedImpl<Request> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des demandes";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		LazyDataModelListenerImpl lazyDataModelListener = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListener == null)
			lazyDataModelListener = new LazyDataModelListenerImpl();
		//Class<?> pageClass = (Class<?>) MapHelper.readByKey(arguments, RequestListPage.class);
		List<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(Request.FIELD_CODE,Request.FIELD_TYPE_AS_STRING,Request.FIELD_CREATION_DATE_AS_STRING,Request.FIELD_STATUS_AS_STRING
				,Request.FIELD_PROCESSING_DATE_AS_STRING));
		//columnsFieldsNames.addAll(0, List.of(Request.FIELD_NAMES));
			//if(Boolean.TRUE.equals(lazyDataModelListener.getProcessingDateIsNotNullable()))
			//	columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
			
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		/*if(pageClass == null || UserRequestsPage.class.equals(pageClass)) {
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("myAccountRequestReadView", MenuItem.FIELD_VALUE,"Consulter",MenuItem.FIELD_ICON,"fa fa-eye");
		}else {
			
		}
		*/
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogRead();
		//if(Boolean.TRUE.equals(lazyDataModelListener.getProcessingDateIsNullable()))
			dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter",MenuItem.FIELD_ICON,"fa fa-file");
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
			if(Request.FIELD_ACTOR_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Compte");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Request.FIELD_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Commentaire");
			}else if(Request.FIELD_CREATION_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Créé le");
				map.put(Column.FIELD_WIDTH, "130");
			}else if(Request.FIELD_PROCESSING_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Traité le");
				map.put(Column.FIELD_WIDTH, "130");
			}else if(Request.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
			}else if(Request.FIELD_FUNCTIONS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s)");
				
			}else if(Request.FIELD_STATUS_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Statut");
				map.put(Column.FIELD_WIDTH, "130");
			}
			return map;
		}
		
		public java.lang.Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Request> implements Serializable {
		private String actorIdentifier;
		private Boolean processingDateIsNullable,processingDateIsNotNullable;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Request> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Request> lazyDataModel) {
			return RequestQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI;
		}
		
		public Filter.Dto instantiateFilter(LazyDataModel<Request> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(StringHelper.isNotBlank(actorIdentifier)) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(RequestQuerier.PARAMETER_NAME_ACTOR_IDENTIFIER, actorIdentifier);
			}
			if(processingDateIsNullable != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NULL, processingDateIsNullable);
			}
			if(processingDateIsNotNullable != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NOT_NULL, processingDateIsNotNullable);
			}
			return filter;
		}
		
		public static final String FIELD_PROCESSING_DATE_IS_NULLABLE = "processingDateIsNullable";
		public static final String FIELD_PROCESSING_DATE_IS_NOT_NULLABLE = "processingDateIsNotNullable";
	}
	
	public static final String OUTCOME = "requestListView";
}