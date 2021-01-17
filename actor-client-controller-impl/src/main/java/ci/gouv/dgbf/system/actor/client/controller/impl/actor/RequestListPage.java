package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestListPage extends AbstractEntityListPageContainerManagedImpl<Request> implements Serializable {

	private LinkedHashMap<String, String> legends = new LinkedHashMap<>();
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		legends.put("Gestionnaire", "#F1EEF6");
		legends.put("Ordonnateur", "#BDC9E1");
		legends.put("Contrôleur Financier", "#74A9CF");
		legends.put("Comptable", "#2B8CBE");
		legends.put("Assistant", "#EDCADD");
	}
	
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
		columnsFieldsNames.addAll(List.of(Request.FIELD_CODE,Request.FIELD_FIRST_NAME,Request.FIELD_LAST_NAMES,Request.FIELD_ELECTRONIC_MAIL_ADDRESS
				,Request.FIELD_MOBILE_PHONE_NUMBER,Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING
				,Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS
				,Request.FIELD_CREATION_DATE_AS_STRING,Request.FIELD_STATUS_AS_STRING,Request.FIELD_PROCESSING_DATE_AS_STRING));
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
		//dataTable.addRecordMenuItemByArgumentsNavigateToViewRead();
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
			}else if(Request.FIELD_ACTOR_NAMES.equals(fieldName)) {
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
			}else if(Request.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Numéro");
				map.put(Column.FIELD_WIDTH, "120");
			}else if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s) budgétaire(s)");
			}else if(Request.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Request.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Tel. Mobile");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "UA");
				map.put(Column.FIELD_WIDTH, "100");
			}
			return map;
		}
		/*
		@Override
		public String getStyleClassByRecord(Object record, Integer recordIndex) {
			if(record instanceof Request) {
				Request request = (Request) record;
				if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsAccountingHolder()))
					return "cyk-scope-function-cpt";
				if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsFinancialControllerHolder()))
					return "cyk-scope-function-cf";
				if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsAuthorizingOfficerHolder()))
					return "cyk-scope-function-ord";
				if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsCreditManagerHolder()))
					return "cyk-scope-function-gc";
				return "cyk-scope-function-assistant";
			}
			return super.getStyleClassByRecord(record, recordIndex);
		}
		*/
		@Override
		public String getStyleClassByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(record instanceof Request) {
				Request request = (Request) record;
				if(columnIndex != null && columnIndex == 0) {					
					if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsAccountingHolder()))
						return "cyk-scope-function-cpt";
					if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsFinancialControllerHolder()))
						return "cyk-scope-function-cf";
					if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsAuthorizingOfficerHolder()))
						return "cyk-scope-function-ord";
					if(Boolean.TRUE.equals(request.getHasBudgetaryScopeFunctionWhereFunctionCodeIsCreditManagerHolder()))
						return "cyk-scope-function-gc";
					return "cyk-scope-function-assistant";
				}
			}
			return super.getStyleClassByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS.equals(column.getFieldName())) {
				Request request = (Request)record;
				Collection<String> strings = null;
				if(request.getStatus().getCode().equals(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.CODE_ACCEPTED)
						&& CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsGrantedAsStrings())) {
					strings =  ((Request)record).getBudgetariesScopeFunctionsGrantedAsStrings();
				}else if(CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsAsStrings())){
					strings =  ((Request)record).getBudgetariesScopeFunctionsAsStrings();
				}
				if(CollectionHelper.isNotEmpty(strings))
					return strings.stream().map(x -> StringUtils.substringBefore(x, " ")).collect(Collectors.joining(","));
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(column.getFieldName())) {
				return StringUtils.substringBefore(((Request)record).getAdministrativeUnitAsString()," ");
			}
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			if(record instanceof Request) {
				Request request = (Request)record;
				if(request.getStatus() != null) {
					List<String> strings = null;
					if(request.getStatus().getCode().equals(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.CODE_ACCEPTED)
							&& CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsGrantedAsStrings())) {
						strings = ((Request)record).getBudgetariesScopeFunctionsGrantedAsStrings().stream().map(x -> x.toString()).collect(Collectors.toList());
					}else if(CollectionHelper.isNotEmpty(request.getBudgetariesScopeFunctionsAsStrings())){
						strings = ((Request)record).getBudgetariesScopeFunctionsAsStrings().stream().map(x -> x.toString()).collect(Collectors.toList());
					}
					if(CollectionHelper.isNotEmpty(strings))
						if(strings.size() == 1)
							return String.format(TOOLTIP_FORMAT, request.getAdministrativeUnitAsString(),strings.get(0));
						else
							return String.format(TOOLTIP_FORMAT, request.getAdministrativeUnitAsString(),SEPARATOR+StringUtils.join(strings,SEPARATOR));
				}
			}
			return super.getTooltipByRecord(record, recordIndex);
		}
		
		public java.lang.Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
		
		private static final String SEPARATOR = "<br/>"+StringUtils.repeat("&nbsp;", 10);
		private static final String TOOLTIP_FORMAT = "Unité administrative : %s<br/>Fonction(s) budgétaire(s) :%s";
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