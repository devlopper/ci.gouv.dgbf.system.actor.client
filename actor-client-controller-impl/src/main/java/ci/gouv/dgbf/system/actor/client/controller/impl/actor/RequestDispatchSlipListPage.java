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
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipListPage extends AbstractEntityListPageContainerManagedImpl<RequestDispatchSlip> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des bordereaux de demandes";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Class<?> pageClass = (Class<?>) MapHelper.readByKey(arguments, RequestDispatchSlipListPage.class);
		LazyDataModelListenerImpl lazyDataModelListener = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListener == null)
			lazyDataModelListener = new LazyDataModelListenerImpl();
		//Class<?> pageClass = (Class<?>) MapHelper.readByKey(arguments, RequestListPage.class);
		List<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(RequestDispatchSlip.FIELD_CODE,RequestDispatchSlip.FIELD_NAME,RequestDispatchSlip.FIELD_FUNCTION));
		//columnsFieldsNames.addAll(0, List.of(Request.FIELD_NAMES));
			//if(Boolean.TRUE.equals(lazyDataModelListener.getProcessingDateIsNotNullable()))
			//	columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
			
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, RequestDispatchSlip.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		if(pageClass == null || RequestDispatchSlipListPage.class.equals(pageClass)) {
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Nouveau bordereau",MenuItem.FIELD_ICON,"fa fa-plus",MenuItem.FIELD___OUTCOME__
					,RequestDispatchSlipEditPage.OUTCOME,MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.NAVIGATE_TO_VIEW);
			dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestDispatchSlipReadPage.OUTCOME, MenuItem.FIELD_VALUE,"Ouvrir",MenuItem.FIELD_ICON,"fa fa-eye");
			//dataTable.addRecordMenuItemByArgumentsOpenViewInDialogRead();
			//dataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
			//dataTable.addRecordMenuItemByArgumentsNavigateToView(Action.UPDATE,RequestDispatchSlipEditPage.OUTCOME, MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-edit");
			
			//dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestDispatchSlipEditPage.OUTCOME, MenuItem.FIELD_VALUE,"Transmettre",MenuItem.FIELD_ICON,"fa fa-send");
			//dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter",MenuItem.FIELD_ICON,"fa fa-file");
		}
		/*if(pageClass == null || UserRequestsPage.class.equals(pageClass)) {
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("myAccountRequestReadView", MenuItem.FIELD_VALUE,"Consulter",MenuItem.FIELD_ICON,"fa fa-eye");
		}else {
			
		}
		*/
		
		//dataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
		//if(Boolean.TRUE.equals(lazyDataModelListener.getProcessingDateIsNullable()))
			
		//dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();		
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
			if(RequestDispatchSlip.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Numéro");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(RequestDispatchSlip.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(RequestDispatchSlip.FIELD_FUNCTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Catégorie de fonction budgétaire");
			}else if(RequestDispatchSlip.FIELD_COMMENT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Commentaire");
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<RequestDispatchSlip> implements Serializable {
		private Boolean sendingDateIsNullable,sendingDateIsNotNullable;
		private Boolean processingDateIsNullable,processingDateIsNotNullable;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			return RequestDispatchSlipQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI;
		}
		
		public Filter.Dto instantiateFilter(LazyDataModel<RequestDispatchSlip> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			/*filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SENDING_DATE_IS_NULL, sendingDateIsNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SENDING_DATE_IS_NOT_NULL, sendingDateIsNotNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NULL, processingDateIsNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NOT_NULL, processingDateIsNotNullable, filter);
			*/
			return filter;
		}
		
		public static final String FIELD_PROCESSING_DATE_IS_NULLABLE = "processingDateIsNullable";
		public static final String FIELD_PROCESSING_DATE_IS_NOT_NULLABLE = "processingDateIsNotNullable";
	}
	
	public static final String OUTCOME = "requestDispatchSlipListView";
}