package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.uri.UniformResourceIdentifierBuilder;
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
import org.cyk.utility.javascript.OpenWindowScriptBuilder;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.report.jasper.client.ReportServlet;
import org.primefaces.PrimeFaces;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.RequestScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestScopeFunctionListPage extends AbstractEntityListPageContainerManagedImpl<RequestScopeFunction> implements Serializable {

	private RequestScopeFunctionFilterController filterController;
	private HashMap<String, String> legends;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new RequestScopeFunctionFilterController().initialize();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		legends = RequestListPage.buildLegends();
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	public static String buildWindowTitleValue(RequestScopeFunctionFilterController filterController) {
		if(filterController == null)
			return null;
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.LABEL);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return buildWindowTitleValue(filterController);
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		RequestScopeFunctionFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (RequestScopeFunctionFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = (RequestScopeFunctionFilterController) MapHelper.readByKey(arguments, RequestScopeFunctionFilterController.class));
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new RequestScopeFunctionFilterController());
		lazyDataModelListenerImpl.enableFilterController();
		if(StringHelper.isBlank(filterController.getOnSelectRedirectorArguments(Boolean.TRUE).getOutcome())) {
			String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
			filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		}
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl().setFilterController(filterController));
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, RequestScopeFunction.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_SORT_MODE, "multiple");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		
		String outcome = (String) ValueHelper.defaultToIfBlank(arguments.get(RequestEditSignatureSpecimenInformationsPage.OUTCOME), RequestEditSignatureSpecimenInformationsPage.OUTCOME);
		dataTable.addRecordMenuItemByArgumentsNavigateToView(null,outcome, MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil"
				,MenuItem.ConfiguratorImpl.FIELD_ARGUMENT_SYSTEM_IDENTIFIER_FIELD_NAME,RequestScopeFunction.FIELD_REQUEST_IDENTIFIER);
		
		dataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Consulter spécimen de signature",MenuItem.FIELD_ICON,"fa fa-eye",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.FALSE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_NULLABLE,Boolean.TRUE
				,MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				RequestScopeFunction requestScopeFunction = (RequestScopeFunction)action.readArgument();
				if(requestScopeFunction == null)
					return null;
				if(StringHelper.isBlank(requestScopeFunction.getSignatureSpecimenReadReportURIQuery()))
					throw new RuntimeException(String.format("Il n'existe aucun spécimen de signature pour le poste %s",requestScopeFunction.getScopeFunctionString()));
				String script = OpenWindowScriptBuilder.getInstance().build(
						UniformResourceIdentifierBuilder.getInstance().buildFromCurrentRequest(ReportServlet.PATH, requestScopeFunction.getSignatureSpecimenReadReportURIQuery()).toString()
						, "Spécimen de signature de "+StringUtils.substringBefore(requestScopeFunction.getScopeFunctionString()," "));
				PrimeFaces.current().executeScript(script);
				return null;
			}
		});
		
		dataTable.addRecordMenuItemByArguments(MenuItem.FIELD_VALUE,"Envoyer spécimen de signature par mail",MenuItem.FIELD_ICON,"fa fa-send",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.INLINE,RenderType.GROWL)
				,MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				RequestScopeFunction requestScopeFunction = (RequestScopeFunction)action.readArgument();
				if(requestScopeFunction == null)
					return null;
				if(StringHelper.isBlank(requestScopeFunction.getSignatureSpecimenReadReportURIQuery()))
					throw new RuntimeException(String.format("Il n'existe aucun spécimen de signature pour le poste %s",requestScopeFunction.getScopeFunctionString()));
				String identifier = requestScopeFunction.getIdentifier();
				requestScopeFunction = new RequestScopeFunction();
				requestScopeFunction.setIdentifier(identifier);
				Arguments<RequestScopeFunction> arguments = new Arguments<RequestScopeFunction>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(requestScopeFunction);
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(RequestScopeFunctionBusiness.NOTIFY_SIGNATURE_SPECIMEN));
				EntitySaver.getInstance().save(RequestScopeFunction.class, arguments);
				return arguments.get__responseEntity__();
			}
		});
		
		//CollectionHelper.getLast(dataTable.getRecordMenu().getItems()).setConfirm(null);
		
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
		private RequestScopeFunctionFilterController filterController;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.FIELD_WIDTH, "250");
			}else if(RequestScopeFunction.FIELD_SCOPE_FUNCTION_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Poste");
			}else if(RequestScopeFunction.FIELD_GRANTED_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Accordé ?");
				map.put(Column.FIELD_WIDTH, "80");
			}else if(RequestScopeFunction.FIELD_SECTION_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "65");
			}else if(RequestScopeFunction.FIELD_ADMINISTRATIVE_UNIT_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Unité Administrative");
			}else if(RequestScopeFunction.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(RequestScopeFunction.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prénom(s)");
				map.put(Column.FIELD_WIDTH, "170");
			}else if(RequestScopeFunction.FIELD_REGISTRATION_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Matricule");
				map.put(Column.FIELD_WIDTH, "90");
			}else if(RequestScopeFunction.FIELD_MOBILE_PHONE_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Tel. Mobile");
				map.put(Column.FIELD_WIDTH, "90");
			}else if(RequestScopeFunction.FIELD_OFFICE_PHONE_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Tel. Bureau");
				map.put(Column.FIELD_WIDTH, "80");
			}else if(RequestScopeFunction.FIELD_ADMINISTRATIVE_UNIT_FUNCTION.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction Administrative");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(RequestScopeFunction.FIELD_ACT_OF_APPOINTMENT_REFERENCE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Réf. Acte Nomination");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(RequestScopeFunction.FIELD_REQUEST_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code Demande");
				map.put(Column.FIELD_WIDTH, "100");
			}
			return map;
		}
		/*
		@Override
		public String getStyleClassByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(record instanceof RequestScopeFunction) {
				RequestScopeFunction request = (RequestScopeFunction) record;
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
		*/
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<RequestScopeFunction> implements Serializable {
		private Collection<String> excludedIdentifiers;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<RequestScopeFunction> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<RequestScopeFunction> lazyDataModel) {
			return RequestScopeFunctionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		public Filter.Dto instantiateFilter(LazyDataModel<RequestScopeFunction> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			filter = RequestScopeFunctionFilterController.populateFilter(filter, (RequestScopeFunctionFilterController) filterController,Boolean.TRUE);
			return filter;
		}
		
		@Override
		public Arguments<RequestScopeFunction> instantiateArguments(LazyDataModel<RequestScopeFunction> lazyDataModel) {
			Arguments<RequestScopeFunction> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().addProjectionsFromStrings(
					ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.FIELD_IDENTIFIER);
			
			arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction
					.FIELDS_REQUEST_IDENTIFIER_IDENTITY_SCOPE_FUNCTION_STRING_GRANTED_STRING);
			/*
			Boolean granted = ((RequestScopeFunctionFilterController)filterController).getGrantedInitial();
			if(granted == null) {
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS
						,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.FIELD_SCOPE_FUNCTION_STRING,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.FIELD_GRANTED_STRING);
			}else
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS
						,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestScopeFunction.FIELD_SCOPE_FUNCTION_STRING);
			*/
			return arguments;
		}
		
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new RequestScopeFunctionFilterController();
			filterController.build();
			return this;
		}
	}
	
	public static final String OUTCOME = "requestScopeFunctionListView";
}