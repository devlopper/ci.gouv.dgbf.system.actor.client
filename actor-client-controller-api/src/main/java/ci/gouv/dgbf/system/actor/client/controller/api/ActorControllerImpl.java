package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowableHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;

@ApplicationScoped
public class ActorControllerImpl extends AbstractControllerEntityImpl<Actor> implements ActorController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object recordRequests(Actor actor) {
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("actor", actor);
		actor.setActorsIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(actor.getActors()));
		actor.setProfilesIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(actor.getProfiles()));
		actor.setScopesIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(actor.getScopes()));
		actor.setIgnoreExisting(Boolean.TRUE);
		actor.set__auditWho__(SessionHelper.getUserName());
		Arguments<Actor> arguments = new Arguments<Actor>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(actor);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorBusiness.RECORD_REQUESTS));
		EntitySaver.getInstance().save(Actor.class, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	public Collection<Actor> search(String string) {
		Arguments<Actor> arguments = new Arguments<Actor>()
				.queryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC).flags(ActorQuerier.FLAG_SEARCH)
				.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.FIELDS_CODE_NAMES_ELECTRONIC_MAIL_ADDRESS);
		if(StringHelper.isNotBlank(string))
			arguments.filterFieldsValues(ActorQuerier.PARAMETER_NAME_SEARCH,string);
		return EntityReader.getInstance().readMany(Actor.class, arguments);
	}
	
}
