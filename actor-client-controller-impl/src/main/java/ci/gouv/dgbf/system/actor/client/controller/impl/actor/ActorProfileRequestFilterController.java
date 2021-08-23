package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueConverter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfileRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ProfileFilterController;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorProfileRequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActorProfileRequestFilterController extends AbstractFilterController implements Serializable {

	private AutoComplete actorSelectOne,profileSelectOne;
	private SelectOneCombo processedSelectOne,grantedSelectOne;
	
	private Actor actorInitial;
	private Profile profileInitial;
	private Boolean processedInitial;
	private Boolean grantedInitial;
	
	private Boolean scopeTypeRequestable;
	private String actionIdentifier;
	
	public ActorProfileRequestFilterController() {
		actorInitial = ProfileFilterController.getActorFromRequestParameter();
		profileInitial = ProfileFilterController.getProfileFromRequestParameter();
		processedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(ActorScopeRequest.FIELD_PROCESSED));
		grantedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(ActorScopeRequest.FIELD_GRANTED));	
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_ACTOR_SELECT_ONE, Actor.class);
		buildInputSelectOne(FIELD_PROFILE_SELECT_ONE, Profile.class);
		buildInputSelectOne(FIELD_PROCESSED_SELECT_ONE, Boolean.class);
		buildInputSelectOne(FIELD_GRANTED_SELECT_ONE, Boolean.class);
		
		enableValueChangeListeners();
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		if(actorSelectOne != null)
			actorSelectOne.enableAjaxItemSelect();
		if(profileSelectOne != null)
			profileSelectOne.enableAjaxItemSelect();
	}
	
	private void selectByValueSystemIdentifier() {
		
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return processedInitial;
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return grantedInitial;
		if(FIELD_PROFILE_SELECT_ONE.equals(fieldName))
			return profileInitial;
		if(FIELD_ACTOR_SELECT_ONE.equals(fieldName))
			return actorInitial;
		return super.getInputSelectOneInitialValue(fieldName, klass);
	}
	
	@Override
	protected String buildParameterName(String fieldName, AbstractInput<?> input) {
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName) || input == grantedSelectOne)
			return ActorScopeRequest.FIELD_GRANTED;
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName) || input == processedSelectOne)
			return ActorScopeRequest.FIELD_PROCESSED;
		return super.buildParameterName(fieldName, input);
	}
	
	@Override
	protected String buildParameterValue(AbstractInput<?> input) {
		if(input == grantedSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		if(input == processedSelectOne)
			return input.getValue() == null ? null : input.getValue().toString();
		return super.buildParameterValue(input);
	}
	
	@Override
	protected AbstractInput<?> buildInput(String fieldName, Object value) {
		if(FIELD_ACTOR_SELECT_ONE.equals(fieldName))
			return Helper.buildActorAutoComplete((Actor) value);
		if(FIELD_PROFILE_SELECT_ONE.equals(fieldName))
			return Helper.buildProfileAutoComplete((Profile) value,Boolean.TRUE);
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return Helper.buildProcessedSelectOneCombo((Boolean) value);
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return Helper.buildGrantedSelectOneCombo((Boolean) value);
		return null;
	}
	
	@Override
	protected Collection<Map<Object, Object>> buildLayoutCells() {
		Collection<Map<Object, Object>> cellsMaps = new ArrayList<>();
		if(actorSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(profileSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileSelectOne,Cell.FIELD_WIDTH,10));
		}
		
		if(processedSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne,Cell.FIELD_WIDTH,4));		
		}
		if(grantedSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,grantedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,grantedSelectOne,Cell.FIELD_WIDTH,4));
		}
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,12));	
		return cellsMaps;
	}
	
	public String generateWindowTitleValue(String prefix) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(actorInitial != null) {
			strings.add(actorInitial.getCode()+" - "+actorInitial.getNames());
		}
		if(profileInitial != null) {
			strings.add(profileInitial.getName());
		}
		if(processedInitial != null && grantedInitial == null) {
			strings.add(processedInitial ? "Traité" : "Non traité");
		}	
		if(grantedInitial != null) {
			strings.add(grantedInitial ? "Accordé" : "Non accordé");
		}		
		return StringHelper.concatenate(strings, " | ");
	}
	
	public Collection<String> generateColumnsNames() {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		if(actorInitial == null)
			columnsFieldsNames.add(ActorProfileRequest.FIELD_ACTOR_STRING);
		if(profileInitial == null)
			columnsFieldsNames.add(ActorProfileRequest.FIELD_PROFILE_STRING);		
		//if(grantedInitial == null)
			columnsFieldsNames.add(ActorProfileRequest.FIELD_GRANTED_AS_STRING);
		if(ActorProfileRequestBusiness.PROCESS.equals(actionIdentifier)) {
			columnsFieldsNames.add(ActorProfileRequest.FIELD_PROCESSING_COMMENT);
		}
		return columnsFieldsNames;
	}
	
	public Actor getActor() {
		return (Actor) AbstractInput.getValue(actorSelectOne);
	}
	
	public Profile getProfile() {
		return (Profile)AbstractInput.getValue(profileSelectOne);
	}
	
	public Boolean getProcessed() {
		return (Boolean) AbstractInput.getValue(processedSelectOne);
	}
	
	public Boolean getGranted() {
		return (Boolean) AbstractInput.getValue(grantedSelectOne);
	}
	
	@Override
	protected String buildParameterName(AbstractInput<?> input) {
		if(processedSelectOne == input)
			return ActorScopeRequest.FIELD_PROCESSED;
		if(grantedSelectOne == input)
			return ActorScopeRequest.FIELD_GRANTED;
		return super.buildParameterName(input);
	}
	
	/**/
	
	public static Filter.Dto populateFilter(Filter.Dto filter,ActorProfileRequestFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(ActorProfileRequestQuerier.PARAMETER_NAME_ACTORS_IDENTIFIERS
				, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(controller.isIgnored(FIELD_ACTOR_SELECT_ONE) || Boolean.TRUE.equals(initial) ? controller.actorInitial : controller.getActor())), filter);
		
		String profileIdentifier = (String) FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.profileInitial : controller.getProfile());
		if(StringHelper.isBlank(profileIdentifier)) {
			
		}else {
			filter = Filter.Dto.addFieldIfValueNotNull(ActorProfileRequestQuerier.PARAMETER_NAME_PROFILES_IDENTIFIERS
					, CollectionHelper.listOf(Boolean.TRUE,profileIdentifier), filter);
		}

		Boolean processed = Boolean.TRUE.equals(initial) ? controller.processedInitial : controller.getProcessed();
		if(processed != null) {
			filter = Filter.Dto.addFieldIfValueNotNull(ActorProfileRequestQuerier.PARAMETER_NAME_PROCESSED, processed, filter);			
			filter = Filter.Dto.addFieldIfValueNotNull(ActorProfileRequestQuerier.PARAMETER_NAME_GRANTED, Boolean.TRUE.equals(initial) ? controller.grantedInitial : controller.getGranted(), filter);
		}
		
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(ActorProfileRequestFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	public static ActorProfileRequestFilterController instantiate(Actor actor,String scopeTypeCode) {
		ActorProfileRequestFilterController filterController = new ActorProfileRequestFilterController();
		if(actor != null)
			filterController.setActorInitial(actor);
		return filterController;
	}
	
	/**/
	
	public static final String FIELD_ACTOR_SELECT_ONE = "actorSelectOne";
	public static final String FIELD_PROFILE_SELECT_ONE = "profileSelectOne";
	public static final String FIELD_PROCESSED_SELECT_ONE = "processedSelectOne";
	public static final String FIELD_GRANTED_SELECT_ONE = "grantedSelectOne";
}