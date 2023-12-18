package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.DependencyInjection;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.ReadListener;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.api.AdministrativeUnitController;
import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.api.ProfileController;
import ci.gouv.dgbf.system.actor.client.controller.api.ProfileTypeController;
import ci.gouv.dgbf.system.actor.client.controller.api.RequestStatusController;
import ci.gouv.dgbf.system.actor.client.controller.api.RequestTypeController;
import ci.gouv.dgbf.system.actor.client.controller.api.ScopeController;
import ci.gouv.dgbf.system.actor.client.controller.api.ScopeTypeController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.FunctionType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.entities.Service;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;

public interface Helper {
	
	static InputText buildSearchInputText(String string,String label) {
		InputText input = InputText.build(SelectOneCombo.FIELD_VALUE,string,InputText.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,label);
		return input;
	}
	
	static InputText buildSearchInputText(String string) {
		return buildSearchInputText(string, "Recherche");
	}
	
	static InputText buildElectronicMailAddressInputText(String string,String label) {
		InputText input = InputText.build(SelectOneCombo.FIELD_VALUE,string,InputText.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,label);
		return input;
	}
	
	static InputText buildElectronicMailAddressInputText(String string) {
		return buildElectronicMailAddressInputText(string, "Email");
	}
	
	static AutoComplete buildActorAutoComplete(Actor actor) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_VALUE,actor,AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER
				,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public Collection<Actor> complete(AutoComplete autoComplete) {
				return __inject__(ActorController.class).search(autoComplete.get__queryString__());
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.LABEL);
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
	
	static SelectOneCombo buildScopeTypeSelectOneCombo(ScopeType scopeType,Boolean scopeTypeRequestable,Object container,String scopeSelectOneFieldName) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,scopeType,SelectOneCombo.FIELD_CHOICE_CLASS,ScopeType.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ScopeType>() {
			@Override
			protected Collection<ScopeType> __computeChoices__(AbstractInputChoice<ScopeType> input,Class<?> entityClass) {
				Collection<ScopeType> choices = Boolean.TRUE.equals(scopeTypeRequestable) ? __inject__(ScopeTypeController.class).readRequestable()
						: __inject__(ScopeTypeController.class).read();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, ScopeType scopeType) {
				super.select(input, scopeType);
				if(container != null && StringHelper.isNotBlank(scopeSelectOneFieldName))
					((AbstractInput<?>)FieldHelper.read(container, scopeSelectOneFieldName)).setValue(null);
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.LABEL);
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	static AutoComplete buildScopeAutoComplete(Scope scope,Object container,String scopeTypeSelectOneFieldName) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_VALUE,scope,AutoComplete.FIELD_ENTITY_CLASS,Scope.class,AutoComplete.FIELD_LISTENER
				,new AutoComplete.Listener.AbstractImpl<Scope>() {
			@Override
			public Collection<Scope> complete(AutoComplete autoComplete) {
				if(container == null || StringHelper.isBlank(scopeTypeSelectOneFieldName))
					return null;
				Object scopeType = AbstractInput.getValue((AbstractInput<?>) FieldHelper.read(container, scopeTypeSelectOneFieldName));
				if(scopeType == null)
					return null;
				Arguments<Scope> arguments = new Arguments<Scope>()
					.queryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ScopeQuerier.FLAG_SEARCH)
					.filterFieldsValues(ScopeQuerier.PARAMETER_NAME_SEARCH,autoComplete.get__queryString__()
							,ScopeQuerier.PARAMETER_NAME_TYPE_IDENTIFIER,FieldHelper.readSystemIdentifier(scopeType));
				return EntityReader.getInstance().readMany(Scope.class, arguments);
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.LABEL);
		return input;
	}
	
