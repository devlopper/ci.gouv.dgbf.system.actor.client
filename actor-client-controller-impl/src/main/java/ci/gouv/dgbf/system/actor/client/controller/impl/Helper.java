package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;

public interface Helper {

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