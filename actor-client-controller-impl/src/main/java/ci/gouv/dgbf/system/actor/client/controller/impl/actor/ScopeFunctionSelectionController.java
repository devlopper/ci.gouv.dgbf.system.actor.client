package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.FunctionComparator;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetSpecializationUnitQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFunctionSelectionController implements Serializable {

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
	
	public ScopeFunctionSelectionController() {
		buildScopeFunctionSelectOne();
		
		//buildFinancialControllerServiceSelectOne();
		//buildAccountingServiceSelectOne();
		
		buildAdministrativeUnitSelectOne();
		buildBudgetSpecializationUnitSelectOne();
		
		buildSectionSelectOne();
		buildFunctionSelectOne();
		
		buildAddCommandButton();
		
		buildLayout();
	}
	
	public void remove(ScopeFunction scopeFunction) {
		if(CollectionHelper.isEmpty(selected))
			return;
		selected.remove(scopeFunction);
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
				layout.setCellsRenderedByIndexes(Boolean.FALSE, 2,3,4,5,6,7,8,9/*,10,11,12,13*/);

				if(Boolean.TRUE.equals(function.isCreditManager())) {
					layout.setCellsRenderedByIndexes(Boolean.TRUE, 2,3,4,5,8,9);
					if(administrativeUnitSelectOne != null) {				
						administrativeUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
						administrativeUnitSelectOne.updateChoices();
						administrativeUnitSelectOne.selectFirstChoice();
					}
				}else if(Boolean.TRUE.equals(function.isAuthorizingOfficer())) {
					layout.setCellsRenderedByIndexes(Boolean.TRUE, 2,3,6,7,8,9);
					if(budgetSpecializationUnitSelectOne != null) {				
						budgetSpecializationUnitSelectOne.setChoicesInitialized(Boolean.FALSE);
						budgetSpecializationUnitSelectOne.updateChoices();
						budgetSpecializationUnitSelectOne.selectFirstChoice();
					}
				}else if(Boolean.TRUE.equals(function.isFinancialController())) {
					layout.setCellsRenderedByIndexes(Boolean.TRUE, 8,9);
					if(scopeFunctionSelectOne != null) {				
						scopeFunctionSelectOne.setChoicesInitialized(Boolean.FALSE);
						scopeFunctionSelectOne.updateChoices();
						//scopeFunctionSelectOne.selectFirstChoice();
					}
				}else if(Boolean.TRUE.equals(function.isAccounting())) {
					layout.setCellsRenderedByIndexes(Boolean.TRUE, 8,9);
					if(scopeFunctionSelectOne != null) {				
						scopeFunctionSelectOne.setChoicesInitialized(Boolean.FALSE);
						scopeFunctionSelectOne.updateChoices();
						//scopeFunctionSelectOne.selectFirstChoice();
					}
				}
			}
		}).enableValueChangeListener(List.of(layoutIdentifier));
	}
	
	private void buildSectionSelectOne() {
		List<Section> sections = (List<Section>) EntityReader.getInstance().readMany(Section.class, SectionQuerier.QUERY_IDENTIFIER_READ);
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
				List<AdministrativeUnit> administrativeUnits = (List<AdministrativeUnit>) EntityReader.getInstance().readMany(AdministrativeUnit.class
						, AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_BY_SECTION_IDENTIFIER_FOR_UI
						,AdministrativeUnitQuerier.PARAMETER_NAME_SECTION_IDENTIFIER,((Section)sectionSelectOne.getValue()).getIdentifier());
				if(CollectionHelper.getSize(administrativeUnits)>1)
					administrativeUnits.add(0, null);
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
			protected Collection<ScopeFunction> __computeChoices__(AbstractInputChoice<ScopeFunction> input, Class<?> entityClass) {
				if(functionSelectOne == null || functionSelectOne.getValue() ==  null)
					return null;
				Function function = (Function)functionSelectOne.getValue();
				String functionIdentifier = function.getIdentifier();
				String scopeCode = null;
				if(Boolean.TRUE.equals(function.isCreditManager())) {
					if(administrativeUnitSelectOne == null || administrativeUnitSelectOne.getValue() ==  null)
						return null;
					scopeCode = ((AdministrativeUnit)administrativeUnitSelectOne.getValue()).getCode();
				}
				if(Boolean.TRUE.equals(function.isAuthorizingOfficer())) {
					if(budgetSpecializationUnitSelectOne == null || budgetSpecializationUnitSelectOne.getValue() ==  null)
						return null;
					scopeCode = ((BudgetSpecializationUnit)budgetSpecializationUnitSelectOne.getValue()).getCode();
				}
				
				List<ScopeFunction> scopeFunctions = (List<ScopeFunction>) EntityReader.getInstance().readMany(ScopeFunction.class, ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER
						,ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER,functionIdentifier
						,ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_CODE_NAME,scopeCode);
				if(CollectionHelper.getSize(scopeFunctions)>1)
					scopeFunctions.add(0, null);
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
					if(CollectionHelper.isNotEmpty(selected)) {
						if(!selected.stream()
							.filter(x -> x.getIdentifier().equals( ((ScopeFunction)scopeFunctionSelectOne.getValue()).getIdentifier())).collect(Collectors.toList()).isEmpty())
						return null;
					}
					if(selected == null)
						selected = new LinkedHashSet<>();
					selected.add((ScopeFunction) scopeFunctionSelectOne.getValue());
					
					//if(CollectionHelper.isNotEmpty(scopeFunctionSelectOne.getChoices()))
					//	scopeFunctionSelectOne.getChoices().remove(scopeFunctionSelectOne.getValue());
					
					return null;
				}
			});
		addCommandButton.setProcess("@this");
		addCommandButton.addUpdates(scopeFunctionsListIdentifier);
	}
	
	private void buildLayout() {
		layout = Layout.build(Layout.FIELD_IDENTIFIER,layoutIdentifier,Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Je suis"),Cell.FIELD_WIDTH,3)
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
		layout.setCellsRenderedByIndexes(Boolean.FALSE, 2,3,4,5,6,7,8,9);		
	}
}