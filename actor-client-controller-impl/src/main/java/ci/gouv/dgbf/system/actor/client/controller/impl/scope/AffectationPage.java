package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityCounter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.Action;
import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.ScopeFunctionFilterController;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private DataTable dataTable;	
	
	private TabMenu tabMenu;
	private Integer selectedAssignmentsTabIndex;
	private TabMenu.Tab selectedTab,selectedAssignmentsTab;
	
	private ScopeFunctionFilterController scopeFunctionFilterController;
	private AssignmentsFilterController assignmentsFilterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		if(selectedTab != null && TAB_SCOPE_FUNCTION.equals(selectedTab.getParameterValue()))
			scopeFunctionFilterController = new ScopeFunctionFilterController();
		if(selectedTab != null && TAB_ASSIGNMENTS.equals(selectedTab.getParameterValue()))
			assignmentsFilterController = new AssignmentsFilterController();
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		buildTabMenu(cellsMaps);
		buildTab(cellsMaps);
		buildLayout(cellsMaps);
	}
		
	private void buildTabMenu(Collection<Map<Object,Object>> cellsMaps) {		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(TabMenu.Tab tab : TABS) {
			MenuItem menuItem = new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			tabMenuItems.add(menuItem);
		}
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		/*
		if(selectedTab != null && selectedTab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildFunctionsTabMenu(cellsMaps);
		*/
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps) {
		if(selectedTab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildTabScopeFunction(cellsMaps);
		else if(selectedTab.getParameterValue().equals(TAB_ASSIGNMENTS))
			buildTabAssignments(cellsMaps);
	}
	
	private void buildTabScopeFunction(Collection<Map<Object,Object>> cellsMaps) {
		if(scopeFunctionFilterController == null)
			scopeFunctionFilterController = new ScopeFunctionFilterController();
		scopeFunctionFilterController.build();
		scopeFunctionFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_SCOPE_FUNCTION);	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeFunctionFilterController.getLayout(),Cell.FIELD_WIDTH,12));
		
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildScopeFunctionDataTable();
			}
		},Cell.FIELD_WIDTH,12));
	}
	
	public DataTable buildScopeFunctionDataTable() {
		DataTable dataTable = ScopeFunctionListPage.buildDataTable(ScopeFunctionListPage.class,Boolean.TRUE,Function.class,scopeFunctionFilterController.getFunction()
				,FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER),FieldHelper.readSystemIdentifier(scopeFunctionFilterController.getFunction()));
		return dataTable;
	}
	
	private void buildTabAssignments(Collection<Map<Object,Object>> cellsMaps) {
		String tabAssignmentsParameterValue = WebController.getInstance().getRequestParameter(TAB_ASSIGNMENTS_PARAMETER_NAME);		
		for(Integer index = 0; index < ASSIGNMENTS_TABS.size(); index = index + 1) {
			TabMenu.Tab tab = ASSIGNMENTS_TABS.get(index);
			if(selectedAssignmentsTabIndex == null && tab.getParameterValue().equals(tabAssignmentsParameterValue))
				selectedAssignmentsTabIndex = index;
		}
		if(selectedAssignmentsTabIndex == null)
			selectedAssignmentsTabIndex = 0;
		selectedAssignmentsTab = ASSIGNMENTS_TABS.get(selectedAssignmentsTabIndex);
		
		buildTabAssignmentsGlobalFilters(cellsMaps);
		
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildAssignmentsTabMenu();
			}
		},Cell.FIELD_WIDTH,12));
		
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildAssignmentsDataTable();
			}
		},Cell.FIELD_WIDTH,12));		
	}
	
	public TabMenu buildAssignmentsTabMenu() {
		Collection<MenuItem> items = new ArrayList<>();
		Long total = count(TAB_ASSIGNMENTS_ALL);
		for(Integer index = 0; index < ASSIGNMENTS_TABS.size(); index = index + 1) {
			TabMenu.Tab tab = ASSIGNMENTS_TABS.get(index);
			MenuItem item = new MenuItem();
			items.add(item);
			Long count = null;
			String name;
			if(tab.getParameterValue().equals(TAB_ASSIGNMENTS_ALL)) {
				count = total;
				name = String.format("%s (%s)", tab.getName(),count);
			}else {
				if(NumberHelper.isEqualToZero(total)) {
					name = String.format("%s (%s)", tab.getName(),0);
				}else {
					count = count(tab.getParameterValue());
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}				
			}
			item.setValue(name).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_ASSIGNMENTS).addParameter(TAB_ASSIGNMENTS_PARAMETER_NAME, tab.getParameterValue());			
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getSection(),assignmentsFilterController.getAdministrativeUnit() == null 
					&& assignmentsFilterController.getBudgetSpecializationUnit() == null && assignmentsFilterController.getAction() == null && assignmentsFilterController.getActivity() == null);
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getAdministrativeUnit(),assignmentsFilterController.getActivity() == null);
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getBudgetSpecializationUnit(),assignmentsFilterController.getAction() == null && assignmentsFilterController.getActivity() == null);
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getAction(),assignmentsFilterController.getActivity() == null);
			item.addParameterFromInstance(assignmentsFilterController.getActivity());
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getExpenditureNature(),assignmentsFilterController.getActivity() == null);
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getActivityCategory(),assignmentsFilterController.getActivity() == null);
			item.addParameterFromInstanceIfConditionIsTrue(assignmentsFilterController.getFunction(),assignmentsFilterController.getScopeFunction() == null);
			item.addParameterFromInstance(assignmentsFilterController.getScopeFunction());
		}		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,selectedAssignmentsTabIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,items);
		return tabMenu;
	}
	
	public DataTable buildAssignmentsDataTable() {
		AssignmentsListPage.LazyDataModelListenerImpl lazyDataModelListener = new AssignmentsListPage.LazyDataModelListenerImpl();
		lazyDataModelListener.applyFilterController(assignmentsFilterController);
		if(selectedAssignmentsTab.getParameterValue().equals(TAB_ASSIGNMENTS_FULLY_ASSIGNED)) {
			lazyDataModelListener.setAllHoldersDefined(Boolean.TRUE);
		}else if(selectedAssignmentsTab.getParameterValue().equals(TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED)) {
			lazyDataModelListener.setSomeHoldersNotDefined(Boolean.TRUE);
		}		
		AssignmentsListPage.DataTableListenerImpl dataTableListener = new AssignmentsListPage.DataTableListenerImpl();		
		DataTable dataTable = AssignmentsListPage.buildDataTable(AssignmentsListPage.class,Boolean.TRUE
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				,DataTable.ConfiguratorImpl.FIELD_LISTENER,dataTableListener
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,AssignmentsListPage.DataTableListenerImpl
				.buildColumnsNames(assignmentsFilterController.getSection(),assignmentsFilterController.getAdministrativeUnit()
						, assignmentsFilterController.getBudgetSpecializationUnit(), assignmentsFilterController.getActivity()
						, assignmentsFilterController.getExpenditureNature(), assignmentsFilterController.getActivityCategory(),assignmentsFilterController.getScopeFunction())
				,Section.class,assignmentsFilterController.getSection(),AdministrativeUnit.class,assignmentsFilterController.getAdministrativeUnit()
				,BudgetSpecializationUnit.class,assignmentsFilterController.getBudgetSpecializationUnit()
				,Action.class,assignmentsFilterController.getAction(),Activity.class,assignmentsFilterController.getActivity()
				,ExpenditureNature.class,assignmentsFilterController.getExpenditureNature(),ActivityCategory.class,assignmentsFilterController.getActivityCategory()
				,ScopeFunction.class,assignmentsFilterController.getScopeFunction()
				);
		return dataTable;
	}
	
	private Long count(String value) {
		Arguments<Assignments> arguments = new Arguments<>();
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setQueryExecutorArguments(
				new QueryExecutorArguments.Dto().setQueryIdentifier(AssignmentsQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER_USING_IDENTIFIERS_ONLY)));
		if(value.equals(TAB_ASSIGNMENTS_FULLY_ASSIGNED)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_ALL_HOLDERS_DEFINED,Boolean.TRUE);
		}else if(value.equals(TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_SOME_HOLDERS_NOT_DEFINED,Boolean.TRUE);
		}else if(value.equals(TAB_ASSIGNMENTS_ALL)) {
			
		}
		AssignmentsListPage.LazyDataModelListenerImpl.addFieldIfValueNotNull(arguments.getRepresentationArguments().getQueryExecutorArguments().getFilter(Boolean.TRUE)
				, assignmentsFilterController.getSection(), assignmentsFilterController.getAdministrativeUnit(), assignmentsFilterController.getBudgetSpecializationUnit()
				, assignmentsFilterController.getAction(), assignmentsFilterController.getExpenditureNature(), assignmentsFilterController.getActivityCategory()
				, assignmentsFilterController.getActivity(), null, assignmentsFilterController.getScopeFunction());
		return EntityCounter.getInstance().count(Assignments.class,arguments);
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(selectedTab == null)
			return super.__getWindowTitleValue__();
		if(TAB_SCOPE_FUNCTION.equals(selectedTab.getParameterValue()))
			return ScopeFunctionListPage.buildWindowTitleValue(selectedTab.getName(), scopeFunctionFilterController.getFunction());
		else if(TAB_ASSIGNMENTS.equals(selectedTab.getParameterValue()))
			return AssignmentsListPage.buildWindowTitleValue(selectedAssignmentsTab.getName(), assignmentsFilterController.getSection()
					,assignmentsFilterController.getAdministrativeUnit(), assignmentsFilterController.getBudgetSpecializationUnit()
					,assignmentsFilterController.getAction(), assignmentsFilterController.getActivity(),assignmentsFilterController.getExpenditureNature()
					,assignmentsFilterController.getActivityCategory(),assignmentsFilterController.getScopeFunction());
		return "Affectation";
	}
	
	/* Filters */
	
	/*         Assignments */
	
	private void buildTabAssignmentsGlobalFilters(Collection<Map<Object,Object>> cellsMaps) {		
		if(assignmentsFilterController == null)
			assignmentsFilterController = new AssignmentsFilterController();
		assignmentsFilterController.build();
		assignmentsFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());			
		assignmentsFilterController.getActivitySelectionController().getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_ASSIGNMENTS);
		assignmentsFilterController.getActivitySelectionController().getOnSelectRedirectorArguments().addParameter(TAB_ASSIGNMENTS_PARAMETER_NAME, selectedAssignmentsTab.getParameterValue());				
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,assignmentsFilterController.getLayout(),Cell.FIELD_WIDTH,12));
	}
	
	/**/
	
	private static final String TAB_SCOPE_FUNCTION = "poste";
	private static final String TAB_ASSIGNMENTS = "lignebudgetaire";
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Poste",TAB_SCOPE_FUNCTION)
		,new TabMenu.Tab("Ligne budgétaire",TAB_ASSIGNMENTS)
	);
	
	private static final String TAB_ASSIGNMENTS_PARAMETER_NAME = "a";
	private static final String TAB_ASSIGNMENTS_ALL = "tout";
	//private static final String TAB_ASSIGNMENTS_ALL_2 = "tout2";
	private static final String TAB_ASSIGNMENTS_FULLY_ASSIGNED = "complet";
	private static final String TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED = "noncomplet";
	private static final List<TabMenu.Tab> ASSIGNMENTS_TABS = List.of(		
		new TabMenu.Tab("Lignes complètement affectées",TAB_ASSIGNMENTS_FULLY_ASSIGNED)
		,new TabMenu.Tab("Lignes non complètement affectées",TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED)
		,new TabMenu.Tab("Toutes les lignes",TAB_ASSIGNMENTS_ALL)
		//,new TabMenu.Tab("Toutes les lignes V2",TAB_ASSIGNMENTS_ALL_2)
	);
	
	public static final String OUTCOME = "affectationView";
}