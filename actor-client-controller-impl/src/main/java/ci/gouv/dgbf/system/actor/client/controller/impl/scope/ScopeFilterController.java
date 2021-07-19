package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFilterController extends AbstractFilterController implements Serializable {

	private InputText codeInputText,nameInputText;
	private SelectOneCombo scopeTypeSelectOne,visibleSelectOne;
	
	private String codeInitial;
	private String nameInitial;
	private ScopeType scopeTypeInitial;
	private Boolean visibleInitial;

	public ScopeFilterController() {
		codeInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_CODE_INPUT_TEXT));
		nameInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_NAME_INPUT_TEXT));
		if(scopeTypeInitial == null) {
			scopeTypeInitial = EntityReader.getInstance().readOneBySystemIdentifierAsParent(ScopeType.class, new Arguments<ScopeType>()
				.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(ScopeType.class))));
		}
		visibleInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(Scope.FIELD_VISIBLE));
	}
	
	@Override
	protected void buildInputs() {
		buildInputText(FIELD_CODE_INPUT_TEXT);
		buildInputText(FIELD_NAME_INPUT_TEXT);
		buildInputSelectOne(FIELD_SCOPE_TYPE_SELECT_ONE, ScopeType.class);
		buildInputSelectOne(FIELD_VISIBLE_SELECT_ONE, Boolean.class);
		
		enableValueChangeListeners();
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		
	}
	
	private void selectByValueSystemIdentifier() {
		
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_VISIBLE_SELECT_ONE.equals(fieldName))
			return visibleInitial;
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return scopeTypeInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_CODE_INPUT_TEXT.equals(fieldName) || input == codeInputText)
			return Scope.FIELD_CODE;
		if(FIELD_NAME_INPUT_TEXT.equals(fieldName) || input == nameInputText)
			return Scope.FIELD_NAME;
		if(FIELD_VISIBLE_SELECT_ONE.equals(fieldName) || input == visibleSelectOne)
			return Scope.FIELD_VISIBLE;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		if(input == visibleSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		return super.buildParameterValue(input);
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_CODE_INPUT_TEXT.equals(fieldName))
			return buildCodeInputText((String) value);
		if(FIELD_NAME_INPUT_TEXT.equals(fieldName))
			return buildNameInputText((String) value);
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return buildScopeTypeSelectOne((ScopeType) value);
		if(FIELD_VISIBLE_SELECT_ONE.equals(fieldName))
			return buildVisibleSelectOne((Boolean) value);
		return null;
	}
	
	private InputText buildCodeInputText(String code) {
		InputText input = InputText.build(SelectOneCombo.FIELD_VALUE,code,InputText.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Code");	
		return input;
	}
	
	private InputText buildNameInputText(String name) {
		InputText input = InputText.build(SelectOneCombo.FIELD_VALUE,name,InputText.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Libellé");	
		return input;
	}
	
	private SelectOneCombo buildScopeTypeSelectOne(ScopeType scopeType) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,scopeType,SelectOneCombo.FIELD_CHOICE_CLASS,ScopeType.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ScopeType>() {
			@Override
			protected Collection<ScopeType> __computeChoices__(AbstractInputChoice<ScopeType> input,Class<?> entityClass) {
				Collection<ScopeType> choices = EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>()
						.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC));
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, ScopeType scopeType) {
				super.select(input, scopeType);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Domaine");
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private SelectOneCombo buildVisibleSelectOne(Boolean visible) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,visible,SelectOneCombo.ConfiguratorImpl.FIELD_CHOICES_ARE_UNKNOWN_YES_NO_ONLY,Boolean.TRUE
				,SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Visible");
		//input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(scopeTypeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne,Cell.FIELD_WIDTH,11));
		}
		
		if(visibleSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,visibleSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,visibleSelectOne,Cell.FIELD_WIDTH,11));
		}
		
		if(codeInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,codeInputText.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,codeInputText,Cell.FIELD_WIDTH,11));	
		}
		
		if(nameInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,nameInputText.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,nameInputText,Cell.FIELD_WIDTH,11));
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,12));	
		return cellsMaps;
	}
	
	public String generateWindowTitleValue(String prefix) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(scopeTypeInitial != null) {
			strings.add(scopeTypeInitial.getName());
		}
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.addAll(List.of(Scope.FIELD_CODE,Scope.FIELD_NAME,Scope.FIELD_VISIBLE_AS_STRING));
		return columnsFieldsNames;
	}
	
	public String getCode() {
		return (String)AbstractInput.getValue(codeInputText);
	}
	
	public String getName() {
		return (String)AbstractInput.getValue(nameInputText);
	}
	
	public ScopeType getScopeType() {
		return (ScopeType)AbstractInput.getValue(scopeTypeSelectOne);
	}
	
	public Boolean getVisible() {
		return (Boolean) AbstractInput.getValue(visibleSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(codeInputText == input)
			return Scope.FIELD_CODE;
		if(nameInputText == input)
			return Scope.FIELD_NAME;
		if(visibleSelectOne == input)
			return Scope.FIELD_VISIBLE;
		return super.buildParameterName(input);
	}
	
	/**/
	
	public static Filter.Dto populateFilter(Filter.Dto filter,ScopeFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(ScopeQuerier.PARAMETER_NAME_TYPE_CODE, FieldHelper.readBusinessIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeTypeInitial : controller.getScopeType()), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(ScopeQuerier.PARAMETER_NAME_VISIBLE, Boolean.TRUE.equals(initial) ? controller.visibleInitial : controller.getVisible(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(ScopeQuerier.PARAMETER_NAME_CODE, Boolean.TRUE.equals(initial) ? controller.codeInitial : controller.getCode(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(ScopeQuerier.PARAMETER_NAME_NAME, Boolean.TRUE.equals(initial) ? controller.nameInitial : controller.getName(), filter);
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(ScopeFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	public static final String FIELD_CODE_INPUT_TEXT = "codeInputText";
	public static final String FIELD_NAME_INPUT_TEXT = "nameInputText";
	public static final String FIELD_SCOPE_TYPE_SELECT_ONE = "scopeTypeSelectOne";
	public static final String FIELD_VISIBLE_SELECT_ONE = "visibleSelectOne";
}