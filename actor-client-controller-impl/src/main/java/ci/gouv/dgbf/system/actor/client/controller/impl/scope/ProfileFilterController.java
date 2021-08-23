package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
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

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ProfileFilterController extends AbstractFilterController implements Serializable {

	private InputText searchInputText;
	private SelectOneCombo profileTypeSelectOne;
	private AutoComplete actorSelectOne;
	
	private ProfileType profileTypeInitial;
	private Boolean visibleInitial;
	private String searchInitial;
	private Actor actorInitial;
	
	public ProfileFilterController() {
		searchInitial = WebController.getInstance().getRequestParameter(buildParameterName(FIELD_SEARCH_INPUT_TEXT));
		profileTypeInitial = getProfileTypeFromRequestParameter(null);
		if(actorInitial == null)
			actorInitial = getActorFromRequestParameter();
	}
	
	public static Profile getProfileFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Profile.class, new Arguments<Profile>()
				.queryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.projections(Profile.FIELD_IDENTIFIER,Profile.FIELD_CODE,Profile.FIELD_NAME,Profile.FIELD_TYPE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Profile.class))));
	}
	
	public static ProfileType getProfileTypeFromRequestParameter(Profile profile) {
		if(profile == null || profile.getType() == null)
			return EntityReader.getInstance().readOneBySystemIdentifierAsParent(ProfileType.class, new Arguments<ProfileType>()
				.queryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(ProfileType.class))));
		return profile.getType();
	}
	
	public static Actor getActorFromRequestParameter() {
		return EntityReader.getInstance().readOneBySystemIdentifierAsParent(Actor.class, new Arguments<Actor>()
				.queryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.FIELDS_CODE_NAMES_ELECTRONIC_MAIL_ADDRESS)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.stringify(Actor.class))));
	}
	
	@Override
	protected void buildInputs() {
		buildInputText(FIELD_SEARCH_INPUT_TEXT);
		buildInputSelectOne(FIELD_PROFILE_TYPE_SELECT_ONE, ProfileType.class);
		buildInputSelectOne(FIELD_ACTOR_SELECT_ONE, Actor.class);
		
		enableValueChangeListeners();
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		if(actorSelectOne != null)
			actorSelectOne.enableAjaxItemSelect();
	}
	
	private void selectByValueSystemIdentifier() {
		
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_PROFILE_TYPE_SELECT_ONE.equals(fieldName))
			return profileTypeInitial;
		if(FIELD_ACTOR_SELECT_ONE.equals(fieldName))
			return actorInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName) || input == searchInputText)
			return Profile.FIELD_SEARCH;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		return super.buildParameterValue(input);
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_SEARCH_INPUT_TEXT.equals(fieldName))
			return Helper.buildSearchInputText((String) value);
		if(FIELD_PROFILE_TYPE_SELECT_ONE.equals(fieldName))
			return Helper.buildProfileTypeSelectOneCombo((ProfileType) value,null,null);
		if(FIELD_ACTOR_SELECT_ONE.equals(fieldName))
			return Helper.buildActorAutoComplete((Actor) value);
		return null;
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(profileTypeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileTypeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileTypeSelectOne,Cell.FIELD_WIDTH,10));
		}		
		
		if(actorSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(searchInputText != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,searchInputText,Cell.FIELD_WIDTH,10));	
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,12));	
		return cellsMaps;
	}
	
	public String generateWindowTitleValue(String prefix,Boolean isForLoggedInUser) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(Boolean.TRUE.equals(isForLoggedInUser)) {
			addGenerateWindowTitleValueActorInitial(strings);
			addGenerateWindowTitleValueProfileTypeInitial(strings);
		}else {
			addGenerateWindowTitleValueProfileTypeInitial(strings);
			addGenerateWindowTitleValueActorInitial(strings);
		}
		return StringHelper.concatenate(strings, " | ");
	}
	
	private void addGenerateWindowTitleValueProfileTypeInitial(Collection<String> strings) {
		if(profileTypeInitial == null)
			return;
		strings.add(profileTypeInitial.getName()+(visibleInitial == null ? ConstantEmpty.STRING : " "+(visibleInitial ? "Visible" : "Non visible")));
	}
	
	private void addGenerateWindowTitleValueActorInitial(Collection<String> strings) {
		if(actorInitial == null)
			return;		
		strings.add(actorInitial.getCode()+" - "+actorInitial.getNames());		
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(profileTypeInitial == null && (isUsedForLoggedUser == null || !isUsedForLoggedUser))
			columnsFieldsNames.add(Profile.FIELD_TYPE_AS_STRING);
		columnsFieldsNames.addAll(List.of(Profile.FIELD_CODE,Profile.FIELD_NAME));
		if(isUsedForLoggedUser == null || !isUsedForLoggedUser)
			columnsFieldsNames.add(Profile.FIELD_NUMBER_OF_ACTORS);
		return columnsFieldsNames;
	}
	
	public String getSearch() {
		return (String)AbstractInput.getValue(searchInputText);
	}
	
	public ProfileType getProfileType() {
		return (ProfileType)AbstractInput.getValue(profileTypeSelectOne);
	}
	
	public Actor getActor() {
		return (Actor) AbstractInput.getValue(actorSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(searchInputText == input)
			return Profile.FIELD_SEARCH;
		return super.buildParameterName(input);
	}
	
	@Override
	public Map<String, List<String>> asMap() {
		Map<String, List<String>> map = new HashMap<>();
		if(profileTypeInitial != null)
			map.put(ParameterName.stringify(ProfileType.class), List.of((String)FieldHelper.readSystemIdentifier(profileTypeInitial)));	
		if(actorInitial != null)
			map.put(ParameterName.stringify(Actor.class), List.of((String)FieldHelper.readSystemIdentifier(actorInitial)));		
		return map;
	}
	
	/**/
	
	public static Filter.Dto populateFilter(Filter.Dto filter,ProfileFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(ProfileQuerier.PARAMETER_NAME_TYPE_IDENTIFIER, FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.profileTypeInitial : controller.getProfileType()), filter);
		filter = Filter.Dto.addFieldIfValueNotNull(ProfileQuerier.PARAMETER_NAME_ACTOR_IDENTIFIER, FieldHelper.readSystemIdentifier(controller.isIgnored(FIELD_ACTOR_SELECT_ONE) || Boolean.TRUE.equals(initial) ? controller.actorInitial : controller.getActor()), filter);
		filter = Filter.Dto.addFieldIfValueNotBlank(ProfileQuerier.PARAMETER_NAME_SEARCH, Boolean.TRUE.equals(initial) ? controller.searchInitial : controller.getSearch(), filter);
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(ProfileFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	public static ProfileFilterController instantiate(Actor actor,String profileTypeCode) {
		ProfileFilterController filterController = new ProfileFilterController();
		if(actor != null)
			filterController.setActorInitial(actor);
		if(filterController.getProfileTypeInitial() == null && StringHelper.isNotBlank(profileTypeCode))
			filterController.setProfileTypeInitial(EntityReader.getInstance().readOne(ProfileType.class, new Arguments<ProfileType>()
				.queryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE).filterFieldsValues(ProfileTypeQuerier.PARAMETER_NAME_CODE,profileTypeCode)));
		return filterController;
	}
	
	/**/
	
	public static final String FIELD_SEARCH_INPUT_TEXT = "searchInputText";
	public static final String FIELD_PROFILE_TYPE_SELECT_ONE = "profileTypeSelectOne";
	public static final String FIELD_ACTOR_SELECT_ONE = "actorSelectOne";
}