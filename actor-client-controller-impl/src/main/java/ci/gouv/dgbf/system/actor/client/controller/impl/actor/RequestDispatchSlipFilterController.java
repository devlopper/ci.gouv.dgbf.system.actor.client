package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.api.BudgetCategoryController;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.FunctionListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestDispatchSlipFilterController extends AbstractFilterController implements Serializable {

	private SelectOneCombo budgetCategorySelectOne,sectionSelectOne,functionSelectOne,sentSelectOne,processedSelectOne;
	private InputText searchInputText;
	
	private BudgetCategory budgetCategoryInitial;
	private Section sectionInitial;
	private Function functionInitial;
	private Boolean sentInitial;
	private Boolean processedInitial;
	private String searchInitial;
	
	public RequestDispatchSlipFilterController() {}
	
	public RequestDispatchSlipFilterController(RequestDispatchSlipFilterController filterController) {
		budgetCategoryInitial = filterController.budgetCategoryInitial;
		sectionInitial = filterController.sectionInitial;
		functionInitial = filterController.functionInitial;
		sentInitial = filterController.sentInitial;
		processedInitial = filterController.processedInitial;
		searchInitial = filterController.searchInitial;
	}
	
	@Override
	public RequestDispatchSlipFilterController initialize() {
		if(budgetCategoryInitial == null)
			budgetCategoryInitial = getBudgetCategoryFromRequestParameter();
		if(sectionInitial == null)
			sectionInitial = getSectionFromRequestParameter();
		if(functionInitial == null)
			functionInitial = getFunctionFromRequestParameter();
		sentInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(RequestDispatchSlip.FIELD_SENT));
		processedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(RequestDispatchSlip.FIELD_PROCESSED));
		searchInitial = WebController.getInstance().getRequestParameter(buildParameterName(RequestDispatchSlip.FIELD_SEARCH));
		return this;
	}

	public static BudgetCategory getBudgetCategoryFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(BudgetCategory.class, new Arguments<BudgetCategory>()
				.queryIdentifier(BudgetCategoryQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(BudgetCategory.FIELD_IDENTIFIER,BudgetCategory.FIELD_CODE,BudgetCategory.FIELD_NAME)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(BudgetCategory.class))));
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

	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_BUDGET_CATEGORY_SELECT_ONE, BudgetCategory.class);
		buildInputSelectOne(FIELD_SECTION_SELECT_ONE, Section.class);
		buildInputSelectOne(FIELD_FUNCTION_SELECT_ONE, Function.class);
		buildInputSelectOne(FIELD_SENT_SELECT_ONE, Boolean.class);
		buildInputSelectOne(FIELD_PROCESSED_SELECT_ONE, Boolean.class);
		buildInputText(FIELD_SEARCH_INPUT_TEXT);

		//enableValueChangeListeners();
	}
	
	@Override
	protected void enableValueChangeListeners() {
			
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_BUDGET_CATEGORY_SELECT_ONE.equals(fieldName))
			return buildBudgetCategorySelectOne((BudgetCategory) value);
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return Helper.buildSectionSelectOne((Section) value, null,null);
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return FunctionListPage.buildSelectOne((Function) value,null,null);
		if(FIELD_SENT_SELECT_ONE.equals(fieldName))
			return Helper.buildSentSelectOneCombo((Boolean) value,null,null);
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return Helper.buildProcessedSelectOneCombo((Boolean) value,null,null);
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName)) {
			InputText inputText = Helper.buildSearchInputText((String) value);
			inputText.setPlaceholder("Numéro");
			return inputText;
		}
		return null;
	}
	
	public static SelectOneCombo buildBudgetCategorySelectOne(BudgetCategory budgetCategory) {
		SelectOneCombo select = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,BudgetCategory.class,SelectOneCombo.FIELD_VALUE,budgetCategory,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<BudgetCategory>() {
			@Override
			public Collection<BudgetCategory> computeChoices(AbstractInputChoice<BudgetCategory> input) {
				//Collection<Section> choices = __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
				//Collection<Section> choices = __inject__(SectionController.class).read();
				Collection<BudgetCategory> choices = __inject__(BudgetCategoryController.class).readVisiblesByLoggedInActorCodeForUI();
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, BudgetCategory budgetCategory) {
				super.select(input, budgetCategory);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.LABEL);		
		return select;
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_BUDGET_CATEGORY_SELECT_ONE.equals(fieldName))
			return budgetCategoryInitial;
		if(FIELD_SECTION_SELECT_ONE.equals(fieldName))
			return sectionInitial;
		if(FIELD_FUNCTION_SELECT_ONE.equals(fieldName))
			return functionInitial;
		if(FIELD_SENT_SELECT_ONE.equals(fieldName))
			return sentInitial;
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return processedInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String getInputTextInitialValue(String fieldName) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName))
			return searchInitial;
		return super.getInputTextInitialValue(fieldName);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName) || input == searchInputText)
			return RequestDispatchSlip.FIELD_SEARCH;
		if(FIELD_SENT_SELECT_ONE.equals(fieldName) || input == sentSelectOne)
			return RequestDispatchSlip.FIELD_SENT;
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName) || input == processedSelectOne)
			return RequestDispatchSlip.FIELD_PROCESSED;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		if(input == sentSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		if(input == processedSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		return super.buildParameterValue(input);
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(budgetCategorySelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetCategorySelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,budgetCategorySelectOne,Cell.FIELD_WIDTH,10));
		}
		if(sectionSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		Integer width = null;
		if(functionSelectOne != null) {
			width = 4;
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			if(sentSelectOne == null)
				width = width + 3;
			if(processedSelectOne == null)
				width = width + 3;
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,width));
		}
		if(sentSelectOne != null) {
			width = 2;
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sentSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sentSelectOne,Cell.FIELD_WIDTH,width));
		}
		if(processedSelectOne != null) {
			width = 2;
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne,Cell.FIELD_WIDTH,width));
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
		if(budgetCategoryInitial != null)
			strings.add("Section "+budgetCategoryInitial.getName());
		if(sectionInitial != null)
			strings.add("Section "+sectionInitial.toString());
		if(functionInitial != null)
			strings.add(functionInitial.getName());
		
		if(sentInitial != null)
			strings.add(sentInitial ? "Transmis" : "À transmettre");
		
		if(processedInitial != null)
			strings.add(processedInitial ? "Traité" : "À traiter");
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(RequestDispatchSlip.FIELD_CODE));
		if(sectionInitial == null)
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_SECTION_AS_STRING);
		if(functionInitial == null)
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_FUNCTION_AS_STRING);
		columnsFieldsNames.addAll(List.of(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS));
		
		if(processedInitial == null || !processedInitial) {
			
		}
		columnsFieldsNames.add(RequestDispatchSlip.FIELD_CREATION_DATE_AS_STRING);
		if(sentInitial == null || sentInitial) {
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_SENDING_DATE_AS_STRING);		
			if(processedInitial == null || processedInitial) {
				columnsFieldsNames.add(RequestDispatchSlip.FIELD_PROCESSING_DATE_AS_STRING);
			}
		}		
		columnsFieldsNames.addAll(List.of(RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_PROCESSED,RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_ACCEPTED
				,RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_REJECTED,RequestDispatchSlip.FIELD_NUMBER_OF_REQUESTS_NOT_PROCESSED));
		return columnsFieldsNames;
	}

	public BudgetCategory getBudgetCategory() {
		return (BudgetCategory) AbstractInput.getValue(budgetCategorySelectOne);
	}
	
	public Section getSection() {
		return (Section) AbstractInput.getValue(sectionSelectOne);
	}
	
	public Function getFunction() {
		return (Function) AbstractInput.getValue(functionSelectOne);
	}
	
	public String getSearch() {
		return (String)AbstractInput.getValue(searchInputText);
	}
	
	public Boolean getProcessed() {
		return (Boolean) AbstractInput.getValue(processedSelectOne);
	}
	
	public Boolean getSent() {
		return (Boolean) AbstractInput.getValue(sentSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(sentSelectOne == input)
			return RequestDispatchSlip.FIELD_SENT;
		if(processedSelectOne == input)
			return RequestDispatchSlip.FIELD_PROCESSED;
		return super.buildParameterName(input);
	}
	
	@Override
	public Map<String, List<String>> asMap() {
		Map<String, List<String>> map = new HashMap<>();
		if(budgetCategoryInitial != null)
			map.put(ParameterName.stringify(BudgetCategory.class), List.of((String)FieldHelper.readSystemIdentifier(budgetCategoryInitial)));
		if(sectionInitial != null)
			map.put(ParameterName.stringify(Section.class), List.of((String)FieldHelper.readSystemIdentifier(sectionInitial)));	
		if(functionInitial != null)
			map.put(ParameterName.stringify(Function.class), List.of((String)FieldHelper.readSystemIdentifier(functionInitial)));
		if(sentInitial != null)
			map.put(RequestDispatchSlip.FIELD_SENT, List.of(sentInitial.toString()));
		if(processedInitial != null)
			map.put(RequestDispatchSlip.FIELD_PROCESSED, List.of(processedInitial.toString()));
		if(searchInitial != null && StringHelper.isNotBlank(searchInitial))
			map.put(RequestDispatchSlip.FIELD_SEARCH, List.of(searchInitial));
		return map;
	}
	
	public static Filter.Dto populateFilter(Filter.Dto filter,RequestDispatchSlipFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_BUDGETS_CATEGORIES_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.budgetCategoryInitial : controller.getBudgetCategory())), filter);
		
		Collection<String> identifiers = Helper.getSectionsIdentifiers(initial, controller.getSectionInitial(), controller.getSectionSelectOne());
		if(CollectionHelper.isNotEmpty(identifiers))
			filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SECTIONS_IDENTIFIERS, identifiers, filter);
		//filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SECTIONS_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.sectionInitial : controller.getSection())), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.functionInitial : controller.getFunction())), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_SENT, Boolean.TRUE.equals(initial) ? controller.sentInitial : controller.getSent(), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(RequestDispatchSlipQuerier.PARAMETER_NAME_PROCESSED, Boolean.TRUE.equals(initial) ? controller.processedInitial : controller.getProcessed(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(RequestDispatchSlipQuerier.PARAMETER_NAME_SEARCH, Boolean.TRUE.equals(initial) ? controller.searchInitial : controller.getSearch(), filter);
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(RequestDispatchSlipFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	
	/**/
	
	public static final String FIELD_SEARCH_INPUT_TEXT = "searchInputText";
	public static final String FIELD_BUDGET_CATEGORY_SELECT_ONE = "budgetCategorySelectOne";
	public static final String FIELD_SECTION_SELECT_ONE = "sectionSelectOne";
	public static final String FIELD_FUNCTION_SELECT_ONE = "functionSelectOne";
	public static final String FIELD_SENT_SELECT_ONE = "sentSelectOne";
	public static final String FIELD_PROCESSED_SELECT_ONE = "processedSelectOne";
}