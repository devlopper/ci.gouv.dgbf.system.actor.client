package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;

import ci.gouv.dgbf.system.actor.client.controller.entities.ExecutionImputation;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ExecutionImputationBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ExecutionImputationsEditScopeFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private DataTable executionImputationsDataTable;
	private List<ExecutionImputation> executionImputations;
	private AutoComplete creditManagerHolderAutoComplete;
	private AutoComplete authorizingOfficerHolderAutoComplete;
	private AutoComplete financialControllerHolderAutoComplete;
	private AutoComplete accountingHolderAutoComplete;
	private Map<String,Object[]> initialValues = new HashMap<>();
	private CommandButton saveCommandButton;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation des postes";
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		creditManagerHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
				,FIELD_CREDIT_MANAGER_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER);
		authorizingOfficerHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER
				,FIELD_AUTHORIZING_OFFICER_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER);
		financialControllerHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER
				,FIELD_FINANCIAL_CONTROLLER_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER);
		accountingHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER
				,FIELD_ACCOUNTING_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_ACCOUNTING_HOLDER);
		buildDataTable();
		buildSaveCommandButton();
		buildLayout();
	}
	
	private void buildDataTable() {
		executionImputationsDataTable = ExecutionImputationListPage.buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl()
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
				,DataTable.FIELD_RENDER_TYPE,org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType.INPUT
				);
		executionImputationsDataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Liste",MenuItem.FIELD_ICON,"fa fa-file",MenuItem.FIELD_USER_INTERFACE_ACTION
				,UserInterfaceAction.NAVIGATE_TO_VIEW,MenuItem.FIELD_LISTENER,new MenuItem.Listener.AbstractImpl() {
			@Override
			protected String getOutcome(AbstractAction action) {
				return "executionImputationListView";
			}
		});
	}
	
	private void buildSaveCommandButton() {
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						LogHelper.logInfo("Nothing to update", getClass());
						if(CollectionHelper.isEmpty(executionImputations)) {
							return null;
						}						
						Collection<ExecutionImputation> updatables = null;
						for(ExecutionImputation executionImputation : executionImputations) {
							if(!Boolean.TRUE.equals(isHasBeenEdited(executionImputation)))							
								continue;
							if(updatables == null)
								updatables = new ArrayList<>();
							updatables.add(executionImputation);
						}
						if(CollectionHelper.isEmpty(updatables)) {
							LogHelper.logInfo("Nothing to update", getClass());
							return null;
						}						
						LogHelper.logInfo("Update "+updatables.size()+" executionImputations", getClass());
						EntitySaver.getInstance().save(ExecutionImputation.class, new Arguments<ExecutionImputation>()
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setActionIdentifier(ExecutionImputationBusiness.SAVE_SCOPE_FUNCTIONS)).setUpdatables(updatables));	
						return null;
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right");
		saveCommandButton.addUpdates(":form:"+executionImputationsDataTable.getIdentifier());
	}
	
	private void buildLayout() {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,executionImputationsDataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
				));
	}
	
	private static AutoComplete buildScopeFunctionAutoComplete(String functionCode,String componentFieldName,String functionFieldName) {
		if(StringHelper.isBlank(functionCode))
			return null;
		AutoComplete autoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ScopeFunction.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.FIELD_READ_QUERY_IDENTIFIER,ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_BY_FUNCTION_CODE
				,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<ScopeFunction>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto()
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_CODE, functionCode);
			}			
		});
		autoComplete.setBindingByDerivation("executionImputationsEditScopeFunctionsPage."+componentFieldName, "record."+functionFieldName);
		/*autoComplete.setReadItemLabelListener(new ReadListener() {
			@Override
			public Object read(Object object) {
				if(!(object instanceof ScopeFunction))
					return ConstantEmpty.STRING;
				return ((ScopeFunction)object).getCode();
			}			
		});*/
		return autoComplete;
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public class DataTableListenerImpl extends ExecutionImputationListPage.DataTableListenerImpl implements Serializable {
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_ACCOUNTING_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}
			return map;
		}
		
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			//Disable tooltip while editing
			return null;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public class LazyDataModelListenerImpl extends ExecutionImputationListPage.LazyDataModelListenerImpl implements Serializable {	
		@Override
		public List<ExecutionImputation> read(LazyDataModel<ExecutionImputation> lazyDataModel) {
			executionImputations = super.read(lazyDataModel);
			initialValues.clear();
			if(CollectionHelper.isNotEmpty(executionImputations))
				executionImputations.forEach(executionImputation -> {
					initialValues.put(executionImputation.getIdentifier(), new Object[] {
							executionImputation.getCreditManager(Boolean.TRUE).getHolder()
							,executionImputation.getAuthorizingOfficer(Boolean.TRUE).getHolder()
							,executionImputation.getFinancialController(Boolean.TRUE).getHolder()
							,executionImputation.getAccounting(Boolean.TRUE).getHolder()
						});
				});
			return executionImputations;
		}
	}
	
	private Boolean isHasBeenEdited(ExecutionImputation executionImputation) {
		Object[] initials = initialValues.get(executionImputation.getIdentifier());
		if(initials == null)
			return Boolean.FALSE;
		ScopeFunction creditManagerHolder = (ScopeFunction)initials[0];
		if(creditManagerHolder == null && (executionImputation.getCreditManager() == null || executionImputation.getCreditManager().getHolder() == null))
			return Boolean.FALSE;
		if(creditManagerHolder == null && executionImputation.getCreditManager() != null && executionImputation.getCreditManager().getHolder() != null)
			return Boolean.TRUE;
		if(creditManagerHolder != null && (executionImputation.getCreditManager() == null || executionImputation.getCreditManager().getHolder() == null))
			return Boolean.TRUE;		
		if(!creditManagerHolder.getIdentifier().equals(executionImputation.getCreditManager().getHolder().getIdentifier()))
			return Boolean.TRUE;
		return  Boolean.FALSE;
	}

	/**/
	
	public static final String FIELD_CREDIT_MANAGER_HOLDER_AUTO_COMPLETE = "creditManagerHolderAutoComplete";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER_AUTO_COMPLETE = "authorizingOfficerHolderAutoComplete";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER_AUTO_COMPLETE = "financialControllerHolderAutoComplete";
	public static final String FIELD_ACCOUNTING_HOLDER_AUTO_COMPLETE = "accountingHolderAutoComplete";
}