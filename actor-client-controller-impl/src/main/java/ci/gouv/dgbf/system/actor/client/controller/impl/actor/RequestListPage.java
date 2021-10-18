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
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.__kernel__.value.ValueHelper;
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
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.persistence.query.Filter;

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

	private RequestFilterController filterController;
	private LinkedHashMap<String, String> legends = new LinkedHashMap<>();
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new RequestFilterController().initialize();
	}
	
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
		DataTable dataTable = buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.Request.LABEL);
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		ContentType contentType = (ContentType) MapHelper.readByKey(arguments, ContentType.class);
		
		RequestFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (RequestFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = (RequestFilterController) MapHelper.readByKey(arguments, RequestFilterController.class));
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new RequestFilterController());
		lazyDataModelListenerImpl.enableFilterController();
		if(StringHelper.isBlank(filterController.getOnSelectRedirectorArguments(Boolean.TRUE).getOutcome())) {
			String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
			filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		}
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl().setContentType(contentType).setFilterController(filterController));
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_SORT_MODE, "multiple");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		
		if(AbstractCollection.RenderType.OUTPUT.equals(dataTable.getRenderType())) {
			dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestReadPage.OUTCOME, MenuItem.FIELD_VALUE,"Consulter",MenuItem.FIELD_ICON,"fa fa-eye");
			if(Boolean.TRUE.equals(SessionManager.getInstance().isUserHasOneOfRoles(Profile.CODE_ADMINISTRATEUR,Profile.CODE_CHARGE_ETUDE_DAS))) {
				//dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter",MenuItem.FIELD_ICON,"fa fa-file");
				if((filterController != null && Boolean.FALSE.equals(filterController.getProcessedInitial())) || ContentType.TO_PROCESS.equals(contentType)) {
					dataTable.addRecordMenuItemByArgumentsNavigateToView(null,RequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter",MenuItem.FIELD_ICON,"fa fa-file");
				}else if((filterController != null && Boolean.TRUE.equals(filterController.getProcessedInitial())) || ContentType.PROCESSED.equals(contentType)) {
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
		//dataTable.getContentOutputPanel().setDeferred(Boolean.TRUE);
		dataTable.getFilterController().getLayout().setDeferred(Boolean.TRUE);
		dataTable.getOrderNumberColumn().setWidth("20");
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private RequestFilterController filterController;
		private ContentType contentType;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(Request.FIELD_ACTOR_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Compte");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Request.FIELD_FIRST_NAME_AND_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
				map.put(Column.FIELD_WIDTH, "200");
				map.put(Column.FIELD_SORT_BY, fieldName);
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Commentaire");
			}else if(Request.FIELD_CREATION_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Créée le");
				map.put(Column.FIELD_WIDTH, "130");
				if(filterController != null)
					map.put(Column.FIELD_VISIBLE, filterController.getProcessedInitial() == null || !filterController.getProcessedInitial());
				map.put(Column.FIELD_SORT_BY, Request.FIELD_CREATION_DATE);
			}else if(Request.FIELD_PROCESSING_DATE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Traitée le");
				map.put(Column.FIELD_WIDTH, "130");
				if(filterController != null)
					map.put(Column.FIELD_VISIBLE, Boolean.TRUE.equals(filterController.getProcessedInitial()));
				map.put(Column.FIELD_SORT_BY, Request.FIELD_PROCESSING_DATE);
			}else if(Request.FIELD_TYPE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type");
			}else if(Request.FIELD_FUNCTIONS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s)");
				
			}else if(Request.FIELD_STATUS_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Statut");
				map.put(Column.FIELD_WIDTH, "70");
				map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
			}else if(Request.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Numéro");
				map.put(Column.FIELD_WIDTH, "110");
				map.put(Column.FIELD_SORT_BY, fieldName);
			}else if(Request.FIELD_BUDGETARIES_SCOPE_FUNCTIONS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s) budgétaire(s)");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(Request.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.FIELD_WIDTH, "200");
				map.put(Column.FIELD_SORT_BY, fieldName);
			}else if(Request.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Tel. Mobile");
				map.put(Column.FIELD_WIDTH, "100");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_DISPATCH_SLIP_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "N° Bordereau");
				map.put(Column.FIELD_WIDTH, "100");
				if(filterController != null && filterController.getDispatchSlipExists() != null && filterController.getDispatchSlipExists())
					map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
				else
					map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "U.A."/*"Unité administrative"*/);
				map.put(Column.FIELD_WIDTH, "80");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "65");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.FIELD_SORT_BY, fieldName);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(Request.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prénoms");
				map.put(Column.FIELD_SORT_BY, fieldName);
				map.put(Column.FIELD_WIDTH, "150");
			}else if(Request.FIELD_REGISTRATION_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Matricule");
				map.put(Column.FIELD_WIDTH, "75");
				map.put(Column.FIELD_SORT_BY, fieldName);
			}else if(Request.FIELD_ACCOUNT_CREATION_MESSAGE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Création compte");
				map.put(Column.FIELD_WIDTH, "200");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Request.FIELD_SCOPE_FUNCTIONS_CODES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Poste(s) demandé(s)");
				map.put(Column.FIELD_WIDTH, "200");
				if(filterController != null)
					map.put(Column.FIELD_VISIBLE, filterController.getProcessedInitial() == null || !filterController.getProcessedInitial());
			}else if(Request.FIELD_GRANTED_SCOPE_FUNCTIONS_CODES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Poste(s) accordé(s)");
				map.put(Column.FIELD_WIDTH, "200");
				if(filterController != null)
					map.put(Column.FIELD_VISIBLE, Boolean.TRUE.equals(filterController.getProcessedInitial()));
			}
			return map;
		}
		
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
			if(Request.FIELD_SCOPE_FUNCTIONS_CODES.equals(column.getFieldName())) {
				Request request = (Request)record;
				Collection<String> strings = request.getScopeFunctionsCodes();
				if(CollectionHelper.isNotEmpty(strings))
					return strings.stream().map(x -> StringUtils.substringBefore(x, " ")).collect(Collectors.joining(","));
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
			columnsFieldsNames.addAll(List.of(Request.FIELD_CREATION_DATE_AS_STRING));
			if(ContentType.TO_PROCESS.equals(contentType)) {
				
			}else if(ContentType.PROCESSED.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
			}else if(ContentType.ALL.equals(contentType)) {
				columnsFieldsNames.addAll(List.of(Request.FIELD_PROCESSING_DATE_AS_STRING));
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
		
		private static final String SEPARATOR = "<br/>"+StringUtils.repeat("&nbsp;", 10);
		private static final String TOOLTIP_FORMAT = "Section : %s<br/>Unité administrative : %s<br/>Fonction(s) budgétaire(s) :%s";
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Request> implements Serializable {
		private Collection<String> excludedIdentifiers;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Request> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Request> lazyDataModel) {
			return RequestQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		public Filter.Dto instantiateFilter(LazyDataModel<Request> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			filter = RequestFilterController.populateFilter(filter, (RequestFilterController) filterController,Boolean.TRUE);
			return filter;
		}
		
		@Override
		public Arguments<Request> instantiateArguments(LazyDataModel<Request> lazyDataModel) {
			Arguments<Request> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().addProjectionsFromStrings(
					ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_IDENTIFIER
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_CODE
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_FIRST_NAME_AND_LAST_NAMES
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_REGISTRATION_NUMBER
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_ELECTRONIC_MAIL_ADDRESS
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_MOBILE_PHONE_NUMBER
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELD_DISPATCH_SLIP_CODE);
			
			arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Request
					.FIELDS_SCOPE_FUNCTIONS_CODES_IS_CREDIT_MANAGER_HOLDER_IS_AUTHORIZING_OFFICER_HOLDER_IS_FINANCIAL_CONTROLLER_HOLDER_IS_ACCOUNTING_HOLDER);
			
			Boolean processed = ((RequestFilterController)filterController).getProcessedInitial();
			if(processed == null || processed) {
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Request
						.FIELDS_SECTION_AS_CODE_ADMINISTRATIVE_UNIT_AS_CODE_TYPE_STATUS_CREATION_DATE_PROCESSING_DATE_AS_STRINGS
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Request.FIELDS_GRANTED_SCOPE_FUNCTIONS_CODES);
			}else
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Request
						.FIELDS_SECTION_AS_CODE_ADMINISTRATIVE_UNIT_AS_CODE_TYPE_STATUS_CREATION_DATE_AS_STRINGS);
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
		
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new RequestFilterController();
			filterController.build();
			return this;
		}
	}
	
	public static enum ContentType {
		TO_PROCESS,PROCESSED,ALL
	}

	public static final String OUTCOME = "requestListView";
}