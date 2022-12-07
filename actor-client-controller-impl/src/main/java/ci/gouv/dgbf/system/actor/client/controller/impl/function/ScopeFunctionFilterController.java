package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;

import ci.gouv.dgbf.system.actor.client.controller.api.BudgetCategoryController;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFunctionFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo sectionSelectOne,budgetCategorySelectOne,functionSelectOne;
	
	@Override
	public ScopeFunctionFilterController build() {
		return (ScopeFunctionFilterController) super.build();
	}
	
	@Override
	public ScopeFunctionFilterController ignore(String... fieldNames) {
		return (ScopeFunctionFilterController) super.ignore(fieldNames);
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_NAME_BUDGET_CATEGORY_SELECT_ONE, BudgetCategory.class);
		budgetCategorySelectOne.selectByValueSystemIdentifier();
		buildInputSelectOne(FIELD_NAME_FUNCTION_SELECT_ONE, Function.class);
		functionSelectOne.selectByValueSystemIdentifier();
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_NAME_FUNCTION_SELECT_ONE.equals(fieldName))
			return FunctionListPage.buildSelectOne((Function) value);
		else if(FIELD_NAME_BUDGET_CATEGORY_SELECT_ONE.equals(fieldName))
			return buildBudgetCategorySelectOne((BudgetCategory) value);
		return null;
	}

	private SelectOneCombo buildBudgetCategorySelectOne(BudgetCategory budgetCategory) {
		SelectOneCombo selectOne = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,budgetCategory,SelectOneCombo.FIELD_CHOICE_CLASS,BudgetCategory.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<BudgetCategory>() {
			@Override
			public Collection<BudgetCategory> computeChoices(AbstractInputChoice<BudgetCategory> input) {
				Collection<BudgetCategory> choices = __inject__(BudgetCategoryController.class).readVisiblesByLoggedInActorCodeForUI();
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, BudgetCategory budgetCategory) {
				super.select(input, budgetCategory);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.LABEL);
		selectOne.updateChoices();
		selectOne.selectByValueSystemIdentifier();
		//functionSelectOne.enableValueChangeListener(List.of());
		return selectOne;
	}
	
	public Section getSection() {
		return (Section) AbstractInput.getValue(sectionSelectOne);
	}
	
	public BudgetCategory getBudgetCategory() {
		return (BudgetCategory) AbstractInput.getValue(budgetCategorySelectOne);
	}
	
	public Function getFunction() {
		return (Function) AbstractInput.getValue(functionSelectOne);
	}
	
	/**/
	
	public static final String FIELD_NAME_BUDGET_CATEGORY_SELECT_ONE = "budgetCategorySelectOne";
	public static final String FIELD_NAME_FUNCTION_SELECT_ONE = "functionSelectOne";
}