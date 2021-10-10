package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueConverter;
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
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.FunctionListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;
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

	private SelectOneCombo sectionSelectOne,functionSelectOne,typeSelectOne,statusSelectOne,administrativeUnitSelectOne,budgetSpecializationUnitSelectOne
		,processedSelectOne,acceptedSelectOne;
	private InputText searchInputText;
	
	private Section sectionInitial;
	private AdministrativeUnit administrativeUnitInitial;
	private Function functionInitial;
	private RequestType typeInitial;
	private RequestStatus statusInitial;
	private String searchInitial;
	private Boolean processedInitial;
	private Boolean acceptedInitial;
	
	public RequestFilterController() {
		sectionInitial = getSectionFromRequestParameter();
		administrativeUnitInitial = getAdministrativeUnitFromRequestParameter();
		functionInitial = getFunctionFromRequestParameter();
		typeInitial = getTypeFromRequestParameter();
		statusInitial = getStatusFromRequestParameter();
		searchInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_SEARCH_INPUT_TEXT));
		processedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(Request.FIELD_PROCESSED));
		acceptedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(Request.FIELD_ACCEPTED));
	}
	
	public static Section getSectionFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Section.class, new Arguments<Section>()
				.queryIdentifier(SectionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(Section.FIELD_IDENTIFIER,Section.FIELD_CODE,Section.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Section.class))));
	}
	
	public static AdministrativeUnit getAdministrativeUnitFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(AdministrativeUnit.class, new Arguments<AdministrativeUnit>()
				.queryIdentifier(AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(AdministrativeUnit.FIELD_IDENTIFIER,AdministrativeUnit.FIELD_CODE,AdministrativeUnit.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(AdministrativeUnit.class))));
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
		buildInputSelectOne(FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE, AdministrativeUnit.class);
		buildInputSelectOne(FIELD_FUNCTION_SELECT_ONE, Function.class);
		buildInputSelectOne(FIELD_TYPE_SELECT_ONE, RequestType.class);
		buildInputSelectOne(FIELD_PROCESSED_SELECT_ONE, Boolean.class);
		buildInputSelectOne(FIELD_STATUS_SELECT_ONE, RequestStatus.class);
		buildInputText(FIELD_SEARCH_INPUT_TEXT);		
		buildInputSelectOne(FIELD_ACCEPTED_SELECT_ONE, Boolean.class);
		
		enableValueChangeListeners();
	}
	
	private void enableValueChangeListeners() {
		if(processedSelectOne != null)
			processedSelectOne.enableValueChangeListener(CollectionHelper.listOf(Boolean.TRUE,statusSelectOne));
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return ScopeListPage.buildSectionSelectOne((Section) value, Boolean.TRUE);
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return FunctionListPage.buildSelectOne((Function) value);
		if(FIELD_TYPE_SELECT_ONE.equals(fieldName))
			return RequestTypeListPage.buildSelectOne((RequestType) value);
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return Helper.buildProcessedSelectOneCombo((Boolean) value,this,FIELD_STATUS_SELECT_ONE);
		if(FIELD_STATUS_SELECT_ONE.equals(fieldName))
			return RequestStatusListPage.buildSelectOne((RequestStatus) value,this,FIELD_PROCESSED_SELECT_ONE);		
		if(FIELD_ACCEPTED_SELECT_ONE.equals(fieldName))
			return Helper.buildAcceptedSelectOneCombo((Boolean) value);
		return null;
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return sectionInitial;
		if(FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE.equals(fieldName))
			return administrativeUnitInitial;
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return functionInitial;
		if(FIELD_TYPE_SELECT_ONE.equals(fieldName))
			return typeInitial;
		if(FIELD_STATUS_SELECT_ONE.equals(fieldName))
			return statusInitial;
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return processedInitial;
		if(FIELD_ACCEPTED_SELECT_ONE.equals(fieldName))
			return acceptedInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName) || input == searchInputText)
			return Request.FIELD_SEARCH;
		if(FIELD_ACCEPTED_SELECT_ONE.equals(fieldName) || input == acceptedSelectOne)
			return Request.FIELD_ACCEPTED;
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName) || input == processedSelectOne)
			return Request.FIELD_PROCESSED;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		if(input == acceptedSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		if(input == processedSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		return super.buildParameterValue(input);
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
		
		if(administrativeUnitSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnitSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,administrativeUnitSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(functionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,10));
		}
		/*
		
		if(searchInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText,Cell.FIELD_WIDTH,10));	
		}
		*/
		if(processedSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne,Cell.FIELD_WIDTH,10));		
		}
		
		if(statusSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,statusSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,statusSelectOne,Cell.FIELD_WIDTH,10));
		}
		/*
		if(acceptedSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,acceptedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,acceptedSelectOne,Cell.FIELD_WIDTH,10));
		}
		*/
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
		
		if(processedInitial != null) {
			strings.add(processedInitial ? "Traité" : "Non traité");
		}	
		if(acceptedInitial != null) {
			strings.add(acceptedInitial ? "Accepté" : "Rejeté");
		}		
		
		if(statusInitial != null)
			strings.add(statusInitial.toString());
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(sectionInitial == null)
			columnsFieldsNames.add(Request.FIELD_SECTION_AS_STRING);
		if(administrativeUnitInitial == null)
			columnsFieldsNames.add(Request.FIELD_ADMINISTRATIVE_UNIT_AS_STRING);
		if(typeInitial == null)
			columnsFieldsNames.add(Request.FIELD_TYPE_AS_STRING);
		if(statusInitial == null)
			columnsFieldsNames.add(Request.FIELD_STATUS_AS_STRING);
		columnsFieldsNames.addAll(List.of(Request.FIELD_CODE,Request.FIELD_FIRST_NAME,Request.FIELD_LAST_NAMES,Request.FIELD_REGISTRATION_NUMBER
				,Request.FIELD_ELECTRONIC_MAIL_ADDRESS,Request.FIELD_MOBILE_PHONE_NUMBER,Request.FIELD_SCOPE_FUNCTIONS_CODES));
		columnsFieldsNames.add(Request.FIELD_CREATION_DATE_AS_STRING);
		columnsFieldsNames.add(Request.FIELD_PROCESSING_DATE_AS_STRING);
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
		return (AdministrativeUnit)AbstractInput.getValue(administrativeUnitSelectOne);
	}
	
	public BudgetSpecializationUnit getBudgetSpecializationUnit() {
		return (BudgetSpecializationUnit)AbstractInput.getValue(budgetSpecializationUnitSelectOne);
	}
	
	public String getSearch() {
		return (String)AbstractInput.getValue(searchInputText);
	}
	
	public Boolean getProcessed() {
		return (Boolean) AbstractInput.getValue(processedSelectOne);
	}
	
	public Boolean getAccepted() {
		return (Boolean) AbstractInput.getValue(acceptedSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(processedSelectOne == input)
			return Request.FIELD_PROCESSED;
		if(acceptedSelectOne == input)
			return Request.FIELD_ACCEPTED;
		return super.buildParameterName(input);
	}
	
	public static Filter.Dto populateFilter(Filter.Dto filter,RequestFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNITS_SECTIONS_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.sectionInitial : controller.getSection())), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNITS_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.administrativeUnitInitial : controller.getAdministrativeUnit())), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.functionInitial : controller.getFunction())), filter);		
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_TYPES_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.typeInitial : controller.getType())), filter);
		
		Boolean processed = Boolean.TRUE.equals(initial) ? controller.processedInitial : controller.getProcessed();
		if(processed == null) {
			
		}else {
			filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_PROCESSED, processed, filter);			
			//filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_ACCEPTED, Boolean.TRUE.equals(initial) ? controller.acceptedInitial : controller.getAccepted(), filter);
		}
		filter = Filter.Dto.addFieldIfValueNotNull(RequestQuerier.PARAMETER_NAME_STATUS_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.statusInitial : controller.getStatus())), filter);
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
	public static final String FIELD_ADMINISTRATIVE_UNIT_SELECT_ONE = "administrativeUnitSelectOne";
	public static final String FIELD_FUNCTION_SELECT_ONE = "functionSelectOne";
	public static final String FIELD_TYPE_SELECT_ONE = "typeSelectOne";
	public static final String FIELD_STATUS_SELECT_ONE = "statusSelectOne";
	public static final String FIELD_PROCESSED_SELECT_ONE = "processedSelectOne";
	public static final String FIELD_ACCEPTED_SELECT_ONE = "acceptedSelectOne";
}