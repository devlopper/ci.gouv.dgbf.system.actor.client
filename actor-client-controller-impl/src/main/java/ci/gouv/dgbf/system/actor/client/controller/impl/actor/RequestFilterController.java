package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.FunctionListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestStatusQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo sectionSelectOne,functionSelectOne,typeSelectOne,statusSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne;
	private InputText searchInputText;
	
	private Section sectionInitial;
	private Function functionInitial;
	private RequestType typeInitial;
	private RequestStatus statusInitial;
	private String searchInitial;
	
	public RequestFilterController() {
		sectionInitial = getSectionFromRequestParameter();
		functionInitial = getFunctionFromRequestParameter();
		typeInitial = getTypeFromRequestParameter();
		statusInitial = getStatusFromRequestParameter();
		searchInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_SEARCH_INPUT_TEXT));		
	}
	
	public static Section getSectionFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Section.class, new Arguments<Section>()
				.queryIdentifier(SectionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(Section.FIELD_IDENTIFIER,Section.FIELD_CODE,Section.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Section.class))));
	}
	
	public static Function getFunctionFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Function.class, new Arguments<Function>()
				.queryIdentifier(FunctionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(Function.FIELD_IDENTIFIER,Function.FIELD_CODE,Function.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class))));
	}
	
	public static RequestType getTypeFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(RequestType.class, new Arguments<RequestType>()
				.queryIdentifier(RequestTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(RequestType.FIELD_IDENTIFIER,RequestType.FIELD_CODE,RequestType.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class))));
	}
	
	public static RequestStatus getStatusFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(RequestStatus.class, new Arguments<RequestStatus>()
				.queryIdentifier(RequestStatusQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(RequestStatus.FIELD_IDENTIFIER,RequestStatus.FIELD_CODE,RequestStatus.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestStatus.class))));
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_SECTION_SELECT_ONE, Section.class);
		buildInputSelectOne(FIELD_FUNCTION_SELECT_ONE, Function.class);
		buildInputSelectOne(FIELD_TYPE_SELECT_ONE, RequestType.class);
		buildInputSelectOne(FIELD_STATUS_SELECT_ONE, RequestStatus.class);
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return ScopeListPage.buildSectionSelectOne((Section) value, Boolean.TRUE);
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return FunctionListPage.buildSelectOne((Function) value);
		if(FIELD_TYPE_SELECT_ONE.equals(fieldName))
			return RequestTypeListPage.buildSelectOne((RequestType) value);
		if(FIELD_STATUS_SELECT_ONE.equals(fieldName))
			return RequestStatusListPage.buildSelectOne((RequestStatus) value);
		return null;
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return sectionInitial;
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return functionInitial;
		if(FIELD_TYPE_SELECT_ONE.equals(fieldName))
			return typeInitial;
		if(FIELD_STATUS_SELECT_ONE.equals(fieldName))
			return statusInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName) || input == searchInputText)
			return Request.FIELD_SEARCH;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(typeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,typeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,typeSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,10));
		}		
		
		if(functionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(statusSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,statusSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,statusSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(searchInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText,Cell.FIELD_WIDTH,10));	
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,12));	
		return cellsMaps;
	}
	
	public String generateWindowTitleValue(String prefix) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(sectionInitial != null)
			strings.add(sectionInitial.toString());
		if(functionInitial != null)
			strings.add(functionInitial.toString());
		if(typeInitial != null)
			strings.add(typeInitial.toString());
		if(statusInitial != null)
			strings.add(statusInitial.toString());
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(sectionInitial == null)
			columnsFieldsNames.add(Request.FIELD_SECTION_AS_STRING);
		if(typeInitial == null)
			columnsFieldsNames.add(Request.FIELD_TYPE_AS_STRING);
		if(statusInitial == null)
			columnsFieldsNames.add(Request.FIELD_STATUS_AS_STRING);
		columnsFieldsNames.addAll(List.of(Request.FIELD_CODE,Request.FIELD_FIRST_NAME,Request.FIELD_LAST_NAMES,Request.FIELD_REGISTRATION_NUMBER
				,Request.FIELD_ELECTRONIC_MAIL_ADDRESS,Request.FIELD_MOBILE_PHONE_NUMBER,Request.FIELD_SCOPE_FUNCTIONS_CODES));
		return columnsFieldsNames;
	}

	public Section getSection() {
		return (Section) AbstractInput.getValue(sectionSelectOne);
	}
	
	public Function getFunction() {
		return (Function) AbstractInput.getValue(functionSelectOne);
	}
	
	public RequestType getType() {
		return (RequestType)AbstractInput.getValue(typeSelectOne);
	}
	
	public RequestStatus getStatus() {
		return (RequestStatus)AbstractInput.getValue(statusSelectOne);
	}
	
	public AdministrativeUnit getAdministrativeUnit() {
		return (AdministrativeUnit)AbstractInput.getValue(statusSelectOne);
	}
	
	public BudgetSpecializationUnit getBudgetSpecializationUnit() {
		return (BudgetSpecializationUnit)AbstractInput.getValue(budgetSpecializationUnitSelectOne);
	}
	
	public String getSearch() {
		return (String)AbstractInput.getValue(searchInputText);
	}
	
	public static Filter.Dto populateFilter(Filter.Dto filter,RequestFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_SECTIONS_IDENTIFIERS, List.of(FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.sectionInitial : controller.getSection())), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS, List.of(FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.functionInitial : controller.getFunction())), filter);		
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_TYPES_IDENTIFIERS, List.of(FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.typeInitial : controller.getType())), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_STATUS_IDENTIFIERS, List.of(FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.statusInitial : controller.getStatus())), filter);		
		filter = Filter.Dto.addFieldIfValueNotBlank(RequestQuerier.PARAMETER_NAME_SEARCH, Boolean.TRUE.equals(initial) ? controller.searchInitial : controller.getSearch(), filter);
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(RequestFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	
	/**/
	
	public static final String FIELD_SEARCH_INPUT_TEXT = "searchInputText";
	public static final String FIELD_SECTION_SELECT_ONE = "sectionSelectOne";
	public static final String FIELD_FUNCTION_SELECT_ONE = "functionSelectOne";
	public static final String FIELD_TYPE_SELECT_ONE = "typeSelectOne";
	public static final String FIELD_STATUS_SELECT_ONE = "statusSelectOne";
}