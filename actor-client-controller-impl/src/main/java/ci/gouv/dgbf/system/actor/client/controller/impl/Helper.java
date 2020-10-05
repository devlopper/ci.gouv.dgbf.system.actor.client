package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Service;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileTypeQuerier;

public interface Helper {

	static TabMenu buildProfileListPageTabMenu(ProfileType profileType,Boolean isService) {
		Collection<ProfileType> profileTypes = EntityReader.getInstance().readMany(ProfileType.class, new Arguments<ProfileType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		if(CollectionHelper.isNotEmpty(profileTypes)) {		
			for(ProfileType index : profileTypes)
				tabMenuItems.add(new MenuItem().setValue(index.getName()).setOutcome("profileListView")
						.addParameter(ParameterName.stringify(ProfileType.class), index.getIdentifier()));
		}		
		tabMenuItems.add(new MenuItem().setValue("Service").setOutcome("profileListView").addParameter(ParameterName.stringify(Service.class), Boolean.TRUE.toString()));
		return TabMenu.build(TabMenu.FIELD_ACTIVE_INDEX,Boolean.TRUE.equals(isService) ? CollectionHelper.getSize(profileTypes) : ((List<ProfileType>)profileTypes).indexOf(profileType)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
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
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
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