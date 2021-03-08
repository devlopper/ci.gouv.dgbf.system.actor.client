package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.actor.client.controller.api.ActivityCategoryController;
import ci.gouv.dgbf.system.actor.client.controller.api.BudgetSpecializationUnitController;
import ci.gouv.dgbf.system.actor.client.controller.api.ExpenditureNatureController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Action;
import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.ActivitySelectionController;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExpenditureNatureQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class AssignmentsFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo sectionSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,actionSelectOne,activitySelectOne,activityCategorySelectOne
		,expenditureNatureSelectOne;
	private ActivitySelectionController activitySelectionController;
	
	private Section sectionInitial;
	private AdministrativeUnit administrativeUnitInitial;
	private BudgetSpecializationUnit budgetSpecializationUnitInitial;
	private ActivityCategory activityCategoryInitial;
	private ExpenditureNature expenditureNatureInitial;
	private Activity activityInitial;
	
	public AssignmentsFilterController() {
		activityInitial = WebController.getInstance().getUsingRequestParameterParentAsSystemIdentifierByQueryIdentifier(Activity.class
					, ActivityQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_WITH_CODES_NAMES_FOR_UI);		
		if(activityInitial != null) {
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
	}
	
	@Override
	public AssignmentsFilterController build() {
		activitySelectionController = new ActivitySelectionController();
		return (AssignmentsFilterController) super.build();
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
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
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_SECTION_SELECT_ONE, Section.class);
		buildInputSelectOne(FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE, AdministrativeUnit.class);
		buildInputSelectOne(FIELD_BUDGET_SPECIALIZATION_UNIT_SELECT_ONE, BudgetSpecializationUnit.class);
		buildInputSelectOne(FIELD_EXPENDITURE_NATURE_SELECT_ONE, ExpenditureNature.class);
		buildInputSelectOne(FIELD_ACTIVITY_CATEGORY_SELECT_ONE, ActivityCategory.class);
		buildInputSelectOne(FIELD_ACTIVITY_SELECT_ONE, Activity.class);		
		enableValueChangeListeners();		
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		sectionSelectOne.enableValueChangeListener(List.of(administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
		administrativeUnitSelectOne.enableValueChangeListener(List.of(expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
		budgetSpecializationUnitSelectOne.enableValueChangeListener(List.of(expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
		expenditureNatureSelectOne.enableValueChangeListener(List.of(activitySelectOne));
		activityCategorySelectOne.enableValueChangeListener(List.of(activitySelectOne));
		activitySelectOne.enableValueChangeListener(List.of());
	}
	
	private void selectByValueSystemIdentifier() {
		sectionSelectOne.selectByValueSystemIdentifier();		
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
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
		return null;
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
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,11));	
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
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,expenditureNatureSelectOne,Cell.FIELD_WIDTH,5));	
		}
		
		if(activityCategorySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activityCategorySelectOne.getOutputLabel().setTitle("Catégorie d'activité"),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activityCategorySelectOne,Cell.FIELD_WIDTH,5));	
		}
		
		if(activitySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne,Cell.FIELD_WIDTH,9));	
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectionController.getShowDialogCommandButton(),Cell.FIELD_WIDTH,1));
		
		return cellsMaps;
	}
	
	public Section getSection() {
		return (Section) AbstractInput.getValue(sectionSelectOne);
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
	
	public static final String FIELD_SECTION_SELECT_ONE = "sectionSelectOne";
	public static final String FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE = "administrativeUnitSelectOne";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_SELECT_ONE = "budgetSpecializationUnitSelectOne";
	public static final String FIELD_EXPENDITURE_NATURE_SELECT_ONE = "expenditureNatureSelectOne";
	public static final String FIELD_ACTIVITY_CATEGORY_SELECT_ONE = "activityCategorySelectOne";
	public static final String FIELD_ACTIVITY_SELECT_ONE = "activitySelectOne";
}