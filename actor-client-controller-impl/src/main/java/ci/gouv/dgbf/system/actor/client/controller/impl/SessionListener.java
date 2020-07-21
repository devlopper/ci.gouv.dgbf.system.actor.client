package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.security.Principal;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.security.SecurityHelper;
import org.cyk.utility.__kernel__.security.User;
import org.cyk.utility.__kernel__.security.UserBuilder;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;

//@javax.servlet.annotation.WebListener
public class SessionListener implements HttpSessionListener {
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		LogHelper.logInfo(String.format("Session created at %s",LocalDateTime.now()), getClass());
		Principal principal = SecurityHelper.getPrincipal();
		User user = UserBuilder.getInstance().build(principal);
		if(user != null) {
			Actor actor = StringHelper.isBlank(user.getName()) ? null : EntityReader.getInstance().readOne(Actor.class, ActorQuerier.QUERY_IDENTIFIER_READ_BY_CODE
					,ActorQuerier.PARAMETER_NAME_CODE, user.getName());
			if(actor != null) {
				if(StringHelper.isNotBlank(actor.getNames()))
					user.setNames(actor.getNames());
				SessionHelper.setAttributeValue("usernames", user.getNames());
			}			
		}
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		LogHelper.logInfo(String.format("Session destroyed at %s",LocalDateTime.now()), getClass());
	}
}