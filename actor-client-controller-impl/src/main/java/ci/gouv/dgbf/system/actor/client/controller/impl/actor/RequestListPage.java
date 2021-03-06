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
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.entities.Profile;
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
	
	public static String buildWindowTitleValue(String prefix,Function function,Section section,AdministrativeUnit administrativeUnit,BudgetSpecializationUnit budgetSpecializationUnit) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(section != null) {
			if(administrativeUnit == null && budgetSpecializationUnit == null)
				strings.add(section.toString());
			else
				strings.add("Section "+section.getCode());
		}
		if(administrativeUnit != null) {
			strings.add(administrativeUnit.toString());
		}
		if(function != null) {
			strings.add(function.getName());
		}
		if(budgetSpecializationUnit != null) {
			strings.add((budgetSpecializationUnit.getCode().startsWith("1") ? "Dotation":"Programme")+" "+budgetSpecializationUnit.getCode());
		}
		return StringHelper.concatenate(strings, " | ");
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		ContentType contentType = (ContentType) MapHelper.readByKey(arguments, ContentType.class);
		//Class<?> pageClass = (Class<?>) MapHelper.readByKey(arguments, RequestListPage.class);
		LazyDataModelListenerImpl lazyDataModelListener = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListener == null)
			lazyDataModelListener = new LazyDataModelListenerImpl();
		//Class<?> pageClass = (Class<?>) MapHelper.readByKey(arguments, RequestListPage.class);
		List<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(Request.FIELD_CODE,Request.FIELD_FIRST_NAME,Request.FIELD_LAST_NAMES,Request.FIELD_REGISTRATION_NUMBER
				,Request.FIELD_ELECTRONIC_MAIL_ADDRESS,Request.FIELD_MOBILE_PHONE_NUMBER,Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING
				,Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS));
		if(ContentType.TO_PROCESS.equals(contentType)) {
			columnsFieldsNames.addAll(List.of(Request.FIELD_CREATION_DATE_AS_STRING));
		}else if(ContentType.PROCESSED.equals(contentType)) {
			columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
		}else
			columnsFieldsNames.addAll(List.of(Request.FIELD_CREATION_DATE_AS_STRING,Request.FIELD_PROCESSING_DATE_AS_STRING));		
		columnsFieldsNames.addAll(List.of(Request.FIELD_STATUS_AS_STRING));
		if(ContentType.PROCESSED.equals(contentType) || ContentType.ALL.equals(contentType)) {
			columnsFieldsNames.addAll(List.of(Request.FIELD_ACCOUNT_CREATION_MESSAGE));
		}
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl().setContentType(contentType));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		
		if(AbstractCollection.RenderType.OUTPUT.equals(dataTable.getRenderType())) {
			dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestReadPage.OUTCOME, MenuItem.FIELD_VALUE,"Consulter",MenuItem.FIELD_ICON,"fa fa-eye");
			if(Boolean.TRUE.equals(SessionManager.getInstance().isUserHasOneOfRoles(Profile.CODE_ADMINISTRATEUR,Profile.CODE_CHARGE_ETUDE_DAS))) {
				if(ContentType.TO_PROCESS.equals(contentType)) {
					dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter",MenuItem.FIELD_ICON,"fa fa-file");
				}else if(ContentType.PROCESSED.equals(contentType)) {
					dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Créer les comptes",MenuItem.FIELD_TITLE
							,"Exporter les demandes acceptées pour la création de compte",MenuItem.FIELD_USER_INTERFACE_ACTION
							,UserInterfaceAction.EXECUTE_FUNCTION,MenuItem.FIELD_ICON,"fa fa-gear"
							,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
							,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
								@Override
								protected Object __runExecuteFunction__(AbstractAction action) {
									Request request = new Request();
									Arguments<Request> arguments = new Arguments<Request>().addCreatablesOrUpdatables(request);
									arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
											.setActionIdentifier(RequestBusiness.EXPORT_FOR_ACCOUNT_CREATION));
									EntitySaver.getInstance().save(Request.class, arguments);
									return null;
								}
							});
				}
			}
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
		private ContentType contentType;
		
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
				map.put(Column.FIELD_HEADER_TEXT, "Créée le");
				map.put(Column.FIELD_WIDTH, "130");
				map.put(Column.FIELD_VISIBLE, ContentType.TO_PROCESS.equals(contentType));
				map.put(Column.FIELD_SORT_BY, Request.FIELD_CREATION_DATE);
			}else if(Request.FIELD_PROCESSING_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Traitée le");
				map.put(Column.FIELD_WIDTH, "130");
				map.put(Column.FIELD_VISIBLE, ContentType.PROCESSED.equals(contentType));
				map.put(Column.FIELD_SORT_BY, Request.FIELD_PROCESSING_DATE);
			}else if(Request.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
			}else if(Request.FIELD_FUNCTIONS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s)");
				
			}else if(Request.FIELD_STATUS_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Statut");
				map.put(Column.FIELD_WIDTH, "130");
				map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
			}else if(Request.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Numéro");
				map.put(Column.FIELD_WIDTH, "110");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RequestQuerier.PARAMETER_NAME_CODE);
			}else if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s) budgétaire(s)");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(Request.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.FIELD_WIDTH, "200");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RequestQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS);
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Tel. Mobile");
				map.put(Column.FIELD_WIDTH, "100");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Unité administrative");
				//map.put(Column.FIELD_WIDTH, "90");
			}else if(Request.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "70");
			}else if(Request.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RequestQuerier.PARAMETER_NAME_FIRST_NAME);
				map.put(Column.FIELD_SORT_BY, RequestQuerier.PARAMETER_NAME_FIRST_NAME);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Request.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prénoms");
				map.put(Column.FIELD_SORT_BY, RequestQuerier.PARAMETER_NAME_LAST_NAMES);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RequestQuerier.PARAMETER_NAME_LAST_NAMES);
				map.put(Column.FIELD_WIDTH, "150");
			}else if(Request.FIELD_REGISTRATION_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Matricule");
				map.put(Column.FIELD_WIDTH, "110");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, RequestQuerier.PARAMETER_NAME_REGISTRATION_NUMBER);
			}else if(Request.FIELD_ACCOUNT_CREATION_MESSAGE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Création compte");
				map.put(Column.FIELD_WIDTH, "200");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
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
					if(Boolean.TRUE.equals(request.getIsAccountingHolder()))
						return "cyk-scope-function-cpt";
					if(Boolean.TRUE.equals(request.getIsFinancialControllerHolder()))
						return "cyk-scope-function-cf";
					if(Boolean.TRUE.equals(request.getIsAuthorizingOfficerHolder()))
						return "cyk-scope-function-ord";
					if(Boolean.TRUE.equals(request.getIsCreditManagerHolder()))
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
			}/*else if(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(column.getFieldName())) {
				return StringUtils.substringBefore(((Request)record).getAdministrativeUnitAsString()," ");
			}*/else if(Request.FIELD_SECTION_AS_STRING.equals(column.getFieldName())) {
				return StringUtils.substringBefore(((Request)record).getSectionAsString()," ");
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
							return String.format(TOOLTIP_FORMAT, request.getSectionAsString(),request.getAdministrativeUnitAsString(),strings.get(0));
						else
							return String.format(TOOLTIP_FORMAT, request.getSectionAsString(),request.getAdministrativeUnitAsString(),SEPARATOR+StringUtils.join(strings,SEPARATOR));
				}
			}
			return super.getTooltipByRecord(record, recordIndex);
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
		
		public static Collection<String> buildColumnsNames(Function function,Section section,AdministrativeUnit administrativeUnit,BudgetSpecializationUnit budgetSpecializationUnit,ContentType contentType) {
			Collection<String> columnsFieldsNames = new ArrayList<>();
			columnsFieldsNames.addAll(List.of(Request.FIELD_CODE,Request.FIELD_FIRST_NAME,Request.FIELD_LAST_NAMES,Request.FIELD_REGISTRATION_NUMBER
					,Request.FIELD_ELECTRONIC_MAIL_ADDRESS,Request.FIELD_MOBILE_PHONE_NUMBER));
			if(section == null)
				columnsFieldsNames.add(Request.FIELD_SECTION_AS_STRING);
			if(administrativeUnit == null)
				columnsFieldsNames.add(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING);
			if(function == null)
				columnsFieldsNames.addAll(List.of(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS));
			if(ContentType.TO_PROCESS.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_CREATION_DATE_AS_STRING));
			}else if(ContentType.PROCESSED.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
			}else if(ContentType.ALL.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_CREATION_DATE_AS_STRING,Request.FIELD_PROCESSING_DATE_AS_STRING));
			}
			columnsFieldsNames.addAll(List.of(Request.FIELD_STATUS_AS_STRING));
			if(ContentType.TO_PROCESS.equals(contentType)) {
				
			}else if(ContentType.PROCESSED.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_ACCOUNT_CREATION_MESSAGE));
			}else if(ContentType.ALL.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_ACCOUNT_CREATION_MESSAGE));
			}
			return columnsFieldsNames;
		}
		
		@Override
		public String getStyleClassByRecord(Object record, Integer recordIndex) {
			if(record instanceof Request) {
				Request request = (Request) record;
				if(!Boolean.TRUE.equals(request.getAccepted()) && Boolean.TRUE.equals(request.getHasGrantedHolderScopeFunction()))
					return "cyk-background-highlight";
			}
			return super.getStyleClassByRecord(record, recordIndex);
		}
		
		private static final String SEPARATOR = "<br/>"+StringUtils.repeat("&nbsp;", 10);
		private static final String TOOLTIP_FORMAT = "Section : %s<br/>Unité administrative : %s<br/>Fonction(s) budgétaire(s) :%s";
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Request> implements Serializable {
		private String actorIdentifier,sectionIdentifier,administrativeUnitIdentifier,functionIdentifier,dispatchSlipIdentifier,statusIdentifier;
		private Boolean processingDateIsNullNullable,processingDateIsNotNullNullable;
		private Boolean dispatchSlipIsNullNullable;
		private Collection<String> excludedIdentifiers;
		
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
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_EXCLUDED_IDENTIFIERS, excludedIdentifiers, filter);		
			
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_ACTOR_IDENTIFIER, actorIdentifier, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NULL, processingDateIsNullNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NOT_NULL, processingDateIsNotNullNullable, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_DISPATCH_SLIP_IDENTIFIER, dispatchSlipIdentifier, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_DISPATCH_SLIP_IS_NULL, dispatchSlipIsNullNullable, filter);
			
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_SECTION_IDENTIFIER, sectionIdentifier, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER, functionIdentifier, filter);
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_STATUS_IDENTIFIER, statusIdentifier, filter);
			return filter;
		}
		
		@Override
		public Arguments<Request> instantiateArguments(LazyDataModel<Request> lazyDataModel) {
			Arguments<Request> arguments = super.instantiateArguments(lazyDataModel);
			ArrayList<String> list = new ArrayList<>();
			list.addAll(List.of(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS,Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_GRANTED_AS_STRINGS
					,Request.FIELD_HAS_GRANTED_HOLDER_SCOPE_FUNCTION,Request.FIELD_ACCEPTED,Request.FIELD_IS_CREDIT_MANAGER_HOLDER
					,Request.FIELD_IS_AUTHORIZING_OFFICER_HOLDER,Request.FIELD_IS_FINANCIAL_CONTROLLER_HOLDER,Request.FIELD_IS_ACCOUNTING_HOLDER));
			arguments.getRepresentationArguments().getQueryExecutorArguments().setProcessableTransientFieldsNames(list);
			return arguments;
		}
		
		public LazyDataModelListenerImpl addExcludedIdentifiers(Collection<String> identifiers) {
			if(CollectionHelper.isEmpty(identifiers))
				return this;
			if(excludedIdentifiers == null)
				excludedIdentifiers = new ArrayList<>();
			excludedIdentifiers.addAll(identifiers);
			return this;
		}
		
		public static final String FIELD_PROCESSING_DATE_IS_NULLABLE = "processingDateIsNullable";
		public static final String FIELD_PROCESSING_DATE_IS_NOT_NULLABLE = "processingDateIsNotNullable";
	}
	
	public static enum ContentType {
		TO_PROCESS,PROCESSED,ALL
	}
	
	public static final String OUTCOME = "requestListView";
}