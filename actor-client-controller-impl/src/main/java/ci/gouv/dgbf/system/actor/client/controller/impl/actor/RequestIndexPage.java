package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityCounter;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestListPage.ContentType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	private TabMenu.Tab selectedTab,selectedRequestTab;
	private Layout layout;
	private SelectOneCombo functionSelectOne,sectionSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne;
	private Section section;
	private AdministrativeUnit administrativeUnit;
	private BudgetSpecializationUnit budgetSpecializationUnit;
	private Function function;
	private CommandButton applyGlobalFilterCommand;
		
	@Override
	protected void __listenPostConstruct__() {
		if(administrativeUnit == null)
			administrativeUnit = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(AdministrativeUnit.class
					, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
		if(administrativeUnit != null && section == null) {
			section = administrativeUnit.getSection();
		}
		if(section == null)
			section = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Section.class, null);
		
		if(function == null)
			function = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Function.class, null);
		super.__listenPostConstruct__();
		//functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		buildGlobalFilters(cellsMaps);	
		buildTabMenu(cellsMaps,selectedTab);
		buildTab(cellsMaps,selectedTab);
		buildLayout(cellsMaps);
	}
	
	private void buildTabMenu(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		String functionIdentifier = (String) FieldHelper.readSystemIdentifier(function);
		String sectionIdentifier = (String) FieldHelper.readSystemIdentifier(section);
		String administrativeUnitIdentifier = (String) FieldHelper.readSystemIdentifier(administrativeUnit);
		String budgetSpecializationUnitIdentifier = (String) FieldHelper.readSystemIdentifier(budgetSpecializationUnit);
		Long total = count(TAB_REQUESTS_ALL,functionIdentifier, sectionIdentifier,administrativeUnitIdentifier, budgetSpecializationUnitIdentifier);
		for(TabMenu.Tab tab : TABS) {
			MenuItem menuItem = new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			tabMenuItems.add(menuItem);
			Long count = null;
			String name;
			if(tab.getParameterValue().equals(TAB_REQUESTS_ALL)) {
				count = total;
				name = String.format("%s (%s)", tab.getName(),count);
			}else {				
				if(NumberHelper.isEqualToZero(total)) {
					name = String.format("%s (%s)", tab.getName(),0);
				}else {
					count = count(tab.getParameterValue(), functionIdentifier, sectionIdentifier, administrativeUnitIdentifier, budgetSpecializationUnitIdentifier);
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}				
			}
			menuItem.setValue(name);
			menuItem.addParameterFromInstance(section);
			menuItem.addParameterFromInstance(function);
		}
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		
		//functionIdentifier = Helper.addCreditManagersAuthorizingOfficersFinancialControllersAssistantsTabMenu(cellsMaps, OUTCOME, TABS, selectedTab.getParameterValue(),functionIdentifier);
	}
	
	private Long count(String value,String functionIdentifier,String sectionIdentifier, String administrativeUnitIdentifier, String budgetSpecializationUnitIdentifier) {
		Arguments<Request> arguments = new Arguments<>();
		arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
				new QueryExecutorArguments.Dto().setQueryIdentifier(RequestQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)));
		if(value.equals(TAB_REQUESTS_PROCESSED)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NOT_NULL,Boolean.TRUE);
		}else if(value.equals(TAB_REQUESTS_TO_PROCESS)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NULL,Boolean.TRUE);
		}else if(value.equals(TAB_REQUESTS_ALL)) {
			
		}
		if(StringHelper.isNotBlank(sectionIdentifier))
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_SECTION_IDENTIFIER, sectionIdentifier);
		if(StringHelper.isNotBlank(functionIdentifier))
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER, functionIdentifier);
		//arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT, administrativeUnitIdentifier);
		//arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT, budgetSpecializationUnitCode);			
		return EntityCounter.getInstance().count(Request.class,arguments);
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		RequestListPage.LazyDataModelListenerImpl lazyDataModelListener = new RequestListPage.LazyDataModelListenerImpl();
		lazyDataModelListener.setSectionIdentifier(StringHelper.get(FieldHelper.readSystemIdentifier(AbstractInput.getValue(sectionSelectOne))));
		lazyDataModelListener.setAdministrativeUnitIdentifier(StringHelper.get(FieldHelper.readSystemIdentifier(AbstractInput.getValue(administrativeUnitSelectOne))));
		lazyDataModelListener.setFunctionIdentifier(StringHelper.get(FieldHelper.readSystemIdentifier(AbstractInput.getValue(functionSelectOne))));
		if(tab.getParameterValue().equals(TAB_REQUESTS_TO_PROCESS))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsToProcess(lazyDataModelListener);
				}
			},Cell.FIELD_WIDTH,12));
		else if(tab.getParameterValue().equals(TAB_REQUESTS_PROCESSED))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsProcessed(lazyDataModelListener);
				}
			},Cell.FIELD_WIDTH,12));
		else if(tab.getParameterValue().equals(TAB_REQUESTS_ALL))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsAll(lazyDataModelListener);
				}
			},Cell.FIELD_WIDTH,12));
	}
	
	private DataTable buildDataTableRequestsToProcess(RequestListPage.LazyDataModelListenerImpl lazyDataModelListener) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.TO_PROCESS
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,RequestListPage.DataTableListenerImpl
					.buildColumnsNames(function,section, administrativeUnit, budgetSpecializationUnit, ContentType.TO_PROCESS)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				.setProcessingDateIsNullNullable(Boolean.FALSE)
				);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsProcessed(RequestListPage.LazyDataModelListenerImpl lazyDataModelListener) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.PROCESSED
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,RequestListPage.DataTableListenerImpl
				.buildColumnsNames(function,section, administrativeUnit, budgetSpecializationUnit, ContentType.PROCESSED)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				.setProcessingDateIsNotNullNullable(Boolean.FALSE)
				);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsAll(RequestListPage.LazyDataModelListenerImpl lazyDataModelListener) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.ALL
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,RequestListPage.DataTableListenerImpl
				.buildColumnsNames(function,section, administrativeUnit, budgetSpecializationUnit, ContentType.ALL)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				);
		return dataTable;
	}
	
	private void buildGlobalFilters(Collection<Map<Object,Object>> cellsMaps) {
		buildTabAssignmentsGlobalFilterSelectOneFunction();
		buildTabAssignmentsGlobalFilterSelectOneSection();
		buildTabAssignmentsGlobalFilterApplyCommand();
		
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section"),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,10));	
		}
		
		if(functionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Fonction budgétaire"),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,9));
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,applyGlobalFilterCommand,Cell.FIELD_WIDTH,1));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneFunction() {		
		functionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Function>() {
			@Override
			public Collection<Function> computeChoices(AbstractInputChoice<Function> input) {
				Collection<Function> choices = __inject__(FunctionController.class).readCreditManagersAuthorizingOfficersFinancialControllersAssistants();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Function function) {
				super.select(input, function);
				
			}
		});
		functionSelectOne.updateChoices();
		functionSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(function));
		functionSelectOne.enableValueChangeListener(List.of());
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneSection() {		
		sectionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Section>() {
			@Override
			public Collection<Section> computeChoices(AbstractInputChoice<Section> input) {
				Collection<Section> choices = __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Section section) {
				super.select(input, section);
				if(administrativeUnitSelectOne != null) {
					administrativeUnitSelectOne.updateChoices();
					if(administrativeUnit == null)
						administrativeUnitSelectOne.selectFirstChoice();
					else
						administrativeUnitSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(administrativeUnit));
				}
			}
		});
		sectionSelectOne.updateChoices();
		sectionSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(section));
		sectionSelectOne.enableValueChangeListener(List.of());
	}
	
	/*private void buildTabAssignmentsGlobalFilterSelectOneAdministrativeUnit() {		
		administrativeUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,AdministrativeUnit.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<AdministrativeUnit>() {
			public Collection<AdministrativeUnit> computeChoices(AbstractInputChoice<AdministrativeUnit> input) {
				Collection<AdministrativeUnit> choices = null;
				if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
					return null;
				Section section = (Section) sectionSelectOne.getValue();
				choices = EntityReader.getInstance().readMany(AdministrativeUnit.class, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
						, AdministrativeUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,section.getIdentifier());
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			public void select(AbstractInputChoiceOne input, AdministrativeUnit administrativeUnit) {
				super.select(input, administrativeUnit);
				
			}
		}
		);
		administrativeUnitSelectOne.enableValueChangeListener(List.of());
	}*/
	
	private void buildTabAssignmentsGlobalFilterApplyCommand() {
		applyGlobalFilterCommand = CommandButton.build(CommandButton.FIELD_VALUE,"Filtrer",CommandButton.FIELD_ICON,"fa fa-filter"
				,CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_LISTENER
				,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				Map<String,List<String>> map = new LinkedHashMap<>();
				if(selectedTab != null)
					map.put(TabMenu.Tab.PARAMETER_NAME,List.of(selectedTab.getParameterValue()));
				
				if(sectionSelectOne != null && sectionSelectOne.getValue() != null)
					map.put(ParameterName.stringify(Section.class),List.of( ((Section)sectionSelectOne.getValue()).getIdentifier()));
				
				if(functionSelectOne != null && functionSelectOne.getValue() != null)
					map.put(ParameterName.stringify(Function.class),List.of( ((Function)functionSelectOne.getValue()).getIdentifier()));
								
				Redirector.getInstance().redirect(OUTCOME, map);
				return null;
			}
		});
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestListPage.buildWindowTitleValue("Demandes",function, section, administrativeUnit, budgetSpecializationUnit);
	}
	
	/**/
	
	public static final String TAB_REQUESTS_TO_PROCESS = "demandes_a_traiter";
	public static final String TAB_REQUESTS_PROCESSED = "demandes_traitees";
	public static final String TAB_REQUESTS_ALL = "demandes_toutes";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Demandes à traiter",TAB_REQUESTS_TO_PROCESS)
		,new TabMenu.Tab("Demandes traitées",TAB_REQUESTS_PROCESSED)
		,new TabMenu.Tab("Toutes les demandes",TAB_REQUESTS_ALL)
	);
	
	public static final String OUTCOME = "requestIndexView";
}