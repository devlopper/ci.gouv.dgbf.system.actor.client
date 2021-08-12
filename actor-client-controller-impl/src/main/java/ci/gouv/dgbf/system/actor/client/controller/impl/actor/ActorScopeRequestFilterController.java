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
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeFilterController;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeRequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ActorScopeRequestFilterController extends AbstractFilterController implements Serializable {

	private AutoComplete actorSelectOne,scopeSelectOne;
	private SelectOneCombo scopeTypeSelectOne,processedSelectOne,grantedSelectOne;
	
	private Actor actorInitial;
	private ScopeType scopeTypeInitial;
	private Scope scopeInitial;
	private Boolean processedInitial;
	private Boolean grantedInitial;
	
	private String actionIdentifier;
	
	public ActorScopeRequestFilterController() {
		actorInitial = ScopeFilterController.getActorFromRequestParameter();
		scopeInitial = ScopeFilterController.getScopeFromRequestParameter();
		scopeTypeInitial = ScopeFilterController.getScopeTypeFromRequestParameter(scopeInitial);			
		processedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(ActorScopeRequest.FIELD_PROCESSED));
		grantedInitial = ValueConverter.getInstance().convertToBoolean(WebController.getInstance().getRequestParameter(ActorScopeRequest.FIELD_GRANTED));	
	}
	
	@Override
	protected void buildInputs() {
		buildInputSelectOne(FIELD_ACTOR_SELECT_ONE, Actor.class);
		buildInputSelectOne(FIELD_SCOPE_SELECT_ONE, ScopeType.class);
		buildInputSelectOne(FIELD_SCOPE_TYPE_SELECT_ONE, ScopeType.class);
		buildInputSelectOne(FIELD_PROCESSED_SELECT_ONE, Boolean.class);
		buildInputSelectOne(FIELD_GRANTED_SELECT_ONE, Boolean.class);
		
		enableValueChangeListeners();
		selectByValueSystemIdentifier();		
	}
	
	private void enableValueChangeListeners() {
		if(actorSelectOne != null)
			actorSelectOne.enableAjaxItemSelect();
		if(scopeTypeSelectOne != null)
			scopeTypeSelectOne.enableValueChangeListener(CollectionHelper.listOf(Boolean.TRUE,scopeSelectOne));
		if(scopeSelectOne != null)
			scopeSelectOne.enableAjaxItemSelect();
	}
	
	private void selectByValueSystemIdentifier() {
		
	}
	
	@Override
	protected Object getInputSelectOneInitialValue(String fieldName, Class<?> klass) {
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return processedInitial;
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return grantedInitial;
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return scopeTypeInitial;
		if(FIELD_SCOPE_SELECT_ONE.equals(fieldName))
			return scopeInitial;
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
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return Helper.buildScopeTypeSelectOneCombo((ScopeType) value,this,FIELD_SCOPE_SELECT_ONE);
		if(FIELD_SCOPE_SELECT_ONE.equals(fieldName))
			return Helper.buildScopeAutoComplete((Scope) value,this,FIELD_SCOPE_TYPE_SELECT_ONE);
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
		
		if(scopeTypeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne,Cell.FIELD_WIDTH,3));
		}
		if(scopeSelectOne != null) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeSelectOne,Cell.FIELD_WIDTH,7));
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
		if(scopeTypeInitial != null) {
			strings.add(scopeTypeInitial.getName());
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
			columnsFieldsNames.add(ActorScopeRequest.FIELD_ACTOR_STRING);
		if(scopeTypeInitial == null)
			columnsFieldsNames.add(ActorScopeRequest.FIELD_SCOPE_TYPE_AS_STRING);
		if(scopeInitial == null)
			columnsFieldsNames.add(ActorScopeRequest.FIELD_SCOPE_STRING);		
		//if(grantedInitial == null)
			columnsFieldsNames.add(ActorScopeRequest.FIELD_GRANTED_AS_STRING);
		if(ActorScopeRequestBusiness.PROCESS.equals(actionIdentifier)) {
			columnsFieldsNames.add(ActorScopeRequest.FIELD_PROCESSING_COMMENT);
		}
		return columnsFieldsNames;
	}
	
	public Actor getActor() {
		return (Actor) AbstractInput.getValue(actorSelectOne);
	}
	
	public ScopeType getScopeType() {
		return (ScopeType)AbstractInput.getValue(scopeTypeSelectOne);
	}
	
	public Scope getScope() {
		return (Scope)AbstractInput.getValue(scopeSelectOne);
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
	
	public static Filter.Dto populateFilter(Filter.Dto filter,ActorScopeRequestFilterController controller,Boolean initial) {
		filter = Filter.Dto.addFieldIfValueNotNull(ActorScopeRequestQuerier.PARAMETER_NAME_ACTORS_IDENTIFIERS
				, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.actorInitial : controller.getActor())), filter);
		
		String scopeIdentifier = (String) FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeInitial : controller.getScope());
		if(StringHelper.isBlank(scopeIdentifier)) {
			filter = Filter.Dto.addFieldIfValueNotNull(ActorScopeRequestQuerier.PARAMETER_NAME_SCOPE_TYPES_IDENTIFIERS
					, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeTypeInitial : controller.getScopeType())), filter);
		}else {
			filter = Filter.Dto.addFieldIfValueNotNull(ActorScopeRequestQuerier.PARAMETER_NAME_SCOPES_IDENTIFIERS
					, CollectionHelper.listOf(Boolean.TRUE,scopeIdentifier), filter);
		}

		Boolean processed = Boolean.TRUE.equals(initial) ? controller.processedInitial : controller.getProcessed();
		if(processed != null) {
			filter = Filter.Dto.addFieldIfValueNotNull(ActorScopeRequestQuerier.PARAMETER_NAME_PROCESSED, processed, filter);			
			filter = Filter.Dto.addFieldIfValueNotNull(ActorScopeRequestQuerier.PARAMETER_NAME_GRANTED, Boolean.TRUE.equals(initial) ? controller.grantedInitial : controller.getGranted(), filter);
		}
		
		return filter;
	}
	
	public static Filter.Dto instantiateFilter(ActorScopeRequestFilterController controller,Boolean initial) {
		return populateFilter(new Filter.Dto(), controller,initial);
	}
	
	/**/
	
	public static ActorScopeRequestFilterController instantiate(Actor actor,String scopeTypeCode) {
		ActorScopeRequestFilterController filterController = new ActorScopeRequestFilterController();
		if(actor != null)
			filterController.setActorInitial(actor)/*.ignore(ActorScopeRequestFilterController.FIELD_ACTOR_SELECT_ONE)*/;
		if(filterController.getScopeTypeInitial() == null && StringHelper.isNotBlank(scopeTypeCode))
			filterController.setScopeTypeInitial(EntityReader.getInstance().readOne(ScopeType.class, new Arguments<ScopeType>()
				.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE).filterFieldsValues(ScopeTypeQuerier.PARAMETER_NAME_CODE,scopeTypeCode)));
		return filterController;
	}
	
	/**/
	
	public static final String FIELD_ACTOR_SELECT_ONE = "actorSelectOne";
	public static final String FIELD_SCOPE_TYPE_SELECT_ONE = "scopeTypeSelectOne";
	public static final String FIELD_SCOPE_SELECT_ONE = "scopeSelectOne";
	public static final String FIELD_PROCESSED_SELECT_ONE = "processedSelectOne";
	public static final String FIELD_GRANTED_SELECT_ONE = "grantedSelectOne";
}