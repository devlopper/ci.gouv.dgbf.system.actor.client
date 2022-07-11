package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueConverter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestScopeFunctionFilterController extends AbstractFilterController implements Serializable {

	private AutoComplete functionAutoComplete;
	private SelectOneCombo grantedSelectOne;
	private InputText electronicMailAddressInputText;
	
	private Collection<Function> functionsInitial;
	private Boolean grantedInitial;
	private String electronicMailAddressInitial;
	
	private Collection<String> functionsCodes;
	
	public RequestScopeFunctionFilterController() {}
	
	public RequestScopeFunctionFilterController(RequestScopeFunctionFilterController requestScopeFunctionFilterController) {
		functionsInitial = requestScopeFunctionFilterController.functionsInitial;
		grantedInitial = requestScopeFunctionFilterController.grantedInitial;
		electronicMailAddressInitial = requestScopeFunctionFilterController.electronicMailAddressInitial;
	}
	
	public RequestScopeFunctionFilterController initialize() {
		Collection<String> functionsIdentifiers = WebController.getInstance().getRequestParameters(ParameterName.stringifyMany(Function.class));
		if(CollectionHelper.isNotEmpty(functionsIdentifiers))
			functionsInitial = EntityReader.getInstance().readMany(Function.class, new Arguments<Function>().queryIdentifier(FunctionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC)
					.filterByIdentifiers(functionsIdentifiers));
		grantedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(RequestScopeFunction.FIELD_GRANTED));
		electronicMailAddressInitial = WebController.getInstance().getRequestParameter(RequestScopeFunction.FIELD_ELECTRONIC_MAIL_ADDRESS);
		return this;
	}
	
	@Override
	protected void buildInputs() {
		//buildInputSelectOne(FIELD_FUNCTION_AUTO_COMPLETE, Function.class);
		buildInputSelectOne(FIELD_GRANTED_SELECT_ONE, Boolean.class);
		buildInputText(FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT);		
		//enableValueChangeListeners();
	}
	
	@Override
	protected void enableValueChangeListeners() {}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		//if(FIELD_FUNCTION_AUTO_COMPLETE.equals(fieldName))
		//	return functionAutoComplete = buildFunctionAutoComplete((ScopeFunction) value);
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return Helper.buildGrantedSelectOneCombo((Boolean) value);
		if(FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT.equals(fieldName)) {
			InputText inputText = Helper.buildElectronicMailAddressInputText((String) value);
			return inputText;
		}
		return null;
	}
	/*
	private AutoComplete buildFunctionAutoComplete(Collection<Function> functions) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Function.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Fonction",AutoComplete.FIELD_VALUE,functions
				,AutoComplete.FIELD_READ_QUERY_IDENTIFIER,FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_BY_FUNCTIONS_CODES
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
	}*/
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_FUNCTION_AUTO_COMPLETE.equals(fieldName))
			return functionsInitial;
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
		
		columnsFieldsNames.addAll(List.of(RequestScopeFunction.FIELD_REQUEST_CODE,RequestScopeFunction.FIELD_REGISTRATION_NUMBER,RequestScopeFunction.FIELD_FIRST_NAME,RequestScopeFunction.FIELD_LAST_NAMES
				,RequestScopeFunction.FIELD_SECTION_STRING,RequestScopeFunction.FIELD_ADMINISTRATIVE_UNIT_FUNCTION,RequestScopeFunction.FIELD_ACT_OF_APPOINTMENT_REFERENCE,RequestScopeFunction.FIELD_ADMINISTRATIVE_UNIT_STRING
				,RequestScopeFunction.FIELD_MOBILE_PHONE_NUMBER,RequestScopeFunction.FIELD_OFFICE_PHONE_NUMBER,RequestScopeFunction.FIELD_POSTAL_BOX_ADDRESS
				,RequestScopeFunction.FIELD_SCOPE_FUNCTION_STRING));
		
		if(grantedInitial == null)
			columnsFieldsNames.addAll(List.of(RequestScopeFunction.FIELD_GRANTED_STRING));
		return columnsFieldsNames;
	}

	@SuppressWarnings("unchecked")
	public Collection<Function> getFunctions() {
		return (Collection<Function>)AbstractInput.getValue(functionAutoComplete);
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
		if(CollectionHelper.isNotEmpty(controller.functionsCodes))
			filter = Filter.Dto.addFieldIfValueNotNull(RequestScopeFunctionQuerier.PARAMETER_NAME_FUNCTIONS_CODES, controller.functionsCodes, filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestScopeFunctionQuerier.PARAMETER_NAME_GRANTED, Boolean.TRUE.equals(initial) ? controller.grantedInitial : controller.getGranted(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(RequestScopeFunctionQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS, Boolean.TRUE.equals(initial) ? controller.electronicMailAddressInitial : controller.getElectronicMailAddress(), filter);
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(RequestScopeFunctionFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	public static RequestScopeFunctionFilterController instantiate() {
		RequestScopeFunctionFilterController filterController = new RequestScopeFunctionFilterController();
		return filterController;
	}
	
	/**/
	
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS_INPUT_TEXT = "electronicMailAddressInputText";
	public static final String FIELD_GRANTED_SELECT_ONE = "grantedSelectOne";
	public static final String FIELD_FUNCTION_AUTO_COMPLETE = "functionAutoComplete";
}