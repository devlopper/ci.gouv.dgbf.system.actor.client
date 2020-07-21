package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.security.Principal;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.security.User;
import org.cyk.utility.__kernel__.security.UserBuilder;
import org.cyk.utility.__kernel__.string.StringHelper;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class UserBuilderImpl extends UserBuilder.AbstractImpl implements Serializable {

	@Override
	protected User __buildKeycloak__(Principal principal) {
		User user = super.__buildKeycloak__(principal);
		if(user != null) {
			Actor actor = StringHelper.isBlank(user.getName()) ? null : EntityReader.getInstance().readOne(Actor.class, ActorQuerier.QUERY_IDENTIFIER_READ_BY_CODE
					,ActorQuerier.PARAMETER_NAME_CODE, user.getName());
			if(actor != null) {
				if(StringHelper.isNotBlank(actor.getNames())) {
					user.setNames(actor.getNames());
				}	
			}			
		}
		return user;
	}	
}