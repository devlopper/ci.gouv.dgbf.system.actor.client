package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;

@ApplicationScoped
public class ActorControllerImpl extends AbstractControllerEntityImpl<Actor> implements ActorController,Serializable {
	private static final long serialVersionUID = 1L;

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
