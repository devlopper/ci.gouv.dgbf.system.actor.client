package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.panel.Dialog;

import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Action;
import ci.gouv.dgbf.system.actor.client.controller.entities.Activity;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class AssignmentsFilterController implements Serializable {

	private Dialog dialog;
	private CommandButton showDialogCommandButton;
	
	private SelectOneCombo sectionSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,activitySelectOne,activityCategorySelectOne
		,expenditureNatureSelectOne;
	
	private Redirector.Arguments onSelectRedirectorArguments;
	
	private CommandButton filterCommandButton;
	
	private Layout layout;
	
	private Section section;
	private AdministrativeUnit administrativeUnit;
	private BudgetSpecializationUnit budgetSpecializationUnit;
	private Action action;
	private Activity activity;
	private ActivityCategory activityCategory;
	private ExpenditureNature expenditureNature;
	
	public AssignmentsFilterController(Section section,AdministrativeUnit administrativeUnit,BudgetSpecializationUnit budgetSpecializationUnit,Action action
			,Activity activity,ActivityCategory activityCategory,ExpenditureNature expenditureNature) {
		
		buildDialog();
		buildShowDialogCommandButton();
		
		buildSelectOneSection();
		
		buildFilterCommandButton();
		buildLayout();
	}
	
	public Redirector.Arguments getOnSelectRedirectorArguments(Boolean injectIfNull) {
		if(onSelectRedirectorArguments == null && Boolean.TRUE.equals(injectIfNull))
			onSelectRedirectorArguments = new Redirector.Arguments();
		return onSelectRedirectorArguments;
	}
	
	private void buildDialog() {
		dialog = Dialog.build(Dialog.FIELD_HEADER,"Filtre ligne(s) budgétaire(s)",Dialog.FIELD_MODAL,Boolean.TRUE
				,Dialog.ConfiguratorImpl.FIELD_COMMAND_BUTTONS_BUILDABLE,Boolean.FALSE);
		dialog.addStyleClasses("cyk-min-width-90-percent");
	}
	
	private void buildShowDialogCommandButton() {
		showDialogCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Filtrer",CommandButton.FIELD_ICON,"fa fa-filter"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.SHOW_DIALOG,CommandButton.FIELD___DIALOG__,dialog);		
	}
	

	private void buildSelectOneSection() {		
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
					administrativeUnitSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(administrativeUnit));
				}
				if(budgetSpecializationUnitSelectOne != null) {
					budgetSpecializationUnitSelectOne.updateChoices();					
					budgetSpecializationUnitSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(budgetSpecializationUnit));
				}
			}
		});
		sectionSelectOne.updateChoices();
		sectionSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(section));
		sectionSelectOne.enableValueChangeListener(List.of(administrativeUnitSelectOne,budgetSpecializationUnitSelectOne,expenditureNatureSelectOne,activityCategorySelectOne,activitySelectOne));
	}
	/*
	private void buildSelectOneAdministrativeUnit() {		
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
	
	private void buildSelectOneBudgetSpecializationUnit() {		
		budgetSpecializationUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class
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
	
	private void buildSelectOneExpenditureNature() {
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
	
	private void buildSelectOneActivityCategory() {
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
	
	private void buildSelectOneActivity() {		
		activitySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Activity.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Activity>() {
			public Collection<Activity> computeChoices(AbstractInputChoice<Activity> input) {
				if(AbstractInput.getValue(administrativeUnitSelectOne) == null && AbstractInput.getValue(budgetSpecializationUnitSelectOne) == null)
					return null;
				Arguments<Activity> arguments = new Arguments<>();
				arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments());
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
	*/
	
	private void buildFilterCommandButton() {
		filterCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Filtrer",CommandButton.FIELD_ICON,"fa fa-filter"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_LISTENER
				,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				Map<String,List<String>> map = new LinkedHashMap<>();
				
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
				Redirector.getInstance().redirect(onSelectRedirectorArguments);		
				return super.__runExecuteFunction__(action);
			}
		});
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		
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
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectOne,Cell.FIELD_WIDTH,10));	
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,1));
		//cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,activitySelectionController.getShowDialogCommandButton(),Cell.FIELD_WIDTH,1));
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
}