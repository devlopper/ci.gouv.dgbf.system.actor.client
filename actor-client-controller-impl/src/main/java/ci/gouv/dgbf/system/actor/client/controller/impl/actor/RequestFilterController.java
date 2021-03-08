package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.FunctionListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeListPage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo functionSelectOne,sectionSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne;
	
	@Override
	public RequestFilterController build() {
		return (RequestFilterController) super.build();
	}
	
	@Override
	public RequestFilterController ignore(String... fieldNames) {
		return (RequestFilterController) super.ignore(fieldNames);
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_NAME_SECTION_SELECT_ONE, Section.class);
		buildInputSelectOne(FIELD_NAME_FUNCTION_SELECT_ONE, Function.class);
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_NAME_SECTION_SELECT_ONE.equals(fieldName))
			return ScopeListPage.buildSectionSelectOne((Section) value, Boolean.TRUE);
		if(FIELD_NAME_FUNCTION_SELECT_ONE.equals(fieldName))
			return FunctionListPage.buildSelectOne((Function) value);
		return null;
	}

	public Section getSection() {
		return (Section) AbstractInput.getValue(sectionSelectOne);
	}
	
	public Function getFunction() {
		return (Function) AbstractInput.getValue(functionSelectOne);
	}
	
	public BudgetSpecializationUnit getBudgetSpecializationUnit() {
		return (BudgetSpecializationUnit) AbstractInput.getValue(budgetSpecializationUnitSelectOne);
	}
	
	public AdministrativeUnit getAdministrativeUnit() {
		return (AdministrativeUnit) AbstractInput.getValue(administrativeUnitSelectOne);
	}
	
	/**/
	
	public static final String FIELD_NAME_SECTION_SELECT_ONE = "sectionSelectOne";
	public static final String FIELD_NAME_FUNCTION_SELECT_ONE = "functionSelectOne";
}