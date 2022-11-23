package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
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
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.AssignmentsBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class AssignmentsCollectionEditScopeFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private DataTable assignmentsDataTable;
	private String assignmentsDataTableCellIdentifier = RandomHelper.getAlphabetic(5);
	private List<Assignments> assignmentsCollection;
	private AutoComplete creditManagerHolderAutoComplete;
	private AutoComplete creditManagerAssistantAutoComplete;
	private AutoComplete authorizingOfficerHolderAutoComplete;
	private AutoComplete authorizingOfficerAssistantAutoComplete;
	private AutoComplete financialControllerHolderAutoComplete;
	private AutoComplete financialControllerAssistantAutoComplete;
	private AutoComplete accountingHolderAutoComplete;
	private AutoComplete accountingAssistantAutoComplete;
	private Map<String,Object[]> initialValues = new HashMap<>();
	private CommandButton saveCommandButton;
	
	//@Deprecated
	//private AssignmentsListPage.PageArguments pageArguments = new AssignmentsListPage.PageArguments();
	private AssignmentsFilterController filterController;
	
	@Override
	protected String __getWindowTitleValue__() {
		return filterController.generateWindowTitleValue("Modification des affectations"); //AssignmentsListPage.buildWindowTitleValue("Modification des affectations", pageArguments);
	}
	
	@Override
	protected void __listenPostConstruct__() {
		//pageArguments.initialize();
		filterController = new AssignmentsFilterController();
		filterController.initialize();
		super.__listenPostConstruct__();
		creditManagerHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
				,FIELD_CREDIT_MANAGER_HOLDER_AUTO_COMPLETE,Assignments.FIELD_CREDIT_MANAGER_HOLDER);
		//creditManagerAssistantAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT
		//		,FIELD_CREDIT_MANAGER_ASSISTANT_AUTO_COMPLETE,Assignments.FIELD_CREDIT_MANAGER_ASSISTANT);
		
		authorizingOfficerHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER
				,FIELD_AUTHORIZING_OFFICER_HOLDER_AUTO_COMPLETE,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER);
		//authorizingOfficerAssistantAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT
		//		,FIELD_AUTHORIZING_OFFICER_ASSISTANT_AUTO_COMPLETE,Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT);
		
		financialControllerHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER
				,FIELD_FINANCIAL_CONTROLLER_HOLDER_AUTO_COMPLETE,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER);
		//financialControllerAssistantAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT
		//		,FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AUTO_COMPLETE,Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT);
		
		accountingHolderAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER
				,FIELD_ACCOUNTING_HOLDER_AUTO_COMPLETE,Assignments.FIELD_ACCOUNTING_HOLDER);
		//accountingAssistantAutoComplete = buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT
		//		,FIELD_ACCOUNTING_ASSISTANT_AUTO_COMPLETE,Assignments.FIELD_ACCOUNTING_ASSISTANT);
		
		buildLayout();
	}
	
	private DataTable buildDataTable() {
		assignmentsDataTable = AssignmentsListPage.buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl()
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,filterController.generateColumnsNames()// AssignmentsListPage.DataTableListenerImpl.buildColumnsNames(pageArguments)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().scopeFunction(filterController.getScopeFunction()).setFilterController(filterController) //applyPageArguments(pageArguments)
				,DataTable.FIELD_RENDER_TYPE,org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType.INPUT
				);
		return assignmentsDataTable;
	}
	
	private CommandButton buildSaveCommandButton() {
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						if(CollectionHelper.isEmpty(assignmentsCollection))
							return "Aucune modification";
						Collection<Assignments> updatables = null;
						for(Assignments executionImputation : assignmentsCollection) {
							if(!Boolean.TRUE.equals(isHasBeenEdited(executionImputation)))							
								continue;
							setIdentifiers(executionImputation);
							if(updatables == null)
								updatables = new ArrayList<>();
							updatables.add(executionImputation);
						}
						if(CollectionHelper.isEmpty(updatables)) {
							LogHelper.logInfo("Nothing to update", getClass());
							return "Aucune modification";
						}
						LogHelper.logInfo("Update "+updatables.size()+" assignments", getClass());
						Arguments<Assignments> arguments = new Arguments<Assignments>().setResponseEntityClass(String.class)
						.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setActionIdentifier(AssignmentsBusiness.SAVE_SCOPE_FUNCTIONS_THEN_EXPORT)).setUpdatables(updatables);
						EntitySaver.getInstance().save(Assignments.class, arguments);	
						return arguments.get__responseEntity__();
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right");
		//saveCommandButton.addUpdates(":form:"+assignmentsDataTableCellIdentifier);
		return saveCommandButton;
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildDataTable();
			}
		},Cell.FIELD_WIDTH,12));
		//cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildDataTable(),Cell.FIELD_WIDTH,12));		
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildSaveCommandButton(),Cell.FIELD_WIDTH,12));
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	public static AutoComplete buildScopeFunctionAutoComplete(String functionCode,String componentFieldName,String functionFieldName) {
		if(StringHelper.isBlank(functionCode))
			return null;
		Function function = __inject__(FunctionController.class).readByBusinessIdentifier(functionCode);
		AutoComplete autoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ScopeFunction.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.FIELD_DROPDOWN,Boolean.FALSE
				,AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,function.getName()
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
		autoComplete.setBindingByDerivation("assignmentsCollectionEditScopeFunctionsPage."+componentFieldName, "record."+functionFieldName);
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
	public class DataTableListenerImpl extends AssignmentsListPage.DataTableListenerImpl implements Serializable {
		
		public DataTableListenerImpl() {
			filterable = Boolean.TRUE;
		}
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_CREDIT_MANAGER_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(Assignments.FIELD_ACCOUNTING_ASSISTANT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}
			return map;
		}
		
		public String getTooltipByRecord(Object record, Integer recordIndex) {
			//Disable tooltip while editing
			return null;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			Object value = super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);		
			if(value instanceof String)
				return StringHelper.getFirstWord((String)value);			
			return value;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public class LazyDataModelListenerImpl extends AssignmentsListPage.LazyDataModelListenerImpl implements Serializable {	
		@Override
		public List<Assignments> read(LazyDataModel<Assignments> lazyDataModel) {
			assignmentsCollection = super.read(lazyDataModel);
			initialValues.clear();
			if(CollectionHelper.isNotEmpty(assignmentsCollection))
				assignmentsCollection.forEach(assignments -> {
					initialValues.put(assignments.getIdentifier(), new Object[] {
							assignments.getCreditManagerHolder()
							//,assignments.getCreditManagerAssistant()
							
							,assignments.getAuthorizingOfficerHolder()
							//,assignments.getAuthorizingOfficerAssistant()
							
							,assignments.getFinancialControllerHolder()
							//,assignments.getFinancialControllerAssistant()
							
							,assignments.getAccountingHolder()
							//,assignments.getAccountingAssistant()
						});
				});
			return assignmentsCollection;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Assignments> lazyDataModel) {
			return AssignmentsQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		protected String getFieldsAllStringsCodesOnly() {
			return ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELDS_ALL_STRINGS_CODES_ONLY_WITHOUT_SCOPE_FUNCTIONS;
		}
		
		@Override
		public Arguments<Assignments> instantiateArguments(LazyDataModel<Assignments> lazyDataModel) {
			Arguments<Assignments> arguments = super.instantiateArguments(lazyDataModel);
			ArrayList<String> list = new ArrayList<>();
			list.addAll(List.of(ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELDS_HOLDERS));
			arguments.getRepresentationArguments().getQueryExecutorArguments().addProcessableTransientFieldsNames(list);
			return arguments;
		}
	}
	
	private void setIdentifiers(Assignments assignments) {
		setIdentifiers(assignments,Assignments.FIELD_CREDIT_MANAGER_HOLDER);
		//setIdentifiers(assignments,Assignments.FIELD_CREDIT_MANAGER_ASSISTANT);
		setIdentifiers(assignments,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER);
		//setIdentifiers(assignments,Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT);
		setIdentifiers(assignments,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER);
		//setIdentifiers(assignments,Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT);
		setIdentifiers(assignments,Assignments.FIELD_ACCOUNTING_HOLDER);
		//setIdentifiers(assignments,Assignments.FIELD_ACCOUNTING_ASSISTANT);
	}
	
	private void setIdentifiers(Assignments assignments,String fieldName) {
		ScopeFunction scopeFunction = (ScopeFunction) FieldHelper.read(assignments, fieldName);
		FieldHelper.write(assignments, fieldName+"AsString", scopeFunction == null ? null : scopeFunction.getIdentifier());			
	}
	
	private Boolean isHasBeenEdited(Assignments assignments) {
		Object[] initials = initialValues.get(assignments.getIdentifier());
		if(initials == null)
			return Boolean.FALSE;
		if(isHasBeenEdited(assignments,Assignments.FIELD_CREDIT_MANAGER_HOLDER,0))
			return Boolean.TRUE;
		//if(isHasBeenEdited(assignments,Assignments.FIELD_CREDIT_MANAGER_ASSISTANT,1))
		//	return Boolean.TRUE;
		if(isHasBeenEdited(assignments,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER,1))
			return Boolean.TRUE;
		//if(isHasBeenEdited(assignments,Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT,3))
		//	return Boolean.TRUE;
		if(isHasBeenEdited(assignments,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER,2))
			return Boolean.TRUE;
		//if(isHasBeenEdited(assignments,Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT,5))
		//	return Boolean.TRUE;
		if(isHasBeenEdited(assignments,Assignments.FIELD_ACCOUNTING_HOLDER,3))
			return Boolean.TRUE;
		//if(isHasBeenEdited(assignments,Assignments.FIELD_ACCOUNTING_ASSISTANT,7))
		//	return Boolean.TRUE;
		return  Boolean.FALSE;
	}

	
	
	private Boolean isHasBeenEdited(Assignments assignments,String fieldName,Integer initialValueIndex) {
		Object[] initials = initialValues.get(assignments.getIdentifier());
		ScopeFunction initialValue = (ScopeFunction) initials[initialValueIndex];
		ScopeFunction current = (ScopeFunction) FieldHelper.read(assignments, fieldName);
		if(initialValue == null)
			return current != null;
		else
			return current == null || !initialValue.getIdentifier().equals(current.getIdentifier());
	}
	
	/**/
	
	public static final String FIELD_CREDIT_MANAGER_HOLDER_AUTO_COMPLETE = "creditManagerHolderAutoComplete";
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT_AUTO_COMPLETE = "creditManagerAssistantAutoComplete";
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER_AUTO_COMPLETE = "authorizingOfficerHolderAutoComplete";
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT_AUTO_COMPLETE = "authorizingOfficerAssistantAutoComplete";
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER_AUTO_COMPLETE = "financialControllerHolderAutoComplete";
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AUTO_COMPLETE = "financialControllerAssistantAutoComplete";
	public static final String FIELD_ACCOUNTING_HOLDER_AUTO_COMPLETE = "accountingHolderAutoComplete";
	public static final String FIELD_ACCOUNTING_ASSISTANT_AUTO_COMPLETE = "accountingAssistantAutoComplete";
}