	static SelectOneCombo buildProfileTypeSelectOneCombo(ProfileType profileType,Object container,String profileSelectOneFieldName) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,profileType,SelectOneCombo.FIELD_CHOICE_CLASS,ProfileType.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<ProfileType>() {
			@Override
			protected Collection<ProfileType> __computeChoices__(AbstractInputChoice<ProfileType> input,Class<?> entityClass) {
				Collection<ProfileType> choices =  __inject__(ProfileTypeController.class).read();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, ProfileType profileType) {
				super.select(input, profileType);
				if(container != null && StringHelper.isNotBlank(profileSelectOneFieldName))
					((AbstractInput<?>)FieldHelper.read(container, profileSelectOneFieldName)).setValue(null);
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.ProfileType.LABEL);
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	static AutoComplete buildProfileAutoComplete(Profile profile,Boolean profileRequestable) {
		AutoComplete input = AutoComplete.build(AutoComplete.FIELD_VALUE,profile,AutoComplete.FIELD_ENTITY_CLASS,Profile.class,AutoComplete.FIELD_LISTENER
				,new AutoComplete.Listener.AbstractImpl<Profile>() {
			@Override
			public Collection<Profile> complete(AutoComplete autoComplete) {
				Collection<Profile> choices = Boolean.TRUE.equals(profileRequestable) ? __inject__(ProfileController.class).readRequestable(null)
						: __inject__(ProfileController.class).read();
				return choices;
			}
		},AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.LABEL);
		return input;
	}
	
	static SelectOneCombo buildSentSelectOneCombo(Boolean sent,Object container,String childSelectOneFieldName) {
		return SelectOneCombo.buildUnknownYesNoOnly((Boolean) sent, "Transmis",new SelectOneCombo.Listener.AbstractImpl<Boolean>() {
			@Override
			public void select(AbstractInputChoiceOne input, Boolean value) {
				super.select(input, value);
				if(container != null && StringHelper.isNotBlank(childSelectOneFieldName)) {
					AbstractInputChoiceOne childInput = (AbstractInputChoiceOne)FieldHelper.read(container, childSelectOneFieldName);
					if(childInput != null) {
						childInput.setValue(null);
						childInput.updateChoices();
					}
				}
			}
		});
	}
	
	static SelectOneCombo buildProcessedSelectOneCombo(Boolean processed,Object container,String childSelectOneFieldName) {
		return SelectOneCombo.buildUnknownYesNoOnly((Boolean) processed, "Traité",new SelectOneCombo.Listener.AbstractImpl<Boolean>() {
			@Override
			public void select(AbstractInputChoiceOne input, Boolean value) {
				super.select(input, value);
				if(container != null && StringHelper.isNotBlank(childSelectOneFieldName)) {
					AbstractInputChoiceOne childInput = (AbstractInputChoiceOne)FieldHelper.read(container, childSelectOneFieldName);
					if(childInput != null) {
						childInput.setValue(null);
						childInput.updateChoices();
					}
				}
			}
		});
	}
	
	static SelectOneCombo buildProcessedSelectOneCombo(Boolean processed) {
		return buildProcessedSelectOneCombo(processed, null, null);
	}
	
	static SelectOneCombo buildGrantedSelectOneCombo(Boolean granted) {
		return SelectOneCombo.buildUnknownYesNoOnly((Boolean) granted, "Accordé");
	}
	
	static SelectOneCombo buildAcceptedSelectOneCombo(Boolean accepted) {
		return SelectOneCombo.buildUnknownYesNoOnly((Boolean) accepted, "Accepté");
	}
	
	static SelectOneCombo buildVisibleSelectOneCombo(Boolean visible) {
		return SelectOneCombo.buildUnknownYesNoOnly((Boolean) visible, "Visible");
	}
	
	static SelectOneCombo buildRequestTypeSelectOneCombo(RequestType requestType) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,requestType,SelectOneCombo.FIELD_CHOICE_CLASS,RequestType.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<RequestType>() {
			@Override
			protected Collection<RequestType> __computeChoices__(AbstractInputChoice<RequestType> input,Class<?> entityClass) {
				Collection<RequestType> choices =  __inject__(RequestTypeController.class).read();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestType.LABEL);
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	static SelectOneCombo buildRequestStatusSelectOneCombo(RequestStatus requestStatus) {
		SelectOneCombo input = SelectOneCombo.build(SelectOneCombo.FIELD_VALUE,requestStatus,SelectOneCombo.FIELD_CHOICE_CLASS,RequestStatus.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<RequestStatus>() {
			@Override
			protected Collection<RequestStatus> __computeChoices__(AbstractInputChoice<RequestStatus> input,Class<?> entityClass) {
				Collection<RequestStatus> choices =  __inject__(RequestStatusController.class).read();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.RequestStatus.LABEL);
		input.setValueAsFirstChoiceIfNull();
		return input;
	}
	
	public static SelectOneCombo buildSectionSelectOne(Section section,Object container,Collection<String> updatablesSelectOneFieldsNames) {
		SelectOneCombo select = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_VALUE,section,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Section>() {
			@Override
			public Collection<Section> computeChoices(AbstractInputChoice<Section> input) {
				//Collection<Section> choices = __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
				//Collection<Section> choices = __inject__(SectionController.class).read();
				//Collection<Scope> scopes = __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
				//if(CollectionHelper.isEmpty(scopes))
				//	return null;
				Collection<Section> choices = new ArrayList<>(__inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI());
				/*for(Scope scope : scopes) {
					Section section = new Section();
					section.setIdentifier(scope.getIdentifier());
					section.setCode(scope.getCode());
					section.setName(scope.getName());
					choices.add(section);
				}
				*/
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Section section) {
				super.select(input, section);
				if(container != null && CollectionHelper.isNotEmpty(updatablesSelectOneFieldsNames)) {
					for(String fieldName : updatablesSelectOneFieldsNames) {
						AbstractInputChoiceOne selectOne = (AbstractInputChoiceOne)FieldHelper.read(container, fieldName);
						if(selectOne != null) {
							selectOne.setValue(null);
							selectOne.updateChoices();
						}
					}					
				}
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Section.LABEL);		
		return select;
	}
	
	static Collection<String> getSectionsIdentifiers(Boolean initial,Object objectInitial,SelectOneCombo  selectOne) {
		Object object = Boolean.TRUE.equals(initial) ? objectInitial : (Section)selectOne.getValue();
		if(object == null)
			return DependencyInjection.inject(ScopeController.class).getLoggedInVisibleSectionsIdentifiers();
		else
			return List.of((String)FieldHelper.readSystemIdentifier(object));
	}
	
	public static SelectOneCombo buildAdministrativeUnitSelectOne(AdministrativeUnit administrativeUnit,Object container,String sectionSelectFieldName) {
		SelectOneCombo select = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,AdministrativeUnit.class,SelectOneCombo.FIELD_VALUE,administrativeUnit
				,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<AdministrativeUnit>() {
			@Override
			public Collection<AdministrativeUnit> computeChoices(AbstractInputChoice<AdministrativeUnit> input) {
				Collection<AdministrativeUnit> choices = null;
				if(container == null || StringHelper.isBlank(sectionSelectFieldName))
					choices =  null;//__inject__(AdministrativeUnitController.class).readVisiblesByLoggedInActorCodeForUI();
				else {
					AbstractInputChoiceOne sectionSelect = (AbstractInputChoiceOne)FieldHelper.read(container, sectionSelectFieldName);
					if(sectionSelect != null) {
						Section section =  (Section) sectionSelect.getValue();
						if(section == null)
							choices =  null;//__inject__(AdministrativeUnitController.class).readVisiblesByLoggedInActorCodeForUI();
						else
							//choices = __inject__(AdministrativeUnitController.class).readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(section.getIdentifier());
							choices = new ArrayList<>(__inject__(AdministrativeUnitController.class).readVisiblesBySectionIdentifierByLoggedInActorCodeForUI(section.getIdentifier()));
					}
				}
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.AdministrativeUnit.LABEL);		
		return select;
	}
	
	static String formatTitleRequestDispatchSlip(RequestDispatchSlip requestDispatchSlip,Action action) {
		if(requestDispatchSlip == null)
			return null;
		if(Action.CREATE.equals(action))
			return "Nouveau bordereau de demandes";
		if(Action.READ.equals(action))
			return String.format("Bordereau de demandes N° %s de %s de la section %s | %s", requestDispatchSlip.getCode(),requestDispatchSlip.getFunction().getName()
					,requestDispatchSlip.getSection(),requestDispatchSlip.getBudgetCategory());
		if(Action.UPDATE.equals(action))
			return String.format("Modification du bordereau de demandes N° %s de %s de la section %s | %s", requestDispatchSlip.getCode(),requestDispatchSlip.getFunction().getName()
					,requestDispatchSlip.getSection(),requestDispatchSlip.getBudgetCategory());
		return null;
	}
	
	static String addCreditManagersAuthorizingOfficersFinancialControllersAssistantsTabMenu(Collection<Map<Object,Object>> cellsMaps,String outcome
			,Collection<TabMenu.Tab> mastersTabs,String selectedMasterTab,String selectedFunctionIdentifier) {		
		Collection<Function> functions = DependencyInjection.inject(FunctionController.class).readCreditManagersAuthorizingOfficersFinancialControllersAssistants();
		if(CollectionHelper.isEmpty(functions))
			return null;
		//String identifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		if(StringHelper.isBlank(selectedFunctionIdentifier))
			selectedFunctionIdentifier = CollectionHelper.getFirst(functions).getIdentifier();
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer tabActiveIndex = null,index = 0;
		for(Function function : functions) {
			tabMenuItems.add(new MenuItem().setValue(function.getName())
				.addParameter(TabMenu.Tab.PARAMETER_NAME, TabMenu.Tab.getByParameterValue(mastersTabs, selectedMasterTab).getParameterValue())
				.addParameter(ParameterName.stringify(Function.class), function.getIdentifier())
			);
			if(function.getIdentifier().equals(selectedFunctionIdentifier))
				tabActiveIndex = index;
			else
				index++;
		}		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,outcome,TabMenu.FIELD_ACTIVE_INDEX,tabActiveIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		return selectedFunctionIdentifier;
	}
	
	static TabMenu buildProfileListPageTabMenu(ProfileType profileType) {
		Collection<ProfileType> profileTypes = EntityReader.getInstance().readMany(ProfileType.class, new Arguments<ProfileType>()
				.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		if(CollectionHelper.isNotEmpty(profileTypes)) {		
			for(ProfileType index : profileTypes)
				tabMenuItems.add(new MenuItem().setValue(index.getName()).setOutcome("profileListView")
						.addParameter(ParameterName.stringify(ProfileType.class), index.getIdentifier()));
		}
		Boolean isService = ValueHelper.convertToBoolean(WebController.getInstance().getRequestParameter(ParameterName.stringify(Service.class)));
		tabMenuItems.add(new MenuItem().setValue("Service").setOutcome("profileListView").addParameter(ParameterName.stringify(Service.class), Boolean.TRUE.toString()));
		return TabMenu.build(TabMenu.FIELD_ACTIVE_INDEX,Boolean.TRUE.equals(isService) ? CollectionHelper.getSize(profileTypes) : ((List<ProfileType>)profileTypes).indexOf(profileType)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
	}
	
	static TabMenu buildFunctionListPageTabMenu(FunctionType functionType) {
		Collection<FunctionType> functionTypes = EntityReader.getInstance().readMany(FunctionType.class, new Arguments<FunctionType>()
				.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(FunctionTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		if(CollectionHelper.isNotEmpty(functionTypes)) {		
			for(FunctionType index : functionTypes)
				tabMenuItems.add(new MenuItem().setValue(index.getName()).setOutcome("functionListView")
						.addParameter(ParameterName.stringify(FunctionType.class), index.getIdentifier()));
		}
		/*
		Boolean isScopeFunction = ValueHelper.convertToBoolean(WebController.getInstance().getRequestParameter(ParameterName.stringify(ScopeFunction.class)));
		tabMenuItems.add(new MenuItem().setValue("Poste").setOutcome("scopeFunctionListView").addParameter(ParameterName.stringify(ScopeFunction.class), Boolean.TRUE.toString()));
		
		Boolean isScopeFunctionExecutionImputation = ValueHelper.convertToBoolean(WebController.getInstance().getRequestParameter(ParameterName.stringify(ExecutionImputation.class)));
		tabMenuItems.add(new MenuItem().setValue("Imputation").setOutcome("executionImputationListView").addParameter(ParameterName.stringify(ExecutionImputation.class), Boolean.TRUE.toString()));
		*/
		Integer index;
		/*if(Boolean.TRUE.equals(isScopeFunction))
			index = CollectionHelper.getSize(functionTypes);
		else if(Boolean.TRUE.equals(isScopeFunctionExecutionImputation))
			index = CollectionHelper.getSize(functionTypes) + 1;
		else*/
			index = ((List<FunctionType>)functionTypes).indexOf(functionType);
		
		return TabMenu.build(TabMenu.FIELD_ACTIVE_INDEX,index,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
	}
	
	static AutoComplete buildActorAutoCompleteRedirector(Actor actor,String outcome_) {
		AutoComplete autoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto().addField(ActorQuerier.PARAMETER_NAME_STRING, "%"+ValueHelper.defaultToIfBlank(autoComplete.get__queryString__(),ConstantEmpty.STRING)+"%");
			}
		},AutoComplete.FIELD_PLACEHOLDER,"Rechercher...");
		
		autoComplete.enableAjaxItemSelect();
		autoComplete.getAjaxes().get("itemSelect").setListener(new Ajax.Listener.AbstractImpl() {
			@Override
			protected void run(AbstractAction action) {
				Actor actor = (Actor) FieldHelper.read(action.get__argument__(), "source.value");
				if(actor != null) {
					Map<String,List<String>> map = new HashMap<>();
					map.put(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(actor.getIdentifier()));
					JsfController.getInstance().redirect(outcome_,map);
				}
			}
		});
		autoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
		autoComplete.setReaderUsable(Boolean.TRUE);
		autoComplete.setReadQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_STRING);
		autoComplete.setCountQueryIdentifier(ActorQuerier.QUERY_NAME_COUNT_BY_STRING);
		
		if(actor == null) {
			
		}else {
			if(autoComplete != null)
				autoComplete.setValue(actor);
		}
		return autoComplete;
	}
	
	static String formatService(String administrativeUnit,String administrativeFunction,String section) {
		if(StringHelper.isBlank(administrativeUnit))
			return ConstantEmpty.STRING;
		String string = administrativeUnit;
		if(StringHelper.isNotBlank(string) && StringHelper.isNotBlank(administrativeFunction))
			string = StringUtils.substringBefore(section," ")+" - " + string + " | "+administrativeFunction;
		return string;
	}
	
	static Profile getProfileFromRequestParameterEntityAsParent(Actor actor) {
		Profile profile = WebController.getInstance().getRequestParameterEntityAsParent(Profile.class);
		if(profile != null)
			return profile;
		if(actor == null)
			return null;		
		profile = CollectionHelper.getFirst(EntityReader.getInstance().readMany(Profile.class, new Arguments<Profile>()
				.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setQueryExecutorArguments(
						new QueryExecutorArguments.Dto().setQueryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES)
						.addFilterField(ProfileQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()))))));
		return profile;
	}
	
	public static String formatActorListPrivilegesWindowTitle(Actor actor,Boolean isStatic) {
		if(actor == null || isStatic == null || !isStatic)
			return ACTOR_WINDOW_TITLE_FORMAT_LIST_PRIVILEGES;
		return String.format(ACTOR_WINDOW_TITLE_FORMAT_LIST_PRIVILEGES_ACCOUNT,actor.getCode(),actor.getNames());
	}
	
	public static String formatActorListScopesWindowTitle(Actor actor,Boolean isStatic) {
		if(actor == null || isStatic == null || !isStatic)
			return ACTOR_WINDOW_TITLE_FORMAT_LIST_SCOPES;
		return String.format(ACTOR_WINDOW_TITLE_FORMAT_LIST_SCOPES_ACCOUNT,actor.getCode(),actor.getNames());
	}
	
	public static String formatActorEditPrivilegesWindowTitle(Actor actor,String by) {
		if(actor == null)
			return ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES;
		if(StringHelper.isBlank(by))
			return String.format(ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_ACCOUNT,actor.getCode(),actor.getNames());
		return String.format(ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY_ACCOUNT, by,actor.getCode(),actor.getNames());
	}
	
	public static String formatActorEditPrivilegesWindowTitle(Actor actor) {
		return formatActorEditPrivilegesWindowTitle(actor, null);
	}
	
	String ACTOR_WINDOW_TITLE_FORMAT_LIST_PRIVILEGES = "Assignation";
	String ACTOR_WINDOW_TITLE_FORMAT_LIST_SCOPES = "Affectation";
	String ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES = "Assignation de privilèges";
	String ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY = ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES+" par %s";
	
	String ACTOR_WINDOW_TITLE_FORMAT_ACCOUNT = " | Compte utilisateur %s : %s";
	
	String ACTOR_WINDOW_TITLE_FORMAT_LIST_PRIVILEGES_ACCOUNT = ACTOR_WINDOW_TITLE_FORMAT_LIST_PRIVILEGES+ACTOR_WINDOW_TITLE_FORMAT_ACCOUNT;
	String ACTOR_WINDOW_TITLE_FORMAT_LIST_SCOPES_ACCOUNT = ACTOR_WINDOW_TITLE_FORMAT_LIST_SCOPES+ACTOR_WINDOW_TITLE_FORMAT_ACCOUNT;
	
	String ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_ACCOUNT = ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES+ACTOR_WINDOW_TITLE_FORMAT_ACCOUNT;
	String ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY_ACCOUNT = ACTOR_WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY+ACTOR_WINDOW_TITLE_FORMAT_ACCOUNT;
}