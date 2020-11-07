package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExecutionImputation;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionExecutionImputationBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExecutionImputationQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ExecutionImputationListPage extends AbstractEntityListPageContainerManagedImpl<ExecutionImputation> implements Serializable {

	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,Helper.buildFunctionListPageTabMenu(null),Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)					
						));
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ExecutionImputationListPage.class,Boolean.TRUE);		
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des imputations de l'exécution";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ExecutionImputation.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES
				, CollectionHelper.listOf(ExecutionImputation.FIELD_SECTION_CODE_NAME,ExecutionImputation.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME
						,ExecutionImputation.FIELD_ACTION_CODE_NAME,ExecutionImputation.FIELD_ACTIVITY_CODE_NAME,ExecutionImputation.FIELD_ECONOMIC_NATURE_CODE_NAME
						,ExecutionImputation.FIELD_ADMINISTRATIVE_UNIT_CODE_NAME,ExecutionImputation.FIELD_ACTIVITY_CATEGORY_CODE_NAME
						,ExecutionImputation.FIELD_EXPENDITURE_NATURE_CODE_NAME
						,ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER_SCOPE_FUNCTION_CODE_NAME,ExecutionImputation.FIELD_CREDIT_MANAGER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME
						,ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER_SCOPE_FUNCTION_CODE_NAME,ExecutionImputation.FIELD_AUTHORIZING_OFFICER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME
						,ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER_SCOPE_FUNCTION_CODE_NAME,ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME
						,ExecutionImputation.FIELD_ACCOUNTING_HOLDER_SCOPE_FUNCTION_CODE_NAME,ExecutionImputation.FIELD_ACCOUNTING_ASSISTANT_SCOPE_FUNCTION_CODE_NAME
						));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		if(Boolean.TRUE.equals(MapHelper.readByKey(arguments, ExecutionImputationListPage.class))) {
			/*
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil",MenuItem.FIELD_USER_INTERFACE_ACTION
					,UserInterfaceAction.NAVIGATE_TO_VIEW,MenuItem.FIELD_LISTENER,new MenuItem.Listener.AbstractImpl() {
						@Override
						protected String getOutcome(AbstractAction action) {
							return "executionImputationEditManyScopeFunctionsView";
						}
					});
			*/
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("executionImputationEditManyScopeFunctionsView"
					, MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.OPEN_VIEW_IN_DIALOG);
			
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("executionImputationEditManyScopeFunctionsByModelView"
					, MenuItem.FIELD_VALUE,"Appliquer un modèle",MenuItem.FIELD_ICON,"fa fa-cubes",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.OPEN_VIEW_IN_DIALOG);
			
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Initialiser",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.FIELD_ICON,"fa fa-database"
					,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
					,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>();
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(ScopeFunctionExecutionImputationBusiness.DERIVE_ALL));					
							EntitySaver.getInstance().save(ScopeFunction.class, arguments);
							return null;
						}
					});
			
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Éffacer",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.FIELD_ICON,"fa fa-trash"
					,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
					,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>();
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(ScopeFunctionExecutionImputationBusiness.DELETE_ALL));					
							EntitySaver.getInstance().save(ScopeFunction.class, arguments);
							return null;
						}
					});
			
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("executionImputationEditScopeFunctionsView", CommandButton.FIELD_VALUE,"Modifier"
					,CommandButton.FIELD_ICON,"fa fa-pencil");
		}
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		private Function creditManagerHolder,creditManagerAssistant,authorizingOfficerHolder,authorizingOfficerAssistant
			,financialControllerHolder,financialControllerAssistant,accountingHolder,accountingAssistant;
		private String tooltipFormat;
		
		public DataTableListenerImpl() {
			creditManagerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER);
			creditManagerAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT);
			authorizingOfficerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER);
			authorizingOfficerAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT);
			financialControllerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER);
			financialControllerAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT);
			accountingHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER);
			accountingAssistant = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT);
			tooltipFormat = StringUtils.join(new String[] {
					 "Section              : %s"
					,"USB                  : %s"
					,"Action               : %s"
					,"Activité             : %s"
					,"Nature économique    : %s"
					,"Unité administrative : %s"
					,"Catégorie activité   : %s"
					,"Nature dépense       : %s"
					,FieldHelper.readName(creditManagerHolder)+"       : %s"
					,FieldHelper.readName(creditManagerAssistant)+"       : %s"
					,FieldHelper.readName(authorizingOfficerHolder)+"  : %s"
					,FieldHelper.readName(authorizingOfficerAssistant)+"  : %s"
					,FieldHelper.readName(financialControllerHolder)+" : %s"
					,FieldHelper.readName(financialControllerAssistant)+" : %s"
					,FieldHelper.readName(accountingHolder)+"          : %s"
					,FieldHelper.readName(accountingAssistant)+"          : %s"
				},"<br/>");
		}
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);			
			if(ExecutionImputation.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");			
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_ACTIVITY_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Activité");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ACTIVITY_CODE_NAME);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ExecutionImputation.FIELD_ECONOMIC_NATURE_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "NE");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ECONOMIC_NATURE_CODE_NAME);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(creditManagerHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_CREDIT_MANAGER_HOLDER_CODE_NAME);
			}else if(ExecutionImputation.FIELD_CREDIT_MANAGER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(creditManagerAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(authorizingOfficerHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_AUTHORIZING_OFFICER_HOLDER_CODE_NAME);
			}else if(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(authorizingOfficerAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(financialControllerHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_FINANCIAL_CONTROLLER_HOLDER_CODE_NAME);
			}else if(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_ASSISTANT_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(financialControllerAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ExecutionImputation.FIELD_ACCOUNTING_HOLDER_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(accountingHolder));
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ACCOUNTING_HOLDER_CODE_NAME);
			}else if(ExecutionImputation.FIELD_ACCOUNTING_ASSISTANT_SCOPE_FUNCTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(accountingAssistant));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ExecutionImputation.FIELD_SECTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_WIDTH, "80");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_SECTION_CODE_NAME);
			}else if(ExecutionImputation.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "USB");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_CODE_NAME);
			}else if(ExecutionImputation.FIELD_ACTION_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Action");
				map.put(Column.FIELD_WIDTH, "120");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ACTION_CODE_NAME);
			}else if(ExecutionImputation.FIELD_ADMINISTRATIVE_UNIT_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "UA");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_CODE_NAME);
			}else if(ExecutionImputation.FIELD_ACTIVITY_CATEGORY_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "CA");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ACTIVITY_CATEGORY_CODE_NAME);
			}else if(ExecutionImputation.FIELD_EXPENDITURE_NATURE_CODE_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "ND");
				map.put(Column.FIELD_WIDTH, "80");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_EXPENDITURE_NATURE_CODE_NAME);
			}
			return map;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			Object value = super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);		
			if(value instanceof String)
				return StringHelper.getFirstWord((String)value);			
			return value;
		}
		
		@Override
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			ExecutionImputation executionImputation = (ExecutionImputation) record;
			return String.format(tooltipFormat,executionImputation.getSectionCodeName(),executionImputation.getBudgetSpecializationUnitCodeName()
					,executionImputation.getActionCodeName(),executionImputation.getActivityCodeName(),executionImputation.getEconomicNatureCodeName()
					,executionImputation.getAdministrativeUnitCodeName(),executionImputation.getActivityCategoryCodeName(),executionImputation.getExpenditureNatureCodeName()
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getCreditManagerHolderScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getCreditManagerAssistantScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getAuthorizingOfficerHolderScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getAuthorizingOfficerAssistantScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getFinancialControllerHolderScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getFinancialControllerAssistantScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getAccountingHolderScopeFunctionCodeName()),ConstantEmpty.STRING)
					,ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getAccountingAssistantScopeFunctionCodeName()),ConstantEmpty.STRING)
				);
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ExecutionImputation> implements Serializable {		
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<ExecutionImputation> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ExecutionImputation> lazyDataModel) {
			return ExecutionImputationQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ExecutionImputation> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			
			return filter;
		}
	}
}