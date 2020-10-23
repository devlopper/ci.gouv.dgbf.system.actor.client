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
		DataTable dataTable = buildDataTable();
		dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-pencil",MenuItem.FIELD_USER_INTERFACE_ACTION
				,UserInterfaceAction.NAVIGATE_TO_VIEW,MenuItem.FIELD_LISTENER,new MenuItem.Listener.AbstractImpl() {
					@Override
					protected String getOutcome(AbstractAction action) {
						return "executionImputationEditManyScopeFunctionsView";
					}
				});
		
		dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Dériver toutes les assignations",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
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
		
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("executionImputationEditScopeFunctionsView", CommandButton.FIELD_VALUE,"Postes"
				,CommandButton.FIELD_ICON,"fa fa-pencil");
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
						,ExecutionImputation.FIELD_ADMINISTRATIVE_UNIT_CODE_NAME
						,ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER,ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER
						,ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER,ExecutionImputation.FIELD_ACCOUNTING_HOLDER));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		private Function creditManagerHolder,authorizingOfficerHolder,financialControllerHolder,accountingHolder;
		private String tooltipFormat;
		
		public DataTableListenerImpl() {
			creditManagerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER);
			authorizingOfficerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER);
			financialControllerHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER);
			accountingHolder = __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER);
			tooltipFormat = StringUtils.join(new String[] {
					"Section : %s"
					,"USB : %s"
					,"Action : %s"
					,"Activité : %s"
					,"Nature économique : %s"
					,"Unité administrative : %s"
					,FieldHelper.readName(creditManagerHolder)+" : %s"
					,FieldHelper.readName(authorizingOfficerHolder)+" : %s"
					,FieldHelper.readName(financialControllerHolder)+" : %s"
					,FieldHelper.readName(accountingHolder)+" : %s"
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
				map.put(Column.FIELD_HEADER_TEXT, "Nat. Éco.");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ECONOMIC_NATURE_CODE_NAME);
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(creditManagerHolder));
			}else if(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(authorizingOfficerHolder));
			}else if(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(financialControllerHolder));
			}else if(ExecutionImputation.FIELD_ACCOUNTING_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, FieldHelper.readBusinessIdentifier(accountingHolder));
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
				map.put(Column.FIELD_HEADER_TEXT, "Unité adm.");
				map.put(Column.FIELD_WIDTH, "100");
				//map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE,Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ExecutionImputationQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_CODE_NAME);
			}
			return map;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_SECTION_CODE_NAME))
				return StringHelper.getFirstWord(((ExecutionImputation)record).getSectionCodeName());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME))
				return StringHelper.getFirstWord(((ExecutionImputation)record).getBudgetSpecializationUnitCodeName());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_ACTION_CODE_NAME))
				return StringHelper.getFirstWord(((ExecutionImputation)record).getActionCodeName());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_ACTIVITY_CODE_NAME))
				return StringHelper.getFirstWord(((ExecutionImputation)record).getActivityCodeName());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_ECONOMIC_NATURE_CODE_NAME))
				return StringHelper.getFirstWord(((ExecutionImputation)record).getEconomicNatureCodeName());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_ADMINISTRATIVE_UNIT_CODE_NAME))
				return StringHelper.getFirstWord(((ExecutionImputation)record).getAdministrativeUnitCodeName());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER))
				return ((ExecutionImputation)record).getCreditManager() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getCreditManager().getHolder());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_CREDIT_MANAGER_ASSISTANT))
				return ((ExecutionImputation)record).getCreditManager() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getCreditManager().getAssistant());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER))
				return ((ExecutionImputation)record).getAuthorizingOfficer() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getAuthorizingOfficer().getHolder());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_ASSISTANT))
				return ((ExecutionImputation)record).getAuthorizingOfficer() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getAuthorizingOfficer().getAssistant());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER))
				return ((ExecutionImputation)record).getFinancialController() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getFinancialController().getHolder());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_ASSISTANT))
				return ((ExecutionImputation)record).getFinancialController() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getFinancialController().getAssistant());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_ACCOUNTING_HOLDER))
				return ((ExecutionImputation)record).getAccounting() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getAccounting().getHolder());
			if(column != null && column.getFieldName().equals(ExecutionImputation.FIELD_ACCOUNTING_ASSISTANT))
				return ((ExecutionImputation)record).getAccounting() == null ? null 
						: FieldHelper.readBusinessIdentifier(((ExecutionImputation)record).getAccounting().getAssistant());
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			ExecutionImputation executionImputation = (ExecutionImputation) record;
			return String.format(tooltipFormat,executionImputation.getSectionCodeName(),executionImputation.getBudgetSpecializationUnitCodeName()
					,executionImputation.getActionCodeName(),executionImputation.getActivityCodeName(),executionImputation.getEconomicNatureCodeName()
					,executionImputation.getAdministrativeUnitCodeName()
					,executionImputation.getCreditManager() == null ? ConstantEmpty.STRING : 
						ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getCreditManager().getHolder()),ConstantEmpty.STRING)
					,executionImputation.getAuthorizingOfficer() == null ? ConstantEmpty.STRING : 
						ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getAuthorizingOfficer().getHolder()),ConstantEmpty.STRING)
					,executionImputation.getFinancialController() == null ? ConstantEmpty.STRING : 
						ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getFinancialController().getHolder()),ConstantEmpty.STRING)
					,executionImputation.getAccounting() == null ? ConstantEmpty.STRING : 
						ValueHelper.defaultToIfBlank(StringHelper.get(executionImputation.getAccounting().getHolder()),ConstantEmpty.STRING)
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
			return ExecutionImputationQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_WITH_ALL;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ExecutionImputation> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			
			return filter;
		}
	}
}