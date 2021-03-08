package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityCounter;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
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

import ci.gouv.dgbf.system.actor.client.controller.api.ActivityCategoryController;
import ci.gouv.dgbf.system.actor.client.controller.api.BudgetSpecializationUnitController;
import ci.gouv.dgbf.system.actor.client.controller.api.ExpenditureNatureController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
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
import ci.gouv.dgbf.system.actor.client.controller.impl.ActivitySelectionController;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.ScopeFunctionFilterController;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExpenditureNatureQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private DataTable dataTable;
	
	private Cell assignmentsTabCell;
	private TabMenu assignmentsTabMenu;
	private Cell dataTableCell;
	
	private TabMenu tabMenu;
	private Integer selectedAssignmentsTabIndex;
	private TabMenu.Tab selectedTab,selectedAssignmentsTab;
	private SelectOneCombo sectionSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,activitySelectOne,activityCategorySelectOne
		,expenditureNatureSelectOne,functionSelectOne;
	private List<Function> functions;
	private Section section,initialSection;
	private AdministrativeUnit administrativeUnit,initialAdministrativeUnit;
	private BudgetSpecializationUnit budgetSpecializationUnit,initialBudgetSpecializationUnit;
	private Action action,initialAction;
	private Activity activity,initialActivity;
	private ActivityCategory activityCategory,initalActivityCategory;
	private ExpenditureNature expenditureNature,initialExpenditureNature;	
	
	private CommandButton applyGlobalFilterCommand;
	private ScopeFunctionFilterController scopeFunctionFilterController;
	private ActivitySelectionController activitySelectionController;
	
	private AssignmentsFilterController assignmentsFilterController;
	
	@Override
	protected void __listenPostConstruct__() {
		if(activity == null)
			initialActivity = activity = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(Activity.class
					, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
		if(activity != null) {
			initialSection = section = activity.getSection();
			initialAdministrativeUnit = administrativeUnit = activity.getAdministrativeUnit();
			initialBudgetSpecializationUnit = budgetSpecializationUnit = activity.getBudgetSpecializationUnit();
			initialExpenditureNature = expenditureNature = activity.getExpenditureNature();
			initalActivityCategory = activityCategory = activity.getCategory();
		}
		
		if(budgetSpecializationUnit == null)
			initialBudgetSpecializationUnit = budgetSpecializationUnit = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(BudgetSpecializationUnit.class
					, BudgetSpecializationUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
		if(budgetSpecializationUnit != null && section == null) {
			initialSection = section = budgetSpecializationUnit.getSection();
		}
		
		if(administrativeUnit == null)
			initialAdministrativeUnit = administrativeUnit = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(AdministrativeUnit.class
					, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
		if(administrativeUnit != null && section == null) {
			initialSection = section = administrativeUnit.getSection();
		}
		
		if(section == null)
			initialSection = section = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Section.class, null);
		
		if(expenditureNature == null && activity != null)
			initialExpenditureNature = expenditureNature = __inject__(ExpenditureNatureController.class).readBySystemIdentifier(activity.getExpenditureNatureIdentifier());
		if(expenditureNature == null)
			initialExpenditureNature = expenditureNature = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(ExpenditureNature.class, null);
		
		
		if(activityCategory == null && activity != null)
			initalActivityCategory = activityCategory = __inject__(ActivityCategoryController.class).readBySystemIdentifier(activity.getCategoryIdentifier());
		if(activityCategory == null)
			initalActivityCategory = activityCategory = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(ActivityCategory.class, null);
		
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		buildTabMenu(cellsMaps);
		buildTab(cellsMaps);
		buildLayout(cellsMaps);
		super.__listenPostConstruct__();
		
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
		scopeFunctionFilterController = new ScopeFunctionFilterController().build();
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
		activitySelectionController = new ActivitySelectionController();
		activitySelectionController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_ASSIGNMENTS);
		
		String tabAssignmentsParameterValue = WebController.getInstance().getRequestParameter(TAB_ASSIGNMENTS_PARAMETER_NAME);		
		for(Integer index = 0; index < ASSIGNMENTS_TABS.size(); index = index + 1) {
			TabMenu.Tab tab = ASSIGNMENTS_TABS.get(index);
			if(selectedAssignmentsTabIndex == null && tab.getParameterValue().equals(tabAssignmentsParameterValue))
				selectedAssignmentsTabIndex = index;
		}
		if(selectedAssignmentsTabIndex == null)
			selectedAssignmentsTabIndex = 0;
		selectedAssignmentsTab = ASSIGNMENTS_TABS.get(selectedAssignmentsTabIndex);
		
		activitySelectionController.getOnSelectRedirectorArguments().addParameter(TAB_ASSIGNMENTS_PARAMETER_NAME, selectedAssignmentsTab.getParameterValue());			
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
		String sectionCode = (String) FieldHelper.readBusinessIdentifier(section);
		String administrativeUnitCode = (String) FieldHelper.readBusinessIdentifier(administrativeUnit);
		String budgetSpecializationUnitCode = (String) FieldHelper.readBusinessIdentifier(budgetSpecializationUnit);
		String activityCategoryCode = (String) FieldHelper.readBusinessIdentifier(activityCategory);
		String expenditureNatureCode = (String) FieldHelper.readBusinessIdentifier(expenditureNature);
		String activityCode = (String) FieldHelper.readBusinessIdentifier(activity);
		
		Collection<MenuItem> items = new ArrayList<>();
		Long total = count(TAB_ASSIGNMENTS_ALL, sectionCode,administrativeUnitCode, budgetSpecializationUnitCode, activityCategoryCode, expenditureNatureCode, activityCode);
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
					count = count(tab.getParameterValue(), sectionCode, administrativeUnitCode, budgetSpecializationUnitCode, activityCategoryCode, expenditureNatureCode, activityCode);
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}				
			}
			item.setValue(name).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_ASSIGNMENTS).addParameter(TAB_ASSIGNMENTS_PARAMETER_NAME, tab.getParameterValue());			
			item.addParameterFromInstanceIfConditionIsTrue(section,administrativeUnit == null && budgetSpecializationUnit == null && action == null && activity == null);
			item.addParameterFromInstanceIfConditionIsTrue(administrativeUnit,activity == null);
			item.addParameterFromInstanceIfConditionIsTrue(budgetSpecializationUnit,action == null && activity == null);
			item.addParameterFromInstanceIfConditionIsTrue(action,activity == null);
			item.addParameterFromInstance(activity);
			item.addParameterFromInstanceIfConditionIsTrue(expenditureNature,activity == null);
			item.addParameterFromInstanceIfConditionIsTrue(activityCategory,activity == null);
		}		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,selectedAssignmentsTabIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,items);
		return tabMenu;
	}
	
	public DataTable buildAssignmentsDataTable() {
		String sectionCode = (String) FieldHelper.readBusinessIdentifier(section);
		String administrativeUnitCode = (String) FieldHelper.readBusinessIdentifier(administrativeUnit);
		String budgetSpecializationUnitCode = (String) FieldHelper.readBusinessIdentifier(budgetSpecializationUnit);
		String activityCategoryCode = (String) FieldHelper.readBusinessIdentifier(activityCategory);
		String expenditureNatureCode = (String) FieldHelper.readBusinessIdentifier(expenditureNature);
		String activityCode = (String) FieldHelper.readBusinessIdentifier(activity);
		
		AssignmentsListPage.LazyDataModelListenerImpl lazyDataModelListener = new AssignmentsListPage.LazyDataModelListenerImpl();
		lazyDataModelListener.setSectionCode(sectionCode);
		lazyDataModelListener.setAdministrativeUnitCode(administrativeUnitCode);
		lazyDataModelListener.setBudgetSpecializationUnitCode(budgetSpecializationUnitCode);
		lazyDataModelListener.setActivityCategoryCode(activityCategoryCode);
		lazyDataModelListener.setExpenditureNatureCode(expenditureNatureCode);
		lazyDataModelListener.setActivityCode(activityCode);		
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
				.buildColumnsNames(section,administrativeUnit, budgetSpecializationUnit, activity, expenditureNature, activityCategory)
				,Section.class,section,AdministrativeUnit.class,administrativeUnit,BudgetSpecializationUnit.class,budgetSpecializationUnit
				,Action.class,action,Activity.class,activity,ExpenditureNature.class,expenditureNature,ActivityCategory.class,activityCategory
				);
		return dataTable;
	}
	
	private Long count(String value, String sectionCode, String administrativeUnitCode, String budgetSpecializationUnitCode, String activityCategoryCode, String expenditureNatureCode, String activityCode) {
		Arguments<Assignments> arguments = new Arguments<>();
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setQueryExecutorArguments(
				new QueryExecutorArguments.Dto().setQueryIdentifier(AssignmentsQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)));
		if(value.equals(TAB_ASSIGNMENTS_FULLY_ASSIGNED)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_ALL_HOLDERS_DEFINED,Boolean.TRUE);
		}else if(value.equals(TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_SOME_HOLDERS_NOT_DEFINED,Boolean.TRUE);
		}else if(value.equals(TAB_ASSIGNMENTS_ALL)) {
			
		}
		arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_SECTION, sectionCode);
		arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT, administrativeUnitCode);
		arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT, budgetSpecializationUnitCode);
		arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_ACTIVITY_CATEGORY, activityCategoryCode);
		arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_EXPENDITURE_NATURE, expenditureNatureCode);
		arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(AssignmentsQuerier.PARAMETER_NAME_ACTIVITY, activityCode);			
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
			return AssignmentsListPage.buildWindowTitleValue(selectedAssignmentsTab.getName(), section,administrativeUnit, budgetSpecializationUnit,action, activity,expenditureNature,activityCategory);
		return "Affectation";
	}
	
	/* Filters */
	
	/*         Assignments */
	
	private void buildTabAssignmentsGlobalFilters(Collection<Map<Object,Object>> cellsMaps) {
		/*
		buildTabAssignmentsGlobalFilterSelectOneActivity();
		buildTabAssignmentsGlobalFilterSelectOneExpenditureNature();
		buildTabAssignmentsGlobalFilterSelectOneActivityCategory();		
		buildTabAssignmentsGlobalFilterSelectOneBudgetSpecializationUnit();
		buildTabAssignmentsGlobalFilterSelectOneAdministrativeUnit();
		buildTabAssignmentsGlobalFilterSelectOneSection();
		buildTabAssignmentsGlobalFilterApplyCommand();
		
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,11));	
		}
		
		if(budgetSpecializationUnitSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("U.S.B.").setTitle("Unité de spécialisation du budget"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetSpecializationUnitSelectOne,Cell.FIELD_WIDTH,3));
		}
		
		if(administrativeUnitSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("U.A.").setTitle("Unité administrative"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnitSelectOne,Cell.FIELD_WIDTH,7));
		}
		
		if(expenditureNatureSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("N.D.").setTitle("Nature de dépense"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,expenditureNatureSelectOne,Cell.FIELD_WIDTH,5));	
		}
		
		if(activityCategorySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("C.A.").setTitle("Catégorie d'activité"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activityCategorySelectOne,Cell.FIELD_WIDTH,5));	
		}
		
		if(activitySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Activité"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne,Cell.FIELD_WIDTH,9));	
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,applyGlobalFilterCommand,Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectionController.getShowDialogCommandButton(),Cell.FIELD_WIDTH,1));
		*/
		assignmentsFilterController = new AssignmentsFilterController().build();
		assignmentsFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());			
		assignmentsFilterController.getActivitySelectionController().getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_ASSIGNMENTS);
		assignmentsFilterController.getActivitySelectionController().getOnSelectRedirectorArguments().addParameter(TAB_ASSIGNMENTS_PARAMETER_NAME, selectedAssignmentsTab.getParameterValue());		
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,assignmentsFilterController.getLayout(),Cell.FIELD_WIDTH,12));
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
				AffectationPage.this.section = section;
				if(administrativeUnitSelectOne != null) {
					administrativeUnitSelectOne.updateChoices();
					if(initialAdministrativeUnit == null)
						administrativeUnitSelectOne.selectFirstChoice();
					else {
						administrativeUnitSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initialAdministrativeUnit));
						initialAdministrativeUnit = null;
					}
				}
				if(budgetSpecializationUnitSelectOne != null) {
					budgetSpecializationUnitSelectOne.updateChoices();
					if(initialBudgetSpecializationUnit == null)
						budgetSpecializationUnitSelectOne.selectFirstChoice();
					else {
						budgetSpecializationUnitSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initialBudgetSpecializationUnit));
						initialBudgetSpecializationUnit = null;
					}
				}
			}
		});
		sectionSelectOne.updateChoices();
		sectionSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(section));
		sectionSelectOne.enableValueChangeListener(List.of(administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneAdministrativeUnit() {		
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
				AffectationPage.this.administrativeUnit = administrativeUnit;
				/*if(expenditureNatureSelectOne != null) {
					expenditureNatureSelectOne.updateChoices();
					if(initialExpenditureNature == null)
						expenditureNatureSelectOne.selectFirstChoice();
					else {
						expenditureNatureSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initialExpenditureNature));
						initialExpenditureNature = null;
					}
				}
				if(activityCategorySelectOne != null) {
					activityCategorySelectOne.updateChoices();
					if(initalActivityCategory == null)
						activityCategorySelectOne.selectFirstChoice();
					else {
						activityCategorySelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initalActivityCategory));
						initalActivityCategory = null;
					}
				}*/
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					if(initialActivity == null)
						activitySelectOne.selectFirstChoice();
					else {
						activitySelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initialActivity));
						//initialActivity = null;
					}
				}
			}
		}
		);
		administrativeUnitSelectOne.enableValueChangeListener(List.of(expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneBudgetSpecializationUnit() {		
		budgetSpecializationUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,BudgetSpecializationUnit.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<BudgetSpecializationUnit>() {
			public Collection<BudgetSpecializationUnit> computeChoices(AbstractInputChoice<BudgetSpecializationUnit> input) {
				Collection<BudgetSpecializationUnit> choices = null;
				if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
					return null;				
				Section section = (Section) sectionSelectOne.getValue();
				choices = __inject__(BudgetSpecializationUnitController.class).readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(section.getIdentifier());
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			public void select(AbstractInputChoiceOne input, BudgetSpecializationUnit budgetSpecializationUnit) {
				super.select(input, budgetSpecializationUnit);
				AffectationPage.this.budgetSpecializationUnit = budgetSpecializationUnit;
				if(expenditureNatureSelectOne != null) {
					expenditureNatureSelectOne.updateChoices();
					if(initialExpenditureNature == null)
						expenditureNatureSelectOne.selectFirstChoice();
					else {
						expenditureNatureSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initialExpenditureNature));
						initialExpenditureNature = null;
					}
				}
				if(activityCategorySelectOne != null) {
					activityCategorySelectOne.updateChoices();
					if(initalActivityCategory == null)
						activityCategorySelectOne.selectFirstChoice();
					else {
						activityCategorySelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initalActivityCategory));
						initalActivityCategory = null;
					}
				}
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					if(initialActivity == null)
						activitySelectOne.selectFirstChoice();
					else {
						activitySelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(initialActivity));
						initialActivity = null;
					}
				}
			}
		}
		);
		budgetSpecializationUnitSelectOne.enableValueChangeListener(List.of(expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneExpenditureNature() {
		expenditureNatureSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,ExpenditureNature.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ExpenditureNature>() {
			@Override
			public Collection<ExpenditureNature> computeChoices(AbstractInputChoice<ExpenditureNature> input) {
				Collection<ExpenditureNature> choices = null;
				if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
					choices = EntityReader.getInstance().readMany(ExpenditureNature.class, ExpenditureNatureQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
				else {
					if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() == null)
						choices = EntityReader.getInstance().readMany(ExpenditureNature.class, ExpenditureNatureQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
								,ExpenditureNatureQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,((Section)sectionSelectOne.getValue()).getIdentifier());
					else
						choices = EntityReader.getInstance().readMany(ExpenditureNature.class, ExpenditureNatureQuerier.QUERY_IDENTIFIER_READ_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER_FOR_UI
								,ExpenditureNatureQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getIdentifier());				
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, ExpenditureNature expenditureNature) {
				super.select(input, expenditureNature);
				AffectationPage.this.expenditureNature = expenditureNature;
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					activitySelectOne.selectFirstChoice();
				}
			}
		});
		if(activity == null) {
			expenditureNatureSelectOne.updateChoices();
			expenditureNatureSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(expenditureNature));
		}		
		expenditureNatureSelectOne.enableValueChangeListener(List.of(activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneActivityCategory() {
		activityCategorySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,ActivityCategory.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ActivityCategory>() {
			@Override
			public Collection<ActivityCategory> computeChoices(AbstractInputChoice<ActivityCategory> input) {
				Collection<ActivityCategory> choices = null;
				if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
					choices = EntityReader.getInstance().readMany(ActivityCategory.class, ActivityCategoryQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
				else {
					if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() == null)
						choices = EntityReader.getInstance().readMany(ActivityCategory.class, ActivityCategoryQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
								,ActivityCategoryQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,((Section)sectionSelectOne.getValue()).getIdentifier());
					else
						choices = EntityReader.getInstance().readMany(ActivityCategory.class, ActivityCategoryQuerier.QUERY_IDENTIFIER_READ_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER_FOR_UI
								,ActivityCategoryQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getIdentifier());				
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, ActivityCategory activityCategory) {
				super.select(input, activityCategory);
				AffectationPage.this.activityCategory = activityCategory;
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					activitySelectOne.selectFirstChoice();
				}
			}
		});
		if(activity == null) {
			activityCategorySelectOne.updateChoices();
			activityCategorySelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(activityCategory));
		}
		activityCategorySelectOne.enableValueChangeListener(List.of(activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneActivity() {		
		activitySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Activity.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Activity>() {
			public Collection<Activity> computeChoices(AbstractInputChoice<Activity> input) {
				if(AbstractInput.getValue(administrativeUnitSelectOne) == null && AbstractInput.getValue(budgetSpecializationUnitSelectOne) == null)
					return null;
				Arguments<Activity> arguments = new Arguments<>();
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments());
				arguments.getRepresentationArguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto()
						.setQueryIdentifier(ActivityQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI));
				if(administrativeUnit != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_IDENTIFIER,FieldHelper.readSystemIdentifier(administrativeUnit));
				if(budgetSpecializationUnit != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,FieldHelper.readSystemIdentifier(budgetSpecializationUnit));
				if(expenditureNature != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_EXPENDITURE_NATURE_IDENTIFIER,FieldHelper.readSystemIdentifier(expenditureNature));
				if(activityCategory != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_CATEGORY_IDENTIFIER,FieldHelper.readSystemIdentifier(activityCategory));								
				Collection<Activity> choices = EntityReader.getInstance().readMany(Activity.class, arguments);				
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		}
		);
		activitySelectOne.enableValueChangeListener(List.of());		
	}
	
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
				if(selectedAssignmentsTab != null)
					map.put(TAB_ASSIGNMENTS_PARAMETER_NAME,List.of(selectedAssignmentsTab.getParameterValue()));
				
				if(activitySelectOne != null && activitySelectOne.getValue() != null)
					map.put(ParameterName.stringify(Activity.class),List.of( ((Activity)activitySelectOne.getValue()).getIdentifier()));
				if((activitySelectOne == null || activitySelectOne.getValue() == null) && budgetSpecializationUnitSelectOne != null && budgetSpecializationUnitSelectOne.getValue() != null)
					map.put(ParameterName.stringify(BudgetSpecializationUnit.class),List.of( ((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getIdentifier()));
				if((budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() == null)
						&& (administrativeUnitSelectOne == null || administrativeUnitSelectOne.getValue() == null) 
						&& sectionSelectOne != null && sectionSelectOne.getValue() != null)
					map.put(ParameterName.stringify(Section.class),List.of( ((Section)sectionSelectOne.getValue()).getIdentifier()));
				
				if(activitySelectOne == null || activitySelectOne.getValue() == null) {
					if(administrativeUnitSelectOne != null && administrativeUnitSelectOne.getValue() != null)
						map.put(ParameterName.stringify(AdministrativeUnit.class),List.of( ((AdministrativeUnit)administrativeUnitSelectOne.getValue()).getIdentifier()));
					if(activityCategorySelectOne != null && activityCategorySelectOne.getValue() != null)
						map.put(ParameterName.stringify(ActivityCategory.class),List.of( ((ActivityCategory)activityCategorySelectOne.getValue()).getIdentifier()));
					if(expenditureNatureSelectOne != null && expenditureNatureSelectOne.getValue() != null)
						map.put(ParameterName.stringify(ExpenditureNature.class),List.of( ((ExpenditureNature)expenditureNatureSelectOne.getValue()).getIdentifier()));				
				}				
				Redirector.getInstance().redirect(OUTCOME, map);
				return null;
			}
		});
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
	public static Map<String,Integer> FUNCTIONS_ORDERS_INDEXES = Map.of(
		ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER,0
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT,1
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER,2
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT,3
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER,4
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT,5
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER,6
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT,7
	);
}