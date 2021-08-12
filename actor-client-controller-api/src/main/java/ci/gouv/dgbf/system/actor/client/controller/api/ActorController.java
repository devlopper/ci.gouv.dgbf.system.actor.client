package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.__kernel__.mapping.MappingHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.ControllerEntity;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;

public interface ActorController extends ControllerEntity<Actor> {

	default Actor getOneToBeCreatedByPublic() {
		ActorDto actorDto = ActorRepresentation.getProxy().getOneToBeCreatedByPublic();
		if(actorDto == null)
			return null;
		return MappingHelper.getSource(actorDto, Actor.class);
	}
	
	default Actor readByCode(String code) {
		return EntityReader.getInstance().readOne(Actor.class,new Arguments<Actor>().setRepresentationArguments(new org.cyk.utility.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_CODE)
						.addFilterField(ActorQuerier.PARAMETER_NAME_CODE,code)
						)));
	}
	
	default Actor readByElectronicMailAddress(String electronicMailAddress) {
		return EntityReader.getInstance().readOne(Actor.class,new Arguments<Actor>().setRepresentationArguments(new org.cyk.utility.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_ELECTRONIC_MAIL_ADDRESS)
						.addFilterField(ActorQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS,electronicMailAddress)
						)));
	}
	
	default Actor getLoggedIn() {
		String username = SessionHelper.getUserName();
		if(StringHelper.isBlank(username))
			return null;
		return EntityReader.getInstance().readOne(Actor.class, ActorQuerier.QUERY_IDENTIFIER_READ_BY_CODE, ActorQuerier.PARAMETER_NAME_CODE,username);
		//readByBusinessIdentifier(username);
	}
	
	Collection<Actor> search(String string);
}