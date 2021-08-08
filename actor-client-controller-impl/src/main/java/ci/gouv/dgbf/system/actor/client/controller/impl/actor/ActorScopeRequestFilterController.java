package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeFilterController;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeRequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
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
		actorSelectOne.enableAjaxItemSelect();
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
			return buildActorSelectOne((Actor) value);
		if(FIELD_SCOPE_TYPE_SELECT_ONE.equals(fieldName))
			return buildScopeTypeSelectOne((ScopeType) value);
		if(FIELD_SCOPE_SELECT_ONE.equals(fieldName))
			return buildScopeSelectOne((Scope) value);
		if(FIELD_PROCESSED_SELECT_ONE.equals(fieldName))
			return buildProcessedSelectOne((Boolean) value);
		if(FIELD_GRANTED_SELECT_ONE.equals(fieldName))
			return buildGrantedSelectOne((Boolean) value);
		return null;
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
	
	private SelectOneCombo buildProcessedSelectOne(Boolean granted) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,granted,SelectOneCombo.ConfiguratorImpl.FIELD_CHOICES_ARE_UNKNOWN_YES_NO_ONLY,Boolean.TRUE
				,SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Traité");
		//input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	private SelectOneCombo buildGrantedSelectOne(Boolean granted) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,granted,SelectOneCombo.ConfiguratorImpl.FIELD_CHOICES_ARE_UNKNOWN_YES_NO_ONLY,Boolean.TRUE
				,SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Accordé");
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
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorSelectOne,Cell.FIELD_WIDTH,11));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypeSelectOne,Cell.FIELD_WIDTH,3));	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeSelectOne,Cell.FIELD_WIDTH,8));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,processedSelectOne,Cell.FIELD_WIDTH,5));		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,grantedSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,1));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,grantedSelectOne,Cell.FIELD_WIDTH,5));
		
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
		if(scopeInitial == null)
			columnsFieldsNames.add(ActorScopeRequest.FIELD_SCOPE_STRING);
		//if(grantedInitial == null)
			columnsFieldsNames.add(ActorScopeRequest.FIELD_GRANTED_AS_STRING);
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
		
		filter = Filter.Dto.addFieldIfValueNotNull(ActorScopeRequestQuerier.PARAMETER_NAME_SCOPES_IDENTIFIERS
				, CollectionHelper.listOf(Boolean.TRUE,FieldHelper.readSystemIdentifier(Boolean.TRUE.equals(initial) ? controller.scopeInitial : controller.getScope())), filter);
		
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
	
	public static final String FIELD_ACTOR_SELECT_ONE = "actorSelectOne";
	public static final String FIELD_SCOPE_TYPE_SELECT_ONE = "scopeTypeSelectOne";
	public static final String FIELD_SCOPE_SELECT_ONE = "scopeSelectOne";
	public static final String FIELD_PROCESSED_SELECT_ONE = "processedSelectOne";
	public static final String FIELD_GRANTED_SELECT_ONE = "grantedSelectOne";
}