package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.primefaces.PrimeFaces;

import ci.gouv.dgbf.system.actor.client.controller.api.BudgetCategoryComparator;
import ci.gouv.dgbf.system.actor.client.controller.api.FunctionComparator;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFunctionSelectionController implements Serializable {

	private SelectOneCombo budgetCategorySelectOne;
	private BudgetCategory budgetCategory;
	private String selectedBudgetCategoryAsString;
	
	private SelectOneCombo categorySelectOne;
	
	private SelectOneCombo functionSelectOne;
	private SelectOneCombo sectionSelectOne;
	
	private SelectOneCombo administrativeUnitSelectOne;
	private SelectOneCombo budgetSpecializationUnitSelectOne;
	private SelectOneCombo financialControllerServiceSelectOne;
	private SelectOneCombo accountingServiceSelectOne;
	
	private SelectOneCombo scopeFunctionSelectOne;
	private Collection<ScopeFunction> selected;
	private String scopeFunctionsListIdentifier = RandomHelper.getAlphabetic(4);
	
	private CommandButton addCommandButton;
	
	private Layout layout;
	private String layoutIdentifier = RandomHelper.getAlphabetic(4);
	
	private Boolean showBudgetCategoryColumn = Boolean.FALSE;
	
	public ScopeFunctionSelectionController(BudgetCategory budgetCategory) {
		this.budgetCategory = budgetCategory;
		buildScopeFunctionSelectOne();
		
		//buildFinancialControllerServiceSelectOne();
		//buildAccountingServiceSelectOne();
		
		buildAdministrativeUnitSelectOne();
		buildBudgetSpecializationUnitSelectOne();
		
		buildSectionSelectOne();
		buildFunctionSelectOne();
		
		buildBudgetCategorySelectOne();
		
		buildAddCommandButton();
		
		buildLayout();
		
		budgetCategorySelectOne.select(budgetCategory);
	}
	
	public void remove(ScopeFunction scopeFunction) {
		if(CollectionHelper.isEmpty(selected))
			return;
		selected.remove(scopeFunction);
		selectedBudgetCategoryAsString = null;
	}
	
	private void buildBudgetCategorySelectOne() {
		List<BudgetCategory> budgetCategories;
		if(budgetCategory == null) {
			budgetCategories = (List<BudgetCategory>) EntityReader.getInstance().readMany(BudgetCategory.class, BudgetCategoryQuerier.QUERY_IDENTIFIER_READ_BY_CODES_FOR_UI
					,FunctionQuerier.PARAMETER_NAME_CODES,List.of(
							 ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_GENERAL
							,ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_EPN
							));
			Collections.sort(budgetCategories,new BudgetCategoryComparator());
			
			if(CollectionHelper.getSize(budgetCategories)>1)
				budgetCategories.add(0, null);
		}else {
			budgetCategories  = List.of(budgetCategory);
		}
		
		budgetCategorySelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,BudgetCategory.class,SelectOneCombo.FIELD_CHOICES,budgetCategories
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<BudgetCategory>() {
			
			@Override
			public Object getChoiceLabel(AbstractInputChoice<BudgetCategory> input, BudgetCategory budgetCategory) {
				return budgetCategory == null ? super.getChoiceLabel(input, budgetCategory) : budgetCategory.getName();
			}
			
			public void select(AbstractInputChoiceOne input, BudgetCategory budgetCategory) {
				super.select(input, budgetCategory);
				renderInitial();
				//layout.setCellsRenderedByIndexes(Boolean.FALSE, 2,3,4,5,6,7,8,9/*,10,11,12,13*/);
				
				functionSelectOne.selectChoiceAt(0);
				
				if(budgetCategory != null) {
					if(ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_GENERAL.equals(budgetCategory.getCode())) {
						categoryRendered(Boolean.TRUE);
						/*layout.setCellsRenderedByIndexes(Boolean.TRUE, 2,3,4,5,8,9);
						if(administrativeUnitSelectOne != null) {				
							administrativeUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
							administrativeUnitSelectOne.updateChoices();
							administrativeUnitSelectOne.selectFirstChoice();
						}*/
					}else if(ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_EPN.equals(budgetCategory.getCode())) {
						categoryRendered(Boolean.TRUE);
						/*layout.setCellsRenderedByIndexes(Boolean.TRUE, 2,3,6,7,8,9);
						if(budgetSpecializationUnitSelectOne != null) {				
							budgetSpecializationUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
							budgetSpecializationUnitSelectOne.updateChoices();
							budgetSpecializationUnitSelectOne.selectFirstChoice();
						}*/
					}
				}else {
					categoryRendered(Boolean.FALSE);
				}
			}
		}).enableValueChangeListener(List.of(layoutIdentifier));
	}
	
	private void buildFunctionSelectOne() {
		List<Function> functions = (List<Function>) EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_BY_CODES_FOR_UI
				,FunctionQuerier.PARAMETER_NAME_CODES,List.of(
						 ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER
						,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT
						));
		Collections.sort(functions,new FunctionComparator());
		
		if(CollectionHelper.getSize(functions)>1)
			functions.add(0, null);
		functionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_CHOICES,functions
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Function>() {
			
			@Override
			public Object getChoiceLabel(AbstractInputChoice<Function> input, Function function) {
				return function == null ? super.getChoiceLabel(input, function) : function.getName();
			}
			
			public void select(AbstractInputChoiceOne input, Function function) {
				super.select(input, function);
				//layout.setCellsRenderedByIndexes(Boolean.FALSE, 2,3,4,5,6,7,8,9/*,10,11,12,13*/);
				renderInitial();
				categoryRendered(Boolean.TRUE);
				if(function != null) {
					if(Boolean.TRUE.equals(function.isCreditManager())) {
						sectionRendered(Boolean.TRUE).administrativeUnitRendered(Boolean.TRUE).scopeFunctionRendered(Boolean.TRUE);
						//layout.setCellsRenderedByIndexes(Boolean.TRUE, 2,3,4,5,8,9);
						if(administrativeUnitSelectOne != null) {				
							administrativeUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
							administrativeUnitSelectOne.updateChoices();
							administrativeUnitSelectOne.selectFirstChoice();
						}
					}else if(Boolean.TRUE.equals(function.isAuthorizingOfficer())) {
						sectionRendered(Boolean.TRUE).budgetSpecializationUnitRendered(Boolean.TRUE).scopeFunctionRendered(Boolean.TRUE);
						//layout.setCellsRenderedByIndexes(Boolean.TRUE, 2,3,6,7,8,9);
						if(budgetSpecializationUnitSelectOne != null) {				
							budgetSpecializationUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
							budgetSpecializationUnitSelectOne.updateChoices();
							budgetSpecializationUnitSelectOne.selectFirstChoice();
						}
					}else if(Boolean.TRUE.equals(function.isFinancialController())) {
						scopeFunctionRendered(Boolean.TRUE);
						//layout.setCellsRenderedByIndexes(Boolean.TRUE, 8,9);
						if(scopeFunctionSelectOne != null) {				
							scopeFunctionSelectOne.setChoicesInitialized(Boolean.FALSE);
							scopeFunctionSelectOne.updateChoices();
							//scopeFunctionSelectOne.selectFirstChoice();
						}
					}else if(Boolean.TRUE.equals(function.isAccounting())) {
						scopeFunctionRendered(Boolean.TRUE);
						//layout.setCellsRenderedByIndexes(Boolean.TRUE, 8,9);
						if(scopeFunctionSelectOne != null) {				
							scopeFunctionSelectOne.setChoicesInitialized(Boolean.FALSE);
							scopeFunctionSelectOne.updateChoices();
							//scopeFunctionSelectOne.selectFirstChoice();
						}
					}
				}else {
					sectionSelectOne.selectChoiceAt(0);
				}
			}
		}).enableValueChangeListener(List.of(layoutIdentifier));
	}
	
	private void buildSectionSelectOne() {
		List<Section> sections = (List<Section>) EntityReader.getInstance().readMany(Section.class, SectionQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
		if(CollectionHelper.getSize(sections)>1)
			sections.add(0,null);
		sectionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_CHOICES, sections
				, SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<Section>() {	
			public void select(AbstractInputChoiceOne input, Section choice) {
				super.select(input, choice);
				if(administrativeUnitSelectOne != null) {				
					administrativeUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
					administrativeUnitSelectOne.updateChoices();
					administrativeUnitSelectOne.selectFirstChoice();
				}
				if(budgetSpecializationUnitSelectOne != null) {				
					budgetSpecializationUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
					budgetSpecializationUnitSelectOne.updateChoices();
					budgetSpecializationUnitSelectOne.selectFirstChoice();
				}
			}
		}).enableValueChangeListener(List.of(budgetSpecializationUnitSelectOne,administrativeUnitSelectOne,scopeFunctionSelectOne));
	}
	
	private void buildAdministrativeUnitSelectOne() {
		administrativeUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,AdministrativeUnit.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<AdministrativeUnit>() {	
			protected Collection<AdministrativeUnit> __computeChoices__(AbstractInputChoice<AdministrativeUnit> input, Class<?> entityClass) {
				if(sectionSelectOne == null || sectionSelectOne.getValue() ==  null)
					return null;
				/*
				List<AdministrativeUnit> administrativeUnits = (List<AdministrativeUnit>) EntityReader.getInstance().readMany(AdministrativeUnit.class
						, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
						,AdministrativeUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,((Section)sectionSelectOne.getValue()).getIdentifier());
				if(CollectionHelper.getSize(administrativeUnits)>1)
					administrativeUnits.add(0, null);
				return administrativeUnits;
				*/
				String serviceGroupCode;
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_EPN.equals(budgetCategory.getCode()))
					serviceGroupCode = "32";
				else
					serviceGroupCode = "";
				Collection<AdministrativeUnit> administrativeUnits = EntityReader.getInstance().readMany(AdministrativeUnit.class,AdministrativeUnitQuerier
						.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_BY_SERVICE_GROUP_CODE_STARTS_WITH_FOR_UI
						,AdministrativeUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,((Section)sectionSelectOne.getValue()).getIdentifier()
						,AdministrativeUnitQuerier.PARAMETER_NAME_SERVICE_GROUP_CODE,serviceGroupCode
						);
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(administrativeUnits);
				return administrativeUnits;
			}
			
			@Override
			public void select(AbstractInputChoiceOne inputChoice, AdministrativeUnit administrativeUnit) {
				super.select(inputChoice, administrativeUnit);
				if(scopeFunctionSelectOne != null) {
					scopeFunctionSelectOne.setChoicesInitialized(Boolean.TRUE);
					scopeFunctionSelectOne.updateChoices();
					scopeFunctionSelectOne.selectFirstChoice();			
				}
			}
		}).enableValueChangeListener(List.of(scopeFunctionSelectOne));
	}
	
	private void buildBudgetSpecializationUnitSelectOne() {
		budgetSpecializationUnitSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,BudgetSpecializationUnit.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<BudgetSpecializationUnit>() {	
			protected Collection<BudgetSpecializationUnit> __computeChoices__(AbstractInputChoice<BudgetSpecializationUnit> input, Class<?> entityClass) {
				if(sectionSelectOne == null || sectionSelectOne.getValue() ==  null)
					return null;
				List<BudgetSpecializationUnit> budgetSpecializationUnits = (List<BudgetSpecializationUnit>) EntityReader.getInstance().readMany(BudgetSpecializationUnit.class
						, BudgetSpecializationUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
						,BudgetSpecializationUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,((Section)sectionSelectOne.getValue()).getIdentifier());
				if(CollectionHelper.getSize(budgetSpecializationUnits)>1)
					budgetSpecializationUnits.add(0, null);
				return budgetSpecializationUnits;
			}
			
			@Override
			public void select(AbstractInputChoiceOne inputChoice, BudgetSpecializationUnit budgetSpecializationUnit) {
				super.select(inputChoice, budgetSpecializationUnit);
				if(scopeFunctionSelectOne != null) {
					scopeFunctionSelectOne.setChoicesInitialized(Boolean.TRUE);
					scopeFunctionSelectOne.updateChoices();
					scopeFunctionSelectOne.selectFirstChoice();			
				}
			}
		}).enableValueChangeListener(List.of(scopeFunctionSelectOne));
	}
	/*
	private void buildFinancialControllerServiceSelectOne() {
		financialControllerServiceSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,FinancialControllerService.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<FinancialControllerService>() {	
			protected Collection<FinancialControllerService> __computeChoices__(AbstractInputChoice<FinancialControllerService> input, Class<?> entityClass) {
				if(sectionSelectOne == null || sectionSelectOne.getValue() ==  null)
					return null;
				List<FinancialControllerService> financialControllerServices = (List<FinancialControllerService>) EntityReader.getInstance().readMany(FinancialControllerService.class
						, FinancialControllerServiceQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
				if(CollectionHelper.getSize(financialControllerServices)>1)
					financialControllerServices.add(0, null);
				return financialControllerServices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne inputChoice, FinancialControllerService financialControllerService) {
				super.select(inputChoice, financialControllerService);
				if(scopeFunctionSelectOne != null) {
					scopeFunctionSelectOne.setChoicesInitialized(Boolean.TRUE);
					scopeFunctionSelectOne.updateChoices();
					scopeFunctionSelectOne.selectFirstChoice();			
				}
			}
		}).enableValueChangeListener(List.of(scopeFunctionSelectOne));
	}
	
	private void buildAccountingServiceSelectOne() {
		accountingServiceSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,AccountingService.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<AccountingService>() {	
			protected Collection<AccountingService> __computeChoices__(AbstractInputChoice<AccountingService> input, Class<?> entityClass) {
				if(sectionSelectOne == null || sectionSelectOne.getValue() ==  null)
					return null;
				List<AccountingService> accountingServices = (List<AccountingService>) EntityReader.getInstance().readMany(AccountingService.class
						, AccountingServiceQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
				if(CollectionHelper.getSize(accountingServices)>1)
					accountingServices.add(0, null);
				return accountingServices;
			}
			
			@Override
			public void select(AbstractInputChoiceOne inputChoice, AccountingService financialControllerService) {
				super.select(inputChoice, financialControllerService);
				if(scopeFunctionSelectOne != null) {
					scopeFunctionSelectOne.setChoicesInitialized(Boolean.TRUE);
					scopeFunctionSelectOne.updateChoices();
					scopeFunctionSelectOne.selectFirstChoice();			
				}
			}
		}).enableValueChangeListener(List.of(scopeFunctionSelectOne));
	}
	*/
	private void buildScopeFunctionSelectOne() {
		scopeFunctionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,ScopeFunction.class
				,SelectOneCombo.FIELD_LISTENER,new SelectOneCombo.Listener.AbstractImpl<ScopeFunction>() {	
			
			@Override
			public Boolean getIsChoiceDisabled(AbstractInputChoice<ScopeFunction> input, ScopeFunction choice) {
				if(choice != null)
					return Boolean.TRUE.equals(choice.getIsHolder()) && Boolean.TRUE.equals(choice.getGranted());
				return super.getIsChoiceDisabled(input, choice);
			}
			
			protected Collection<ScopeFunction> __computeChoices__(AbstractInputChoice<ScopeFunction> input, Class<?> entityClass) {
				if(functionSelectOne == null || functionSelectOne.getValue() ==  null)
					return null;
				Function function = (Function)functionSelectOne.getValue();
				String functionIdentifier = function.getIdentifier();
				Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>();
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments());
				arguments.getRepresentationArguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto());
				if(Boolean.TRUE.equals(function.isCreditManager())) {
					if(administrativeUnitSelectOne == null || administrativeUnitSelectOne.getValue() ==  null)
						return null;
					arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI);
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterFieldsValues(
							/*ScopeFunctionQuerier.PARAMETER_NAME_BUDGET_CATEGORY_IDENTIFIER,((BudgetCategory) budgetCategorySelectOne.getValue()).getIdentifier()
							,*/ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER,functionIdentifier
							,ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_CODE_NAME,((AdministrativeUnit)administrativeUnitSelectOne.getValue()).getCode());
				}
				if(Boolean.TRUE.equals(function.isAuthorizingOfficer())) {
					if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() ==  null)
						return null;
					arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_FUNCTION_IDENTIFIER_BY_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER);
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterFieldsValues(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER
							,functionIdentifier,ScopeFunctionQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT_IDENTIFIER,((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getIdentifier());
				}
				if(Boolean.TRUE.equals(function.isFinancialController())) {
					//if(AbstractInput.getValue(financialControllerServiceSelectOne) == null)
					//	return null;
					arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI);
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterFieldsValues(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER
							,functionIdentifier);
				}
				if(Boolean.TRUE.equals(function.isAccounting())) {
					//if(AbstractInput.getValue(accountingServiceSelectOne) == null)
					//	return null;
					arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI);
					arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterFieldsValues(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER
							,functionIdentifier);
				}
				
				ArrayList<String> list = new ArrayList<>();
				list.addAll(List.of(ScopeFunction.FIELD_REQUESTED,ScopeFunction.FIELD_GRANTED,ScopeFunction.FIELD_IS_HOLDER));
				arguments.getRepresentationArguments().getQueryExecutorArguments().setProcessableTransientFieldsNames(list);
				List<ScopeFunction> scopeFunctions = (List<ScopeFunction>) EntityReader.getInstance().readMany(ScopeFunction.class, arguments);
				if(CollectionHelper.isNotEmpty(scopeFunctions)) {
					scopeFunctions = scopeFunctions.stream().filter(x -> ((BudgetCategory) budgetCategorySelectOne.getValue()).getCode().equals(x.getBudgetCategoryCode()))
						.collect(Collectors.toList());
				}
				if(CollectionHelper.getSize(scopeFunctions)>1) {
					scopeFunctions.add(0, null);
				}else if(CollectionHelper.getSize(scopeFunctions) == 1) {
					ScopeFunction scopeFunction = scopeFunctions.iterator().next();
					if(Boolean.TRUE.equals(scopeFunction.getIsHolder()) && Boolean.TRUE.equals(scopeFunction.getGranted()))
						scopeFunctions.add(0, null);
				}
				//if(CollectionHelper.isNotEmpty(scopeFunctions) && CollectionHelper.isNotEmpty(ScopeFunctionSelectionController.this.scopeFunctions)) {
				//	scopeFunctions.removeAll(ScopeFunctionSelectionController.this.scopeFunctions);
				//}
				return scopeFunctions;
			}		
		});
		scopeFunctionSelectOne.enableValueChangeListener(null);
	}

	private void buildAddCommandButton() {
		addCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus"
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_NULLABLE,Boolean.TRUE
				,CommandButton.FIELD_USER_INTERFACE_ACTION
				,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
				@Override
				protected Object __runExecuteFunction__(AbstractAction action) {
					if(scopeFunctionSelectOne == null || scopeFunctionSelectOne.getValue() == null)
						throw new RuntimeException("Veuillez sélectionner une fonction budgétaire");
					ScopeFunction scopeFunction = (ScopeFunction) scopeFunctionSelectOne.getValue();
					if(StringHelper.isBlank(selectedBudgetCategoryAsString))
						selectedBudgetCategoryAsString = scopeFunction.getBudgetCategoryAsString();
					else if(!selectedBudgetCategoryAsString.equals(scopeFunction.getBudgetCategoryAsString()))
						throw new RuntimeException("Veuillez sélectionner une fonction budgétaire de la catégorie de budget : "+selectedBudgetCategoryAsString);
					if(CollectionHelper.isNotEmpty(selected)) {
						if(!selected.stream()
							.filter(x -> x.getIdentifier().equals( ((ScopeFunction)scopeFunctionSelectOne.getValue()).getIdentifier())).collect(Collectors.toList()).isEmpty())
						return null;
					}
					addScopeFunction((ScopeFunction) scopeFunctionSelectOne.getValue());
					PrimeFaces.current().executeScript("PF('scopeFunctionSelectorDialog').hide();");
					return null;
				}
			});
		addCommandButton.setProcess("@this");
		addCommandButton.addUpdates(scopeFunctionsListIdentifier);
	}
	
	public void addScopeFunction(ScopeFunction scopeFunction) {
		if(scopeFunction == null)
			return;
		if(selected == null)
			selected = new LinkedHashSet<>();
		selected.add(scopeFunction);
		//if(CollectionHelper.isNotEmpty(scopeFunctionSelectOne.getChoices()))
		//	scopeFunctionSelectOne.getChoices().remove(scopeFunctionSelectOne.getValue());
	}
	
	private void buildLayout() {
		layout = Layout.build(Layout.FIELD_IDENTIFIER,layoutIdentifier,Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Catégorie de budget"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,budgetCategorySelectOne,Cell.FIELD_WIDTH,9)
						
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Je suis"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,9)
						
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,9)
						
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Unité administrative"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnitSelectOne,Cell.FIELD_WIDTH,9)
						
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Unité de spécialisation du budget"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,budgetSpecializationUnitSelectOne,Cell.FIELD_WIDTH,9)
						
						/*
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Contrôle financier"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,financialControllerServiceSelectOne,Cell.FIELD_WIDTH,9)
						
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Poste comptable"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,accountingServiceSelectOne,Cell.FIELD_WIDTH,9)
						*/
						,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Fonction budgétaire"),Cell.FIELD_WIDTH,3)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,scopeFunctionSelectOne,Cell.FIELD_WIDTH,9)
						
						,MapHelper.instantiate(Cell.FIELD_CONTROL,addCommandButton,Cell.FIELD_WIDTH,12)
					));
		//addCommandButton.setProcess("@this");
		renderInitial();
		//layout.setCellsRenderedByIndexes(Boolean.FALSE, 2,3,4,5,6,7,8,9);		
	}
	
	private ScopeFunctionSelectionController budgetCategoryRendered(Boolean rendered) {
		layout.setCellsRenderedByIndexes(rendered, BUDGET_CATEGORY_LABEL_INDEX,BUDGET_CATEGORY_INPUT_INDEX);
		return this;
	}
	
	private ScopeFunctionSelectionController categoryRendered(Boolean rendered) {
		layout.setCellsRenderedByIndexes(rendered, CATEGORY_LABEL_INDEX,CATEGORY_INPUT_INDEX);
		return this;
	}
	
	private ScopeFunctionSelectionController sectionRendered(Boolean rendered) {
		layout.setCellsRenderedByIndexes(rendered, SECTION_LABEL_INDEX,SECTION_INPUT_INDEX);
		return this;
	}
	
	private ScopeFunctionSelectionController administrativeUnitRendered(Boolean rendered) {
		layout.setCellsRenderedByIndexes(rendered, ADMINISTRATIVE_UNIT_LABEL_INDEX,ADMINISTRATIVE_UNIT_INPUT_INDEX);
		return this;
	}
	
	private ScopeFunctionSelectionController budgetSpecializationUnitRendered(Boolean rendered) {
		layout.setCellsRenderedByIndexes(rendered, BUDGET_SPECIALIZATION_UNIT_LABEL_INDEX,BUDGET_SPECIALIZATION_UNIT_INPUT_INDEX);
		return this;
	}
	
	private ScopeFunctionSelectionController scopeFunctionRendered(Boolean rendered) {
		layout.setCellsRenderedByIndexes(rendered, SCOPE_FUNCTION_LABEL_INDEX,SCOPE_FUNCTION_INPUT_INDEX);
		return this;
	}
	
	private ScopeFunctionSelectionController renderInitial() {
		budgetCategoryRendered(Boolean.TRUE).categoryRendered(Boolean.FALSE).sectionRendered(Boolean.FALSE).administrativeUnitRendered(Boolean.FALSE).budgetSpecializationUnitRendered(Boolean.FALSE).scopeFunctionRendered(Boolean.FALSE);
		return this;
	}
	
	private static final Integer BUDGET_CATEGORY_LABEL_INDEX = 0;
	private static final Integer BUDGET_CATEGORY_INPUT_INDEX = BUDGET_CATEGORY_LABEL_INDEX+1;
	
	private static final Integer CATEGORY_LABEL_INDEX = BUDGET_CATEGORY_INPUT_INDEX+1;
	private static final Integer CATEGORY_INPUT_INDEX = CATEGORY_LABEL_INDEX+1;
	
	private static final Integer SECTION_LABEL_INDEX = CATEGORY_INPUT_INDEX+1;
	private static final Integer SECTION_INPUT_INDEX = SECTION_LABEL_INDEX+1;
	
	private static final Integer ADMINISTRATIVE_UNIT_LABEL_INDEX = SECTION_INPUT_INDEX+1;
	private static final Integer ADMINISTRATIVE_UNIT_INPUT_INDEX = ADMINISTRATIVE_UNIT_LABEL_INDEX+1;
	
	private static final Integer BUDGET_SPECIALIZATION_UNIT_LABEL_INDEX = ADMINISTRATIVE_UNIT_INPUT_INDEX+1;
	private static final Integer BUDGET_SPECIALIZATION_UNIT_INPUT_INDEX = BUDGET_SPECIALIZATION_UNIT_LABEL_INDEX+1;
	
	private static final Integer SCOPE_FUNCTION_LABEL_INDEX = BUDGET_SPECIALIZATION_UNIT_INPUT_INDEX+1;
	private static final Integer SCOPE_FUNCTION_INPUT_INDEX = SCOPE_FUNCTION_LABEL_INDEX+1;
}