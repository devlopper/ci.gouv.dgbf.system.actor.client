package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueConverter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestScopeFunctionFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo grantedSelectOne;
	private InputText electronicMailAddressInputText;
	
	private Boolean grantedInitial;
	private String electronicMailAddressInitial;
	
	public RequestScopeFunctionFilterController() {}
	
	public RequestScopeFunctionFilterController(RequestScopeFunctionFilterController requestScopeFunctionFilterController) {
		grantedInitial = requestScopeFunctionFilterController.grantedInitial;
		electronicMailAddressInitial = requestScopeFunctionFilterController.electronicMailAddressInitial;
	}
	
	public RequestScopeFunctionFilterController initialize() {
		grantedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(RequestScopeFunction.FIELD_GRANTED));
		electronicMailAddressInitial = WebController.getInstance().getRequestParameter(RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS);
		return this;
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_GRANTED_SELECT_ONE, Boolean.class);
		buildInputText(FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT);		
		enableValueChangeListeners();
	}
	
	private void enableValueChangeListeners() {}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return Helper.buildGrantedSelectOneCombo((Boolean) value);
		if(FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT.equals(fieldName)) {
			InputText inputText = Helper.buildElectronicMailAddressInputText((String) value);
			return inputText;
		}
		return null;
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return grantedInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String getInputTextInitialValue(String fieldName) {
		if(FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT.equals(fieldName))
			return electronicMailAddressInitial;
		return super.getInputTextInitialValue(fieldName);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName) || input == grantedSelectOne)
			return RequestScopeFunction.FIELD_GRANTED;
		if(FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT.equals(fieldName) || input == electronicMailAddressInputText)
			return RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		if(input == grantedSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		return super.buildParameterValue(input);
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		
		if(electronicMailAddressInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,electronicMailAddressInputText.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,electronicMailAddressInputText,Cell.FIELD_WIDTH,10));	
		}
		
		if(grantedSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,grantedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,grantedSelectOne,Cell.FIELD_WIDTH,10));		
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,12));	
		return cellsMaps;
	}
	
	public String generateWindowTitleValue(String prefix) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(StringHelper.isNotBlank(electronicMailAddressInitial))
			strings.add(electronicMailAddressInitial);
		if(grantedInitial != null)
			strings.add(grantedInitial ? "Accordées" : "Refusées");
		
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(StringHelper.isBlank(electronicMailAddressInitial))
			columnsFieldsNames.addAll(List.of(RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS));
		
		columnsFieldsNames.addAll(List.of(RequestScopeFunction.FIELD_REGISTRATION_NUMBER,RequestScopeFunction.FIELD_FIRST_NAME,RequestScopeFunction.FIELD_LAST_NAMES
				,RequestScopeFunction.FIELD_SECTION_STRING,RequestScopeFunction.FIELD_ADMINISTRATIVE_UNIT_FUNCTION,RequestScopeFunction.FIELD_ACT_OF_APPOINTMENT_REFERENCE,RequestScopeFunction.FIELD_ADMINISTRATIVE_UNIT_STRING
				,RequestScopeFunction.FIELD_MOBILE_PHONE_NUMBER,RequestScopeFunction.FIELD_OFFICE_PHONE_NUMBER,RequestScopeFunction.FIELD_POSTAL_BOX_ADDRESS
				,RequestScopeFunction.FIELD_SCOPE_FUNCTION_STRING));
		
		if(grantedInitial == null)
			columnsFieldsNames.addAll(List.of(RequestScopeFunction.FIELD_GRANTED_STRING));
		return columnsFieldsNames;
	}

	public String getElectronicMailAddress() {
		return (String)AbstractInput.getValue(electronicMailAddressInputText);
	}
	
	public Boolean getGranted() {
		return (Boolean) AbstractInput.getValue(grantedSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(grantedSelectOne == input)
			return RequestScopeFunction.FIELD_GRANTED;
		return super.buildParameterName(input);
	}
	
	public static Filter.Dto populateFilter(Filter.Dto filter,RequestScopeFunctionFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(RequestScopeFunctionQuerier.PARAMETER_NAME_GRANTED, Boolean.TRUE.equals(initial) ? controller.grantedInitial : controller.getGranted(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(RequestScopeFunctionQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS, Boolean.TRUE.equals(initial) ? controller.electronicMailAddressInitial : controller.getElectronicMailAddress(), filter);
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(RequestScopeFunctionFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	
	/**/
	
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT = "electronicMailAddressInputText";
	public static final String FIELD_GRANTED_SELECT_ONE = "grantedSelectOne";
}