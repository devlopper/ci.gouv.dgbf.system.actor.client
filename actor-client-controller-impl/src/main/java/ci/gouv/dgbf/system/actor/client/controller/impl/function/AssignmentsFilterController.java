package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.actor.client.controller.api.ActivityCategoryController;
import ci.gouv.dgbf.system.actor.client.controller.api.BudgetCategoryController;
import ci.gouv.dgbf.system.actor.client.controller.api.BudgetSpecializationUnitController;
import ci.gouv.dgbf.system.actor.client.controller.api.ExpenditureNatureController;
import ci.gouv.dgbf.system.actor.client.controller.api.LocalityController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Action;
import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Locality;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.ActivitySelectionController;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExpenditureNatureQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.LocalityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class AssignmentsFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo budgetCategorySelectOne,exerciseSelectOne,sectionSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,actionSelectOne,activitySelectOne,activityCategorySelectOne
		,expenditureNatureSelectOne,functionSelectOne,regionSelectOne,departmentSelectOne,subPrefectureSelectOne;
	private AutoComplete scopeFunctionAutoComplete;
	private ActivitySelectionController activitySelectionController;
	
	private BudgetCategory budgetCategoryInitial;
	private Integer exerciseInitial;
	private Section sectionInitial;
	private AdministrativeUnit administrativeUnitInitial;
	private BudgetSpecializationUnit budgetSpecializationUnitInitial;
	private Action actionInitial;
	private ActivityCategory activityCategoryInitial;
	private ExpenditureNature expenditureNatureInitial;
	private Activity activityInitial;
	private Collection<Activity> activitiesInitial;
	private Function functionInitial;
	private Locality regionInitial;
	private Locality departmentInitial;
	private Locality subPrefectureInitial;
	private ScopeFunction scopeFunctionInitial;
	
	private Collection<BudgetCategory> visibleBudgetCategories;
	
	public AssignmentsFilterController() {
		exerciseInitial = NumberHelper.getInteger(WebController.getInstance().getRequestParameter("exercice"),2022);
		Collection<String> activitiesIdentifiers = WebController.getInstance().getRequestParameters(ParameterName.stringifyMany(Activity.class));
		if(CollectionHelper.isEmpty(activitiesIdentifiers)) {
			activityInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(Activity.class
					, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
			if(activityInitial != null) {
				budgetCategoryInitial = activityInitial.getBudgetCategory();
				sectionInitial = activityInitial.getSection();
				administrativeUnitInitial = activityInitial.getAdministrativeUnit();
				budgetSpecializationUnitInitial = activityInitial.getBudgetSpecializationUnit();
				expenditureNatureInitial = activityInitial.getExpenditureNature();
				activityCategoryInitial = activityInitial.getCategory();
			}
			
			if(budgetSpecializationUnitInitial == null)
				budgetSpecializationUnitInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(BudgetSpecializationUnit.class
						, BudgetSpecializationUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
			if(budgetSpecializationUnitInitial != null && sectionInitial == null) {
				sectionInitial = budgetSpecializationUnitInitial.getSection();
			}
			
			if(administrativeUnitInitial == null)
				administrativeUnitInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(AdministrativeUnit.class
						, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
			if(administrativeUnitInitial != null && sectionInitial == null) {
				sectionInitial = administrativeUnitInitial.getSection();
			}
			
			if(administrativeUnitInitial != null && regionInitial == null)
				regionInitial = administrativeUnitInitial.getRegion();
			
			if(administrativeUnitInitial != null && departmentInitial == null)
				departmentInitial = administrativeUnitInitial.getDepartment();
			
			if(administrativeUnitInitial != null && subPrefectureInitial == null)
				subPrefectureInitial = administrativeUnitInitial.getSubPrefecture();
			
			if(subPrefectureInitial == null) {
				subPrefectureInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(Locality.class
						,LocalityQuerier.QUERY_IDENTIFIER_READ_SUB_PREFECTURE_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);
				if(subPrefectureInitial != null) {
					regionInitial = subPrefectureInitial.getRegion();
					departmentInitial = subPrefectureInitial.getDepartment();
				}
			}
			
			if(departmentInitial == null) {
				departmentInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(Locality.class
						,LocalityQuerier.QUERY_IDENTIFIER_READ_DEPARTMENT_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);
				if(departmentInitial != null) {
					regionInitial = departmentInitial.getRegion();
				}
			}
			
			if(regionInitial == null) {
				regionInitial = WebController.getInstance().getRequestParameterEntityAsParent(Locality.class);
			}
			
			if(budgetCategoryInitial == null)
				budgetCategoryInitial = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(BudgetCategory.class, null);
			if(budgetCategoryInitial == null) {
				visibleBudgetCategories = __inject__(BudgetCategoryController.class).readVisiblesByLoggedInActorCodeForUI();
				budgetCategoryInitial = CollectionHelper.getFirst(visibleBudgetCategories);
			}
			if(sectionInitial == null)
				sectionInitial = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Section.class, null);
			
			if(expenditureNatureInitial == null && activityInitial != null)
				expenditureNatureInitial = __inject__(ExpenditureNatureController.class).readBySystemIdentifier(activityInitial.getExpenditureNatureIdentifier());
			if(expenditureNatureInitial == null)
				expenditureNatureInitial = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(ExpenditureNature.class, null);
			
			
			if(activityCategoryInitial == null && activityInitial != null)
				activityCategoryInitial = __inject__(ActivityCategoryController.class).readBySystemIdentifier(activityInitial.getCategoryIdentifier());
			if(activityCategoryInitial == null)
				activityCategoryInitial = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(ActivityCategory.class, null);
			
			if(scopeFunctionInitial == null)
				scopeFunctionInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(ScopeFunction.class
						, ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI);	
			/*	
			if(functionInitial == null && scopeFunctionInitial != null)
				functionInitial = scopeFunctionInitial.getFunction();
			if(functionInitial == null)
				functionInitial = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Function.class, null);
			*/
		}else {
			activitiesInitial = EntityReader.getInstance().readMany(Activity.class, new Arguments<Activity>().queryIdentifier(ActivityQuerier.QUERY_IDENTIFIER_READ_DYNAMIC)
					.filterByIdentifiers(activitiesIdentifiers));
		}
	}
	
	
	@Override
	public AssignmentsFilterController build() {
		activitySelectionController = new ActivitySelectionController();
		activitySelectionController.setIsMultiple(Boolean.TRUE);
		activitySelectionController.build();
		return (AssignmentsFilterController) super.build();
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_EXERCISE_SELECT_ONE.equals(fieldName))
			return exerciseInitial;
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return sectionInitial;
		if(FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE.equals(fieldName))
			return administrativeUnitInitial;
		if(FIELD_BUDGET_SPECIALIZATION_UNIT_SELECT_ONE.equals(fieldName))
			return budgetSpecializationUnitInitial;
		if(FIELD_EXPENDITURE_NATURE_SELECT_ONE.equals(fieldName))
			return expenditureNatureInitial;
		if(FIELD_ACTIVITY_CATEGORY_SELECT_ONE.equals(fieldName))
			return activityCategoryInitial;
		if(FIELD_ACTIVITY_SELECT_ONE.equals(fieldName))
			return activityInitial;
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return functionInitial;
		if(FIELD_SCOPE_FUNCTION_AUTO_COMPLETE.equals(fieldName))
			return scopeFunctionInitial;
		
		if(FIELD_REGION_SELECT_ONE.equals(fieldName))
			return regionInitial;
		
		if(FIELD_DEPARTMENT_SELECT_ONE.equals(fieldName))
			return departmentInitial;
		
		if(FIELD_SUB_PREFECTURE_SELECT_ONE.equals(fieldName))
			return subPrefectureInitial;
		
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected void __buildInputs__() {
		super.__buildInputs__();
		buildInputSelectOne(FIELD_BUDGET_CATEGORY_SELECT_ONE,BudgetCategory.class);
		buildInputSelectOne(FIELD_EXERCISE_SELECT_ONE,Integer.class);
		buildInputSelectOne(FIELD_SECTION_SELECT_ONE, Section.class);
		buildInputSelectOne(FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE, AdministrativeUnit.class);
		buildInputSelectOne(FIELD_BUDGET_SPECIALIZATION_UNIT_SELECT_ONE, BudgetSpecializationUnit.class);
		buildInputSelectOne(FIELD_EXPENDITURE_NATURE_SELECT_ONE, ExpenditureNature.class);
		buildInputSelectOne(FIELD_ACTIVITY_CATEGORY_SELECT_ONE, ActivityCategory.class);
		buildInputSelectOne(FIELD_ACTIVITY_SELECT_ONE, Activity.class);
		//buildInputSelectOne(FIELD_FUNCTION_SELECT_ONE, ScopeFunction.class);
		buildInputSelectOne(FIELD_SCOPE_FUNCTION_AUTO_COMPLETE, ScopeFunction.class);
		
		buildInputSelectOne(FIELD_REGION_SELECT_ONE, ScopeFunction.class);
		buildInputSelectOne(FIELD_DEPARTMENT_SELECT_ONE, ScopeFunction.class);
		buildInputSelectOne(FIELD_SUB_PREFECTURE_SELECT_ONE, ScopeFunction.class);
		
		//enableValueChangeListeners();		
		//selectByValueSystemIdentifier();		
	}
	
	@Override
	protected void enableValueChangeListeners() {
		sectionSelectOne.enableValueChangeListener(List.of(administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
		administrativeUnitSelectOne.enableValueChangeListener(List.of(expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
		budgetSpecializationUnitSelectOne.enableValueChangeListener(List.of(expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
		expenditureNatureSelectOne.enableValueChangeListener(List.of(activitySelectOne));
		activityCategorySelectOne.enableValueChangeListener(List.of(activitySelectOne));
		activitySelectOne.enableValueChangeListener(List.of());
		//functionSelectOne.enableValueChangeListener(List.of());
		
		regionSelectOne.enableValueChangeListener(List.of(departmentSelectOne,subPrefectureSelectOne));
		departmentSelectOne.enableValueChangeListener(List.of(subPrefectureSelectOne));
		subPrefectureSelectOne.enableValueChangeListener(List.of());
	}
	
	@Override
	protected void selectByValueSystemIdentifier() {
		sectionSelectOne.selectByValueSystemIdentifier();
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_EXERCISE_SELECT_ONE.equals(fieldName))
			return buildExerciseSelectOne((Integer) value);
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return buildSectionSelectOne((Section) value);
		if(FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE.equals(fieldName))
			return buildAdministrativeUnitSelectOne((AdministrativeUnit) value);
		if(FIELD_BUDGET_SPECIALIZATION_UNIT_SELECT_ONE.equals(fieldName))
			return buildBudgetSpecializationUnitSelectOne((BudgetSpecializationUnit) value);
		if(FIELD_EXPENDITURE_NATURE_SELECT_ONE.equals(fieldName))
			return buildExpenditureNatureSelectOne((ExpenditureNature) value);
		if(FIELD_ACTIVITY_CATEGORY_SELECT_ONE.equals(fieldName))
			return buildActivityCategorySelectOne((ActivityCategory) value);
		if(FIELD_ACTIVITY_SELECT_ONE.equals(fieldName))
			return buildActivitySelectOne((Activity) value);
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return FunctionListPage.buildSelectOne((Function) value);
		if(FIELD_SCOPE_FUNCTION_AUTO_COMPLETE.equals(fieldName))
			return scopeFunctionAutoComplete = buildScopeFunctionAutoComplete((ScopeFunction) value);
		
		if(FIELD_REGION_SELECT_ONE.equals(fieldName))
			return buildLocalityRegionSelectOne((Locality) value);
		if(FIELD_DEPARTMENT_SELECT_ONE.equals(fieldName))
			return buildLocalityDepartmentSelectOne((Locality) value);
		if(FIELD_SUB_PREFECTURE_SELECT_ONE.equals(fieldName))
			return buildLocalitySousPrefectureSelectOne((Locality) value);
		
		if(FIELD_BUDGET_CATEGORY_SELECT_ONE.equals(fieldName))
			return buildBudgetCategorySelectOne((BudgetCategory) value);
		
		return null;
	}
	
	private SelectOneCombo buildExerciseSelectOne(Integer exercise) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,exercise,SelectOneCombo.FIELD_CHOICE_CLASS,Integer.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Integer>() {
			@Override
			public Collection<Integer> computeChoices(AbstractInputChoice<Integer> input) {
				Collection<Integer> choices = List.of(2021,2022);
				return choices;
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Exercice");
		return input;
	}
	
	private SelectOneCombo buildBudgetCategorySelectOne(BudgetCategory budgetCategory) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,budgetCategory,SelectOneCombo.FIELD_CHOICE_CLASS,BudgetCategory.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<BudgetCategory>() {
			@Override
			public Collection<BudgetCategory> computeChoices(AbstractInputChoice<BudgetCategory> input) {
				Collection<BudgetCategory> choices = visibleBudgetCategories == null ? __inject__(BudgetCategoryController.class).readVisiblesByLoggedInActorCodeForUI() : new ArrayList<>(visibleBudgetCategories);
				if(budgetCategoryInitial == null)
					budgetCategoryInitial = CollectionHelper.getFirst(choices);
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"C.B.");
		return input;
	}
	
	private SelectOneCombo buildSectionSelectOne(Section section) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,section,SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_LISTENER
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
					//administrativeUnitSelectOne.selectByValueSystemIdentifier();
				}
				if(budgetSpecializationUnitSelectOne != null) {
					budgetSpecializationUnitSelectOne.updateChoices();
					//budgetSpecializationUnitSelectOne.selectByValueSystemIdentifier();
				}
				
				if(expenditureNatureSelectOne != null) {
					expenditureNatureSelectOne.updateChoices();
					expenditureNatureSelectOne.selectFirstChoice();
				}
				if(activityCategorySelectOne != null) {
					activityCategorySelectOne.updateChoices();
					activityCategorySelectOne.selectFirstChoice();
				}
				
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					activitySelectOne.selectFirstChoice();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Section");
		return input;
	}
	
	private SelectOneCombo buildAdministrativeUnitSelectOne(AdministrativeUnit administrativeUnit) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,administrativeUnit,SelectOneCombo.FIELD_CHOICE_CLASS,AdministrativeUnit.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<AdministrativeUnit>() {
			public Collection<AdministrativeUnit> computeChoices(AbstractInputChoice<AdministrativeUnit> input) {
				Collection<AdministrativeUnit> choices = null;
				if(sectionSelectOne == null || sectionSelectOne.getValue() == null)
					return null;
				choices = EntityReader.getInstance().readMany(AdministrativeUnit.class, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
						, AdministrativeUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,FieldHelper.readSystemIdentifier(sectionSelectOne.getValue()));
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			public void select(AbstractInputChoiceOne input, AdministrativeUnit administrativeUnit) {
				super.select(input, administrativeUnit);
				if(expenditureNatureSelectOne != null) {
					expenditureNatureSelectOne.updateChoices();
					//expenditureNatureSelectOne.selectFirstChoice();
				}
				if(activityCategorySelectOne != null) {
					activityCategorySelectOne.updateChoices();
					//activityCategorySelectOne.selectFirstChoice();
				}
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					//activitySelectOne.selectFirstChoice();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"U.A.");
		return input;
	}
	
	private SelectOneCombo buildBudgetSpecializationUnitSelectOne(BudgetSpecializationUnit budgetSpecializationUnit) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,budgetSpecializationUnit,SelectOneCombo.FIELD_CHOICE_CLASS,BudgetSpecializationUnit.class
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
				if(expenditureNatureSelectOne != null) {
					expenditureNatureSelectOne.updateChoices();
					//expenditureNatureSelectOne.selectFirstChoice();
				}
				if(activityCategorySelectOne != null) {
					activityCategorySelectOne.updateChoices();
					//activityCategorySelectOne.selectFirstChoice();
				}
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					//activitySelectOne.selectFirstChoice();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"U.S.B.");
		return input;
	}
	
	private SelectOneCombo buildExpenditureNatureSelectOne(ExpenditureNature expenditureNature) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,expenditureNature,SelectOneCombo.FIELD_CHOICE_CLASS,ExpenditureNature.class,SelectOneCombo.FIELD_LISTENER
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
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					//activitySelectOne.selectFirstChoice();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"N.D.");
		return input;
	}
	
	private SelectOneCombo buildActivityCategorySelectOne(ActivityCategory activityCategory) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,activityCategory,SelectOneCombo.FIELD_CHOICE_CLASS,ActivityCategory.class,SelectOneCombo.FIELD_LISTENER
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
				if(activitySelectOne != null) {
					activitySelectOne.updateChoices();
					//activitySelectOne.selectFirstChoice();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"C.A.");
		return input;
	}
	
	private SelectOneCombo buildActivitySelectOne(Activity activity) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,activity,SelectOneCombo.FIELD_CHOICE_CLASS,Activity.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Activity>() {
			public Collection<Activity> computeChoices(AbstractInputChoice<Activity> input) {
				if(AbstractInput.getValue(administrativeUnitSelectOne) == null && AbstractInput.getValue(budgetSpecializationUnitSelectOne) == null)
					return null;
				Arguments<Activity> arguments = new Arguments<>();
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments());
				arguments.getRepresentationArguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto()
						.setQueryIdentifier(ActivityQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI));
				if(administrativeUnitSelectOne != null && administrativeUnitSelectOne.getValue() != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_IDENTIFIER
							,FieldHelper.readSystemIdentifier(administrativeUnitSelectOne.getValue()));
				if(budgetSpecializationUnitSelectOne != null && budgetSpecializationUnitSelectOne.getValue() != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER
							,FieldHelper.readSystemIdentifier(budgetSpecializationUnitSelectOne.getValue()));
				if(expenditureNatureSelectOne != null && expenditureNatureSelectOne.getValue() != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_EXPENDITURE_NATURE_IDENTIFIER
							,FieldHelper.readSystemIdentifier(expenditureNatureSelectOne.getValue()));
				if(activityCategorySelectOne != null && activityCategorySelectOne.getValue() != null)
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(ActivityQuerier.PARAMETER_NAME_CATEGORY_IDENTIFIER
							,FieldHelper.readSystemIdentifier(activityCategorySelectOne.getValue()));								
				Collection<Activity> choices = EntityReader.getInstance().readMany(Activity.class, arguments);				
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Activité");
		return input;
	}
	
	private AutoComplete buildScopeFunctionAutoComplete(ScopeFunction scopeFunction) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ScopeFunction.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Poste",AutoComplete.FIELD_VALUE,scopeFunction
				,AutoComplete.FIELD_READ_QUERY_IDENTIFIER,ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_BY_FUNCTIONS_CODES
				,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<ScopeFunction>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto()
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTIONS_CODES, ci.gouv.dgbf.system.actor.server.persistence.entities.Function.EXECUTION_HOLDERS_CODES)
						;
			}
			
			@Override
			public Arguments<ScopeFunction> instantiateArguments(AutoComplete autoComplete) {
				Arguments<ScopeFunction> arguments = super.instantiateArguments(autoComplete);
				arguments.getRepresentationArguments().getQueryExecutorArguments().addProcessableTransientFieldsNames(ScopeFunction.FIELD_FUNCTION_CODE);
				return arguments;
			}
		});
		input.enableAjaxItemSelect();
		return input;
	}
	
	private SelectOneCombo buildLocalityRegionSelectOne(Locality region) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,region,SelectOneCombo.FIELD_CHOICE_CLASS,Locality.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Locality>() {
			@Override
			public Collection<Locality> computeChoices(AbstractInputChoice<Locality> input) {
				Collection<Locality> choices = __inject__(LocalityController.class).readRegions();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Locality locality) {
				super.select(input, locality);
				if(departmentSelectOne != null) {
					departmentSelectOne.updateChoices();
					//administrativeUnitSelectOne.selectByValueSystemIdentifier();
				}
				if(subPrefectureSelectOne != null) {
					subPrefectureSelectOne.updateChoices();
					//budgetSpecializationUnitSelectOne.selectByValueSystemIdentifier();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Région");
		return input;
	}
	
	private SelectOneCombo buildLocalityDepartmentSelectOne(Locality department) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,department,SelectOneCombo.FIELD_CHOICE_CLASS,Locality.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Locality>() {
			public Collection<Locality> computeChoices(AbstractInputChoice<Locality> input) {
				Collection<Locality> choices = null;
				if(regionSelectOne == null || regionSelectOne.getValue() == null)
					return null;
				choices = __inject__(LocalityController.class).readByParent((Locality)regionSelectOne.getValue());
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
			public void select(AbstractInputChoiceOne input, Locality locality) {
				super.select(input, locality);
				if(subPrefectureSelectOne != null) {
					subPrefectureSelectOne.updateChoices();
					//expenditureNatureSelectOne.selectFirstChoice();
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Département");
		return input;
	}
	
	private SelectOneCombo buildLocalitySousPrefectureSelectOne(Locality sousPrefecture) {		
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,sousPrefecture,SelectOneCombo.FIELD_CHOICE_CLASS,Locality.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Locality>() {
			public Collection<Locality> computeChoices(AbstractInputChoice<Locality> input) {
				Collection<Locality> choices = null;
				if(departmentSelectOne == null || departmentSelectOne.getValue() == null)
					return null;
				choices = __inject__(LocalityController.class).readByParent((Locality)departmentSelectOne.getValue());
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"S.P.");
		return input;
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(budgetCategorySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetCategorySelectOne.getOutputLabel().setTitle("Catégorie budget"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetCategorySelectOne,Cell.FIELD_WIDTH,11));	
		}
		
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,9));	
		}
		
		if(exerciseSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,exerciseSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,exerciseSelectOne,Cell.FIELD_WIDTH,1));	
		}
		
		if(budgetSpecializationUnitSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetSpecializationUnitSelectOne.getOutputLabel().setTitle("Unité de spécialisation du budget"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetSpecializationUnitSelectOne,Cell.FIELD_WIDTH,3));
		}
		
		if(administrativeUnitSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnitSelectOne.getOutputLabel().setTitle("Unité administrative"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnitSelectOne,Cell.FIELD_WIDTH,7));
		}
		
		if(expenditureNatureSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,expenditureNatureSelectOne.getOutputLabel().setTitle("Nature de dépense"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,expenditureNatureSelectOne,Cell.FIELD_WIDTH,3));	
		}
		
		if(activityCategorySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activityCategorySelectOne.getOutputLabel().setTitle("Catégorie d'activité"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activityCategorySelectOne,Cell.FIELD_WIDTH,7));	
		}
		
		if(activitySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne,Cell.FIELD_WIDTH,10));	
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectionController.getShowDialogCommandButton(),Cell.FIELD_WIDTH,1));
		}
		/*	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,3));
		*/	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeFunctionAutoComplete.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeFunctionAutoComplete,Cell.FIELD_WIDTH,11));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,regionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,regionSelectOne,Cell.FIELD_WIDTH,2));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,departmentSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,departmentSelectOne,Cell.FIELD_WIDTH,3));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,subPrefectureSelectOne.getOutputLabel().setTitle("Sous-Préfecture"),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,subPrefectureSelectOne,Cell.FIELD_WIDTH,3));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,1));
			
		return cellsMaps;
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(activityInitial == null && budgetSpecializationUnitInitial == null && budgetCategoryInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_BUDGET_CATEGORY_AS_STRING);	
		if(activityInitial == null && budgetSpecializationUnitInitial == null && sectionInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_SECTION_AS_STRING);
		if(activityInitial == null && administrativeUnitInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING);	
		if(activityInitial == null && budgetSpecializationUnitInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING);			
		if(activityInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_ACTIVITY_AS_STRING);
		columnsFieldsNames.add(Assignments.FIELD_ECONOMIC_NATURE_AS_STRING);	
		//if(activity == null)
		//	columnsFieldsNames.addAll(List.of(Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING));
		if(activityInitial == null && expenditureNatureInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_EXPENDITURE_NATURE_AS_STRING);
		if(activityInitial == null && activityCategoryInitial == null)
			columnsFieldsNames.add(Assignments.FIELD_ACTIVITY_CATEGORY_AS_STRING);			
		
		if(scopeFunctionInitial == null || StringHelper.isBlank(scopeFunctionInitial.getFunctionCode())) {
			columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
					,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
		}else {
			if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER.equals(scopeFunctionInitial.getFunctionCode()))
				columnsFieldsNames.addAll(List.of(Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
						,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER.equals(scopeFunctionInitial.getFunctionCode()))
				columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING
						,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER.equals(scopeFunctionInitial.getFunctionCode()))
				columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
						,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING));
			else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER.equals(scopeFunctionInitial.getFunctionCode()))
				columnsFieldsNames.addAll(List.of(Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
						,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING));					
		}		
		return columnsFieldsNames;
	}
	
	public String generateWindowTitleValue(String prefix) {
		Collection<String> strings = generateWindowTitleValues(prefix);
		return StringHelper.concatenate(strings, " | ");
	}
	
	protected Collection<String> generateWindowTitleValues(String prefix) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(exerciseInitial != null) {
			strings.add("Exercice "+exerciseInitial);
		}
		if(CollectionHelper.isEmpty(activitiesInitial)) {
			if(budgetCategoryInitial != null) {
				strings.add(budgetCategoryInitial.toString());
			}
			if(sectionInitial != null) {
				if(administrativeUnitInitial == null && budgetSpecializationUnitInitial == null)
					strings.add(sectionInitial.toString());
				else
					strings.add("Section "+sectionInitial.getCode());
			}
			if(administrativeUnitInitial != null) {
				strings.add(administrativeUnitInitial.toString());
			}
			if(budgetSpecializationUnitInitial != null) {
				if(actionInitial == null && activityInitial == null)
					strings.add(budgetSpecializationUnitInitial.toString());
				else
					strings.add((budgetSpecializationUnitInitial.getCode().startsWith("1") ? "Dotation":"Programme")+" "+budgetSpecializationUnitInitial.getCode());
			}
			if(actionInitial != null) {
				if(activityInitial == null)
					strings.add(actionInitial.toString());
				else
					strings.add("Action "+actionInitial.getCode());
			}
			
			if(activityInitial == null) {
				if(expenditureNatureInitial != null)
					strings.add("Nature de dépense : "+expenditureNatureInitial.toString());
				if(activityCategoryInitial != null)
					strings.add("Catégorie d'activité : "+activityCategoryInitial.toString());	
			}else {
				strings.add(activityInitial.toString());
			}
			
			if(ValueHelper.isNotBlank(WebController.getInstance().getRequestParameter(ParameterName.stringify(Locality.class)))) {
				if(regionInitial != null && departmentInitial == null && subPrefectureInitial == null)
					strings.add(regionInitial.toString());
				
				if(departmentInitial != null && subPrefectureInitial == null)
					strings.add(departmentInitial.toString());
				
				if(subPrefectureInitial != null)
					strings.add(subPrefectureInitial.toString());
			}
			
			if(scopeFunctionInitial == null) {
					
			}else {
				strings.add(scopeFunctionInitial.toString());
			}
		}else {
			activitiesInitial.forEach(a -> {
				strings.add(a.toString());
			});
		}
		
		return strings;
	}
	
	public Integer getExercise() {
		Object value = AbstractInput.getValue(exerciseSelectOne);
		return NumberHelper.getInteger(value, 2021);
	}
	
	public Section getSection() {
		return (Section) AbstractInput.getValue(sectionSelectOne);
	}
	
	public BudgetCategory getBudgetCategory() {
		return (BudgetCategory) AbstractInput.getValue(budgetCategorySelectOne);
	}
	
	public AdministrativeUnit getAdministrativeUnit() {
		return (AdministrativeUnit) AbstractInput.getValue(administrativeUnitSelectOne);
	}
	
	public BudgetSpecializationUnit getBudgetSpecializationUnit() {
		return (BudgetSpecializationUnit) AbstractInput.getValue(budgetSpecializationUnitSelectOne);
	}
	
	public Action getAction() {
		return (Action) AbstractInput.getValue(actionSelectOne);
	}
	
	public ExpenditureNature getExpenditureNature() {
		return (ExpenditureNature) AbstractInput.getValue(expenditureNatureSelectOne);
	}
	
	public ActivityCategory getActivityCategory() {
		return (ActivityCategory) AbstractInput.getValue(activityCategorySelectOne);
	}
	
	public Activity getActivity() {
		return (Activity) AbstractInput.getValue(activitySelectOne);
	}
	
	public Collection<Activity> getActivities() {
		return activitiesInitial;
	}
	
	public Function getFunction() {
		return (Function) AbstractInput.getValue(functionSelectOne);
	}
	
	public ScopeFunction getScopeFunction() {
		return (ScopeFunction) AbstractInput.getValue(scopeFunctionAutoComplete);
	}
	
	public Locality getRegion() {
		return (Locality) AbstractInput.getValue(regionSelectOne);
	}
	
	public Locality getDepartment() {
		return (Locality) AbstractInput.getValue(departmentSelectOne);
	}
	
	public Locality getSubPrefecture() {
		return (Locality) AbstractInput.getValue(subPrefectureSelectOne);
	}
	
	public Locality getLocality() {
		Locality locality = getSubPrefecture();
		if(locality != null)
			return locality;
		locality = getDepartment();
		if(locality != null)
			return locality;
		return getRegion();
	}
	
	@Override
	protected String buildParameterName(String fieldName) {
		if(FIELD_EXERCISE_SELECT_ONE.equals(fieldName))
			return "exercice";
		return super.buildParameterName(fieldName);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(exerciseSelectOne == input)
			return "exercice";
		return super.buildParameterName(input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		if(exerciseSelectOne == input)
			return (String) exerciseSelectOne.getValue();
		return super.buildParameterValue(input);
	}
	
	@Override
	protected Boolean isSelectRedirectorArgumentsParameter(Class<?> klass, AbstractInput<?> input) {
		if(Locality.class.equals(klass) && input == regionSelectOne)
			return AbstractInput.getValue(departmentSelectOne) == null && AbstractInput.getValue(subPrefectureSelectOne) == null;
		if(Locality.class.equals(klass) && input == departmentSelectOne)
			return AbstractInput.getValue(subPrefectureSelectOne) == null;
		return super.isSelectRedirectorArgumentsParameter(klass, input);
	}
	
	public static final String FIELD_BUDGET_CATEGORY_SELECT_ONE = "budgetCategorySelectOne";
	public static final String FIELD_EXERCISE_SELECT_ONE = "exerciseSelectOne";
	public static final String FIELD_SECTION_SELECT_ONE = "sectionSelectOne";
	public static final String FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE = "administrativeUnitSelectOne";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_SELECT_ONE = "budgetSpecializationUnitSelectOne";
	public static final String FIELD_EXPENDITURE_NATURE_SELECT_ONE = "expenditureNatureSelectOne";
	public static final String FIELD_ACTIVITY_CATEGORY_SELECT_ONE = "activityCategorySelectOne";
	public static final String FIELD_ACTIVITY_SELECT_ONE = "activitySelectOne";
	public static final String FIELD_FUNCTION_SELECT_ONE = "functionSelectOne";
	public static final String FIELD_SCOPE_FUNCTION_AUTO_COMPLETE = "scopeFunctionAutoComplete";
	public static final String FIELD_REGION_SELECT_ONE = "regionSelectOne";
	public static final String FIELD_DEPARTMENT_SELECT_ONE = "departmentSelectOne";
	public static final String FIELD_SUB_PREFECTURE_SELECT_ONE = "subPrefectureSelectOne";
}