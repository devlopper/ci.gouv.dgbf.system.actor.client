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
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActorFilterController extends AbstractFilterController implements Serializable {

	private InputText searchInputText;
	private SelectOneCombo profileSelectOne,scopeTypeSelectOne,visibleSelectOne;
	private AutoComplete scopeSelectOne;
	
	private ScopeType scopeTypeInitial;
	private Boolean visibleInitial;
	private String searchInitial;
	private Scope scopeInitial;
	private Profile profileInitial;
	
	public ActorFilterController() {
		searchInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_SEARCH_INPUT_TEXT));
		if(profileInitial == null) {
			profileInitial = EntityReader.getInstance().readOneBySystemIdentifierAsParent(Profile.class, new Arguments<Profile>()
				.queryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Profile.class))));
		}
		
		if(scopeTypeInitial == null) {
			scopeTypeInitial = EntityReader.getInstance().readOneBySystemIdentifierAsParent(ScopeType.class, new Arguments<ScopeType>()
				.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(ScopeType.class))));
		}
		
		if(scopeInitial == null) {
			scopeInitial = EntityReader.getInstance().readOneBySystemIdentifierAsParent(Scope.class, new Arguments<Scope>()
				.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Scope.class))));
		}
		
		visibleInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(Scope.FIELD_VISIBLE));
	}
	
	@Override
	protected void buildInputs() {
		buildInputText(FIELD_SEARCH_INPUT_TEXT);
		buildInputSelectOne(FIELD_SCOPE_TYPE_SELECT_ONE, ScopeType.class);
		buildInputSelectOne(FIELD_VISIBLE_SELECT_ONE, Boolean.class);
		buildInputSelectOne(FIELD_PROFILE_SELECT_ONE, Profile.class);
		buildInputSelectOne(FIELD_SCOPE_SELECT_ONE, Scope.class);
		
		enableValueChangeListeners();
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		profileSelectOne.enableValueChangeListener(List.of());
		scopeTypeSelectOne.enableValueChangeListener(List.of());
		scopeSelectOne.enableAjaxItemSelect();
	}
	
	private void selectByValueSystemIdentifier() {
		
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_VISIBLE_SELECT_ONE.equals(fieldName))
			return visibleInitial;
		if(FIELD_PROFILE_SELECT_ONE.equals(fieldName))
			return profileInitial;
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return scopeTypeInitial;
		if(FIELD_SCOPE_SELECT_ONE.equals(fieldName))
			return scopeInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName) || input == searchInputText)
			return Scope.FIELD_SEARCH;
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
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName))
			return buildSearchInputText((String) value);
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return buildScopeTypeSelectOne((ScopeType) value);
		if(FIELD_VISIBLE_SELECT_ONE.equals(fieldName))
			return buildVisibleSelectOne((Boolean) value);
		if(FIELD_SCOPE_SELECT_ONE.equals(fieldName))
			return buildScopeSelectOne((Scope) value);
		if(FIELD_PROFILE_SELECT_ONE.equals(fieldName))
			return buildProfileSelectOne((Profile) value);
		return null;
	}
	
	private InputText buildSearchInputText(String search) {
		InputText input = InputText.build(SelectOneCombo.FIELD_VALUE,search,InputText.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Recherche");	
		return input;
	}
	
	private SelectOneCombo buildProfileSelectOne(Profile profile) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,profile,SelectOneCombo.FIELD_CHOICE_CLASS,Profile.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Profile>() {
			@Override
			protected Collection<Profile> __computeChoices__(AbstractInputChoice<Profile> input,Class<?> entityClass) {
				Collection<Profile> choices = EntityReader.getInstance().readMany(Profile.class, new Arguments<Profile>()
						.queryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC));
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Profile profile) {
				super.select(input, profile);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Profile");
		input.setValueAsFirstChoiceIfNull();
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
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Type de domaine");
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private SelectOneCombo buildVisibleSelectOne(Boolean visible) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,visible,SelectOneCombo.ConfiguratorImpl.FIELD_CHOICES_ARE_UNKNOWN_YES_NO_ONLY,Boolean.TRUE
				,SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Visible");
		//input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private AutoComplete buildScopeSelectOne(Scope scope) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_VALUE,scope,AutoComplete.FIELD_ENTITY_CLASS,Scope.class,AutoComplete.FIELD_LISTENER
				,new AutoComplete.Listener.AbstractImpl<Scope>() {
			@Override
			public Collection<Scope> complete(AutoComplete autoComplete) {
				Arguments<Scope> arguments = new Arguments<Scope>()
						.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ScopeQuerier.FLAG_SEARCH)
						.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__());
				if(AbstractInput.getValue(scopeTypeSelectOne) != null) {
					arguments.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,FieldHelper.readSystemIdentifier(scopeTypeSelectOne.getValue()));
				}
				Collection<Scope> choices = EntityReader.getInstance().readMany(Scope.class, arguments);
				return choices;
			}
						
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Domaine");
		return input;
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(profileSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileSelectOne,Cell.FIELD_WIDTH,11));
		}
		
		if(scopeTypeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne,Cell.FIELD_WIDTH,11));
		}
		
		if(scopeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeSelectOne,Cell.FIELD_WIDTH,11));
		}
		/*
		if(visibleSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,visibleSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,visibleSelectOne,Cell.FIELD_WIDTH,11));
		}
		*/
		if(searchInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText.getOutputLabel(),Cell.FIELD_WIDTH,1));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText,Cell.FIELD_WIDTH,11));	
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,12));	
		return cellsMaps;
	}
	
	public String generateWindowTitleValue(String prefix) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(profileInitial != null) {
			strings.add(profileInitial.toString());
		}
		if(scopeTypeInitial != null) {
			strings.add(scopeTypeInitial.getName());
		}
		if(scopeInitial != null) {
			strings.add(scopeInitial.toString());
		}
		/*
		if(visibleInitial != null) {
			strings.add(visibleInitial ? "Visible" : "Non visible");
		}
		*/
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		//if(scopeTypeInitial == null)
		//	columnsFieldsNames.add(Actor.FIELD_CODE);
		columnsFieldsNames.addAll(List.of(Actor.FIELD_CODE,Actor.FIELD_FIRST_NAME,Actor.FIELD_LAST_NAMES));
		//if(visibleInitial == null)
		//	columnsFieldsNames.add(Scope.FIELD_VISIBLE_AS_STRING);
		return columnsFieldsNames;
	}
	
	public String getSearch() {
		return (String)AbstractInput.getValue(searchInputText);
	}
	
	public ScopeType getScopeType() {
		return (ScopeType)AbstractInput.getValue(scopeTypeSelectOne);
	}
	
	public Boolean getVisible() {
		return (Boolean) AbstractInput.getValue(visibleSelectOne);
	}
	
	public Scope getScope() {
		return (Scope) AbstractInput.getValue(scopeSelectOne);
	}
	
	public Profile getProfile() {
		return (Profile) AbstractInput.getValue(profileSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(searchInputText == input)
			return Scope.FIELD_SEARCH;
		if(visibleSelectOne == input)
			return Scope.FIELD_VISIBLE;
		return super.buildParameterName(input);
	}
	
	/**/
	
	public static Filter.Dto populateFilter(Filter.Dto filter,ActorFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(ActorQuerier.PARAMETER_NAME_PROFILE_IDENTIFIER, FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.profileInitial : controller.getProfile()), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(ActorQuerier.PARAMETER_NAME_VISIBLE_SCOPE_TYPE_CODE, FieldHelper.readBusinessIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeTypeInitial : controller.getScopeType()), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(ActorQuerier.PARAMETER_NAME_VISIBLE_SCOPE_IDENTIFIER, FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeInitial : controller.getScope()), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(ActorQuerier.PARAMETER_NAME_SEARCH, Boolean.TRUE.equals(initial) ? controller.searchInitial : controller.getSearch(), filter);
		
		//filter = Filter.Dto.addFieldIfValueNotNull(ActorQuerier.PARAMETER_NAME_PROFILE_IDENTIFIER, FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeTypeInitial : controller.getScopeType()), filter);
		/*filter = Filter.Dto.addFieldIfValueNotNull(ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, FieldHelper.readBusinessIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeInitial : controller.getActor()), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(ScopeQuerier.PARAMETER_NAME_VISIBLE, Boolean.TRUE.equals(initial) ? controller.visibleInitial : controller.getVisible(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(ScopeQuerier.PARAMETER_NAME_CODE, Boolean.TRUE.equals(initial) ? controller.codeInitial : controller.getCode(), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(ScopeQuerier.PARAMETER_NAME_NAME, Boolean.TRUE.equals(initial) ? controller.nameInitial : controller.getName(), filter);
		*/
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(ActorFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	public static final String FIELD_SEARCH_INPUT_TEXT = "searchInputText";
	public static final String FIELD_SCOPE_TYPE_SELECT_ONE = "scopeTypeSelectOne";
	public static final String FIELD_VISIBLE_SELECT_ONE = "visibleSelectOne";
	public static final String FIELD_SCOPE_SELECT_ONE = "scopeSelectOne";
	public static final String FIELD_PROFILE_SELECT_ONE = "profileSelectOne";
}