package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
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
import ci.gouv.dgbf.system.actor.client.controller.api.FunctionComparator;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private TabMenu tabMenu;
	private TabMenu.Tab selectedTab,selectedAssignmentsTab;
	private SelectOneCombo sectionSelectOne,budgetSpecializationUnitSelectOne,activitySelectOne,activityCategorySelectOne,expenditureNatureSelectOne,functionSelectOne;
	private List<Function> functions;
	private Section section;
	private List<Section> sections;
	private BudgetSpecializationUnit budgetSpecializationUnit;
	private List<BudgetSpecializationUnit> budgetSpecializationUnits;
	private Activity activity;
	private ActivityCategory activityCategory;
	private List<ActivityCategory> activityCategories;
	private ExpenditureNature expenditureNature;
	private List<ExpenditureNature> expenditureNatures;
	private AdministrativeUnit administrativeUnit;
	private Function function;
	
	@Override
	protected void __listenPostConstruct__() {
		contentOutputPanel.setDeferred(Boolean.TRUE);
		
		activityCategories = (List<ActivityCategory>) __inject__(ActivityCategoryController.class).readAllForUI();
		expenditureNatures = (List<ExpenditureNature>) __inject__(ExpenditureNatureController.class).readAllForUI();
		
		sections = (List<Section>) __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
		budgetSpecializationUnits = (List<BudgetSpecializationUnit>) __inject__(BudgetSpecializationUnitController.class).readVisiblesByLoggedInActorCodeForUI();
		
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Activity.class));		
		if(activity == null)
			activity = StringHelper.isBlank(identifier) ? null : EntityReader.getInstance().readOne(Activity.class, ActivityQuerier
					.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI, ActivityQuerier.PARAMETER_NAME_IDENTIFIER,identifier);		
		if(activity != null) {
			for(BudgetSpecializationUnit index : budgetSpecializationUnits) {
				if(index.getIdentifier().equals(activity.getBudgetSpecializationUnitIdentifier())) {
					budgetSpecializationUnit = index;
					break;
				}
			}
		}
		
		if(budgetSpecializationUnit == null)
			budgetSpecializationUnit = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(BudgetSpecializationUnit.class, budgetSpecializationUnits);		
		if(budgetSpecializationUnit != null) {
			for(Section index : sections) {
				if(index.getIdentifier().equals(budgetSpecializationUnit.getSectionIdentifier())) {
					section = index;
					break;
				}
			}
		}
		
		if(section == null)
			section = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Section.class, sections);
		
		if(expenditureNature == null && activity != null)
			expenditureNature = CollectionHelper.getFirst(expenditureNatures.stream().filter(x -> x.getIdentifier().equals(activity.getExpenditureNatureIdentifier()))
					.collect(Collectors.toList()));
		if(expenditureNature == null)
			expenditureNature = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(ExpenditureNature.class, expenditureNatures);
		
		if(activityCategory == null && activity != null)
			activityCategory = CollectionHelper.getFirst(activityCategories.stream().filter(x -> x.getIdentifier().equals(activity.getCategoryIdentifier()))
					.collect(Collectors.toList()));
		if(activityCategory == null)
			activityCategory = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(ActivityCategory.class, activityCategories);
		
		CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(sections);
		CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(expenditureNatures);
		CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(activityCategories);
		super.__listenPostConstruct__();
		
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
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
		
		if(selectedTab != null && selectedTab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildFunctionsTabMenu(cellsMaps);
	}
	
	private void buildFunctionsTabMenu(Collection<Map<Object,Object>> cellsMaps) {		
		functions = (List<Function>) EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATED_TO_SCOPE_TYPE_FOR_UI);
		if(CollectionHelper.isEmpty(functions))
			return;
		Collections.sort((List<Function>)functions, new FunctionComparator());		
		function = WebController.getInstance().getRequestParameterEntityBySystemIdentifier(Function.class, functions, function);
		if(function == null)
			return;
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer tabActiveIndex = null,index = 0;
		for(Function indexFunction : functions) {
			tabMenuItems.add(new MenuItem().setValue(indexFunction.getName())
				.addParameter(TabMenu.Tab.PARAMETER_NAME, TabMenu.Tab.getByParameterValue(TABS, TAB_SCOPE_FUNCTION).getParameterValue())
				.addParameter(ParameterName.stringify(Function.class), indexFunction.getIdentifier())
			);
			if(function.equals(indexFunction))
				tabActiveIndex = index;
			else
				index++;
		}
		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,tabActiveIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps) {
		if(selectedTab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildTabScopeFunction(cellsMaps);
		else if(selectedTab.getParameterValue().equals(TAB_ASSIGNMENTS))
			buildTabAssignments(cellsMaps);
	}
	
	private void buildTabScopeFunction(Collection<Map<Object,Object>> cellsMaps) {
		/*String functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		Collection<Function> functions = EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATED_TO_SCOPE_TYPE_FOR_UI);
		if(StringHelper.isBlank(functionIdentifier) && CollectionHelper.isNotEmpty(functions))
			functionIdentifier = CollectionHelper.getFirst(functions).getIdentifier();
		*/
		DataTable dataTable = ScopeFunctionListPage.buildDataTable(ScopeFunctionListPage.class,Boolean.TRUE
				,FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER),FieldHelper.readSystemIdentifier(function));
		//dataTable.setTitle(OutputText.buildFromValue("Liste des postes"));
		/*SelectOneCombo functionSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_CHOICES,functions);
		functionSelectOneCombo.selectBySystemIdentifier(functionIdentifier);
		functionSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			protected void select(AbstractAction action, Object value) {
				((ScopeFunctionListPage.LazyDataModelListenerImpl)((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener())
					.setFunctionIdentifier((String)FieldHelper.readSystemIdentifier(value));
			}
		}, List.of(dataTable));		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Fonction"),Cell.FIELD_WIDTH,1));	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOneCombo,Cell.FIELD_WIDTH,11));*/
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabAssignments(Collection<Map<Object,Object>> cellsMaps) {
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer tabActiveIndex = null;
		String tabAssignmentsParameterValue = WebController.getInstance().getRequestParameter(TAB_ASSIGNMENTS_PARAMETER_NAME);
		for(Integer index = 0; index < ASSIGNMENTS_TABS.size(); index = index + 1) {
			MenuItem menuItem = new MenuItem().setValue(ASSIGNMENTS_TABS.get(index).getName())
					.addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_ASSIGNMENTS)
					.addParameter(TAB_ASSIGNMENTS_PARAMETER_NAME, ASSIGNMENTS_TABS.get(index).getParameterValue());
			if(activity == null) {
				if(budgetSpecializationUnit == null) {
					if(section == null) {
						
					}else {
						menuItem.addParameter(ParameterName.stringify(Section.class), section.getIdentifier());
					}
				}else {
					menuItem.addParameter(ParameterName.stringify(BudgetSpecializationUnit.class), budgetSpecializationUnit.getIdentifier());
				}			
			}else {
				menuItem.addParameter(ParameterName.stringify(Activity.class), activity.getIdentifier());
			}		
			
			tabMenuItems.add(menuItem);
			if(ASSIGNMENTS_TABS.get(index).getParameterValue().equals(tabAssignmentsParameterValue))
				tabActiveIndex = index;
		}
		if(tabActiveIndex == null)
			tabActiveIndex = 0;
		selectedAssignmentsTab = ASSIGNMENTS_TABS.get(tabActiveIndex);
		
		
		
		/*
		if(section == null)
			budgetSpecializationUnits = (List<BudgetSpecializationUnit>) __inject__(BudgetSpecializationUnitController.class).readVisiblesByLoggedInActorCodeForUI();
		else
			budgetSpecializationUnits = (List<BudgetSpecializationUnit>) __inject__(BudgetSpecializationUnitController.class)
			.readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(section.getIdentifier());
		CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(budgetSpecializationUnits);
		
		budgetSpecializationUnit = WebController.getInstance().getRequestParameterEntityBySystemIdentifier(BudgetSpecializationUnit.class, budgetSpecializationUnits
				,budgetSpecializationUnit);
		if(budgetSpecializationUnit != null) {
			if(section== null)
				section = CollectionHelper.getFirst(sections.stream().filter(x -> x!=null && x.getIdentifier().equals(budgetSpecializationUnit.getSectionIdentifier()))
						.collect(Collectors.toList()));
		}
		*/
		buildTabAssignmentsGlobalFilters(cellsMaps);
		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,tabActiveIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		
		AssignmentsListPage.LazyDataModelListenerImpl lazyDataModelListener = new AssignmentsListPage.LazyDataModelListenerImpl();
		lazyDataModelListener.setSectionCode(StringHelper.get(FieldHelper.readBusinessIdentifier(section)));
		lazyDataModelListener.setBudgetSpecializationUnitCode(StringHelper.get(FieldHelper.readBusinessIdentifier(budgetSpecializationUnit)));
		lazyDataModelListener.setActivityCategoryCode(StringHelper.get(FieldHelper.readBusinessIdentifier(activityCategory)));
		lazyDataModelListener.setExpenditureNatureCode(StringHelper.get(FieldHelper.readBusinessIdentifier(expenditureNature)));
		lazyDataModelListener.setActivityCode(StringHelper.get(FieldHelper.readBusinessIdentifier(activity)));
		
		AssignmentsListPage.DataTableListenerImpl dataTableListenerImpl = new AssignmentsListPage.DataTableListenerImpl();
		if(ASSIGNMENTS_TABS.get(tabActiveIndex).getParameterValue().equals(TAB_ASSIGNMENTS_FULLY_ASSIGNED)) {
			lazyDataModelListener.setAllHoldersDefined(Boolean.TRUE);
		}else if(ASSIGNMENTS_TABS.get(tabActiveIndex).getParameterValue().equals(TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED)) {
			lazyDataModelListener.setSomeHoldersNotDefined(Boolean.TRUE);
		}
		
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
			columnsFieldsNames.add(Assignments.FIELD_SECTION_AS_STRING);
		if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() == null)
			columnsFieldsNames.add(Assignments.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING);
		if(activitySelectOne == null || activitySelectOne.getValue() == null)
			columnsFieldsNames.add(Assignments.FIELD_ACTIVITY_AS_STRING);
		columnsFieldsNames.add(Assignments.FIELD_ECONOMIC_NATURE_AS_STRING);
		
		columnsFieldsNames.addAll(List.of(Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING));
		
		if(activityCategorySelectOne == null || activityCategorySelectOne.getValue() == null)
			columnsFieldsNames.add(Assignments.FIELD_ACTIVITY_CATEGORY_AS_STRING);
		
		if(expenditureNatureSelectOne == null || expenditureNatureSelectOne.getValue() == null)
			columnsFieldsNames.add(Assignments.FIELD_EXPENDITURE_NATURE_AS_STRING);
		
		columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
				,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
		
		DataTable dataTable = AssignmentsListPage.buildDataTable(AssignmentsListPage.class,Boolean.TRUE
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				,DataTable.ConfiguratorImpl.FIELD_LISTENER,dataTableListenerImpl
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames
				);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabAssignmentsGlobalFilters(Collection<Map<Object,Object>> cellsMaps) {
		buildTabAssignmentsGlobalFilterSelectOneActivity(cellsMaps);
		buildTabAssignmentsGlobalFilterSelectOneActivityCategory(cellsMaps);
		buildTabAssignmentsGlobalFilterSelectOneExpenditureNature(cellsMaps);
		buildTabAssignmentsGlobalFilterSelectOneBudgetSpecializationUnit(cellsMaps);
		buildTabAssignmentsGlobalFilterSelectOneSection(cellsMaps);
				
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,11));	
		}
		
		if(budgetSpecializationUnitSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("U.S.B.").setTitle("Unité de spécialisation du budget"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetSpecializationUnitSelectOne,Cell.FIELD_WIDTH,3));	
		}
		
		if(expenditureNatureSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("N.D.").setTitle("Nature de dépense"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,expenditureNatureSelectOne,Cell.FIELD_WIDTH,2));	
		}
		
		if(activityCategorySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("C.A.").setTitle("Catégorie d'activité"),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activityCategorySelectOne,Cell.FIELD_WIDTH,3));	
		}
				
		if(activitySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Activité"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne,Cell.FIELD_WIDTH,10));	
		}
		
		CommandButton commandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Filtrer",CommandButton.FIELD_ICON,"fa fa-filter"
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
				else if(budgetSpecializationUnitSelectOne != null && budgetSpecializationUnitSelectOne.getValue() != null)
					map.put(ParameterName.stringify(BudgetSpecializationUnit.class),List.of( ((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getIdentifier()));
				else if(sectionSelectOne != null && sectionSelectOne.getValue() != null)
					map.put(ParameterName.stringify(Section.class),List.of( ((Section)sectionSelectOne.getValue()).getIdentifier()));
				
				if(activitySelectOne == null || activitySelectOne.getValue() == null) {
					if(activityCategorySelectOne != null && activityCategorySelectOne.getValue() != null)
						map.put(ParameterName.stringify(ActivityCategory.class),List.of( ((ActivityCategory)activityCategorySelectOne.getValue()).getIdentifier()));
					if(expenditureNatureSelectOne != null && expenditureNatureSelectOne.getValue() != null)
						map.put(ParameterName.stringify(ExpenditureNature.class),List.of( ((ExpenditureNature)expenditureNatureSelectOne.getValue()).getIdentifier()));
				}				
				
				//if(functionSelectOne != null && functionSelectOne.getValue() != null)
				//	map.put(ParameterName.stringify(Function.class),List.of(((Function)functionSelectOne.getValue()).getIdentifier()));				
				Redirector.getInstance().redirect(OUTCOME, map);
				return null;
			}
		});
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,commandButton,Cell.FIELD_WIDTH,1));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneSection(Collection<Map<Object,Object>> cellsMaps) {		
		sectionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Section>() {
			@Override
			public Collection<Section> computeChoices(AbstractInputChoice<Section> input) {
				return sections;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, Section section) {
				super.select(input, section);
				AffectationPage.this.section = section;
				if(budgetSpecializationUnitSelectOne != null) {
					budgetSpecializationUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
					budgetSpecializationUnitSelectOne.updateChoices();
					budgetSpecializationUnitSelectOne.selectFirstChoice();
				}
			}
		});
		sectionSelectOne.setValue(section);
		sectionSelectOne.enableValueChangeListener(List.of(budgetSpecializationUnitSelectOne,activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneBudgetSpecializationUnit(Collection<Map<Object,Object>> cellsMaps) {		
		budgetSpecializationUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<BudgetSpecializationUnit>() {
			public Collection<BudgetSpecializationUnit> computeChoices(AbstractInputChoice<BudgetSpecializationUnit> input) {
				Collection<BudgetSpecializationUnit> choices = null;
				if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
					choices = CollectionHelper.isEmpty(budgetSpecializationUnits) ? null : new ArrayList<BudgetSpecializationUnit>(budgetSpecializationUnits);
				else {
					Section section = (Section) sectionSelectOne.getValue();
					choices = budgetSpecializationUnits.stream().filter(x -> x == null || section.getIdentifier().equals(x.getSectionIdentifier())).collect(Collectors.toList());
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			public void select(AbstractInputChoiceOne input, BudgetSpecializationUnit budgetSpecializationUnit) {
				super.select(input, budgetSpecializationUnit);
				AffectationPage.this.budgetSpecializationUnit = budgetSpecializationUnit;
				if(activitySelectOne != null) {
					activitySelectOne.setChoicesInitialized(Boolean.FALSE);
					activitySelectOne.updateChoices();
					activitySelectOne.selectFirstChoice();
				}
			}
		}
		);
		budgetSpecializationUnitSelectOne.setValue(budgetSpecializationUnit);
		budgetSpecializationUnitSelectOne.enableValueChangeListener(List.of(activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneExpenditureNature(Collection<Map<Object,Object>> cellsMaps) {
		expenditureNatureSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,ExpenditureNature.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ExpenditureNature>() {
			@Override
			public Collection<ExpenditureNature> computeChoices(AbstractInputChoice<ExpenditureNature> input) {
				return expenditureNatures;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, ExpenditureNature expenditureNature) {
				super.select(input, expenditureNature);
				AffectationPage.this.expenditureNature = expenditureNature;
				if(activitySelectOne != null) {
					activitySelectOne.setChoicesInitialized(Boolean.FALSE);
					activitySelectOne.updateChoices();
					activitySelectOne.selectFirstChoice();
				}
			}
		});
		expenditureNatureSelectOne.setValue(expenditureNature);
		expenditureNatureSelectOne.enableValueChangeListener(List.of(activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneActivityCategory(Collection<Map<Object,Object>> cellsMaps) {
		activityCategorySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,ActivityCategory.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ActivityCategory>() {
			@Override
			public Collection<ActivityCategory> computeChoices(AbstractInputChoice<ActivityCategory> input) {
				return activityCategories;
			}
			
			@Override
			public void select(AbstractInputChoiceOne input, ActivityCategory activityCategory) {
				super.select(input, activityCategory);
				AffectationPage.this.activityCategory = activityCategory;
				if(activitySelectOne != null) {
					activitySelectOne.setChoicesInitialized(Boolean.FALSE);
					activitySelectOne.updateChoices();
					activitySelectOne.selectFirstChoice();
				}
			}
		});
		activityCategorySelectOne.setValue(activityCategory);
		activityCategorySelectOne.enableValueChangeListener(List.of(activitySelectOne));
	}
	
	private void buildTabAssignmentsGlobalFilterSelectOneActivity(Collection<Map<Object,Object>> cellsMaps) {		
		activitySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Activity.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Activity>() {
			public Collection<Activity> computeChoices(AbstractInputChoice<Activity> input) {
				Collection<Activity> choices = null;
				if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() == null)
					choices = null;
				else {
					BudgetSpecializationUnit budgetSpecializationUnit = (BudgetSpecializationUnit) budgetSpecializationUnitSelectOne.getValue();
					ExpenditureNature expenditureNature = (ExpenditureNature) expenditureNatureSelectOne.getValue();
					ActivityCategory activityCategory = (ActivityCategory) activityCategorySelectOne.getValue();
					if(budgetSpecializationUnit != null && expenditureNature != null && activityCategory != null)
						choices = EntityReader.getInstance().readMany(Activity.class, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER_BY_EXPENDITURE_NATURE_IDENTIFIER_BY_CATEGORY_IDENTIFIER_FOR_UI
								,ActivityQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER, budgetSpecializationUnit.getIdentifier()
								,ActivityQuerier.PARAMETER_NAME_EXPENDITURE_NATURE_IDENTIFIER, expenditureNature.getIdentifier()
								,ActivityQuerier.PARAMETER_NAME_CATEGORY_IDENTIFIER, activityCategory.getIdentifier());
					else if(budgetSpecializationUnit != null && expenditureNature != null)
						choices = EntityReader.getInstance().readMany(Activity.class, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER_BY_EXPENDITURE_NATURE_IDENTIFIER_FOR_UI
								,ActivityQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER, budgetSpecializationUnit.getIdentifier()
								,ActivityQuerier.PARAMETER_NAME_EXPENDITURE_NATURE_IDENTIFIER, expenditureNature.getIdentifier());
					else if(budgetSpecializationUnit != null && activityCategory != null)
						choices = EntityReader.getInstance().readMany(Activity.class, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER_BY_CATEGORY_IDENTIFIER_FOR_UI
								,ActivityQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER, budgetSpecializationUnit.getIdentifier()
								,ActivityQuerier.PARAMETER_NAME_CATEGORY_IDENTIFIER, activityCategory.getIdentifier());
					else if(budgetSpecializationUnit != null)
						choices = EntityReader.getInstance().readMany(Activity.class, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER_FOR_UI
								,ActivityQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER, budgetSpecializationUnit.getIdentifier());
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		}
		);
		activitySelectOne.setValue(activity);
		activitySelectOne.enableValueChangeListener(List.of());		
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		Collection<String> strings = new ArrayList<>();
		strings.add("Affectation");
		if(section != null)
			strings.add(section.toString());
		if(budgetSpecializationUnit != null)
			strings.add(budgetSpecializationUnit.toString());
		if(activity != null)
			strings.add(activity.toString());
		return StringHelper.concatenate(strings, " | ");
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
		new TabMenu.Tab("Affectées",TAB_ASSIGNMENTS_FULLY_ASSIGNED)
		,new TabMenu.Tab("Non affectées",TAB_ASSIGNMENTS_NOT_FULLY_ASSIGNED)
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