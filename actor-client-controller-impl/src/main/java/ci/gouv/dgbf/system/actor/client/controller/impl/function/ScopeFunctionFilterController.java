package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFunctionFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo sectionSelectOne,functionSelectOne;
	
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
		buildInputSelectOne(FIELD_NAME_FUNCTION_SELECT_ONE, Function.class);
		functionSelectOne.selectByValueSystemIdentifier();
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
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
	
	/**/
	
	//public static final String FIELD_NAME_SECTION_SELECT_ONE = "sectionSelectOne";
	public static final String FIELD_NAME_FUNCTION_SELECT_ONE = "functionSelectOne";
}