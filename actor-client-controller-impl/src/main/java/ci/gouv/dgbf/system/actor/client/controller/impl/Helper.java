package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.util.List;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.web.WebController;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;

public interface Helper {

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
}