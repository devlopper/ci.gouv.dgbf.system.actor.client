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
import org.cyk.utility.__kernel__.object.ReadListener;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueConverter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFilterController extends AbstractFilterController implements Serializable {

	private InputText codeInputText,nameInputText;
	private SelectOneCombo scopeTypeSelectOne,visibleSelectOne;
	private AutoComplete actorSelectOne;
	
	private ScopeType scopeTypeInitial;
	private Boolean visibleInitial;
	private String codeInitial;
	private String nameInitial;
	private Actor actorInitial;
	
	public ScopeFilterController() {
		codeInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_CODE_INPUT_TEXT));
		nameInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_NAME_INPUT_TEXT));
		scopeTypeInitial = getScopeTypeFromRequestParameter(null);
		if(actorInitial == null)
			actorInitial = getActorFromRequestParameter();
		visibleInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(Scope.FIELD_VISIBLE));
	}
	
	public static Scope getScopeFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Scope.class, new Arguments<Scope>()
				.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(Scope.FIELD_IDENTIFIER,Scope.FIELD_CODE,Scope.FIELD_NAME,Scope.FIELD_TYPE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Scope.class))));
	}
	
	public static ScopeType getScopeTypeFromRequestParameter(Scope scope) {
		if(scope == null || scope.getType() == null)
			return EntityReader.getInstance().readOneBySystemIdentifierAsParent(ScopeType.class, new Arguments<ScopeType>()
				.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(ScopeType.class))));
		return scope.getType();
	}
	
	public static Actor getActorFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Actor.class, new Arguments<Actor>()
				.queryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.FIELDS_CODE_NAMES_ELECTRONIC_MAIL_ADDRESS)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Actor.class))));
	}
	
	@Override
	protected void buildInputs() {
		buildInputText(FIELD_CODE_INPUT_TEXT);
		buildInputText(FIELD_NAME_INPUT_TEXT);
		buildInputSelectOne(FIELD_SCOPE_TYPE_SELECT_ONE, ScopeType.class);
		buildInputSelectOne(FIELD_VISIBLE_SELECT_ONE, Boolean.class);
		buildInputSelectOne(FIELD_ACTOR_SELECT_ONE, Actor.class);
		
		enableValueChangeListeners();
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		actorSelectOne.enableAjaxItemSelect();
	}
	
	private void selectByValueSystemIdentifier() {
		
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_VISIBLE_SELECT_ONE.equals(fieldName))
			return visibleInitial;
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return scopeTypeInitial;
		if(FIELD_ACTOR_SELECT_ONE.equals(fieldName))
			return actorInitial;
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
		if(FIELD_ACTOR_SELECT_ONE.equals(fieldName))
			return buildActorSelectOne((Actor) value);
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
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Type");
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private SelectOneCombo buildVisibleSelectOne(Boolean visible) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,visible,SelectOneCombo.ConfiguratorImpl.FIELD_CHOICES_ARE_UNKNOWN_YES_NO_ONLY,Boolean.TRUE
				,SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Visible");
		//input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private AutoComplete buildActorSelectOne(Actor actor) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_VALUE,actor,AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER
				,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public Collection<Actor> complete(AutoComplete autoComplete) {
				Collection<Actor> choices = EntityReader.getInstance().readMany(Actor.class, new Arguments<Actor>()
						.queryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ActorQuerier.FLAG_SEARCH)
						.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.FIELDS_CODE_NAMES_ELECTRONIC_MAIL_ADDRESS)
						.filterFieldsValues(ActorQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__()));
				return choices;
			}
						
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Acteur");
		input.setReadItemLabelListener(new ReadListener() {		
			@Override
			public Object read(Object object) {
				if(object == null)
					return null;
				return ((Actor)object).getCode()+" - "+((Actor)object).getNames();
			}
		});
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
		
		if(actorSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne,Cell.FIELD_WIDTH,11));
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
		if(visibleInitial != null) {
			strings.add(visibleInitial ? "Visible" : "Non visible");
		}
		if(actorInitial != null) {
			strings.add(actorInitial.getCode()+" - "+actorInitial.getNames());
		}
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(scopeTypeInitial == null)
			columnsFieldsNames.add(Scope.FIELD_TYPE_AS_STRING);
		columnsFieldsNames.addAll(List.of(Scope.FIELD_CODE,Scope.FIELD_NAME));
		if(visibleInitial == null)
			columnsFieldsNames.add(Scope.FIELD_VISIBLE_AS_STRING);
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
	
	public Actor getActor() {
		return (Actor) AbstractInput.getValue(actorSelectOne);
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
		filter = Filter.Dto.addFieldIfValueNotNull(ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, FieldHelper.readBusinessIdentifier(Boolean.TRUE.equals(initial) ? controller.actorInitial : controller.getActor()), filter);
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
	public static final String FIELD_ACTOR_SELECT_ONE = "actorSelectOne";
}