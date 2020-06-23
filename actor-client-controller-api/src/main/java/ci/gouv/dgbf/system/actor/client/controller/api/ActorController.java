package ci.gouv.dgbf.system.actor.client.controller.api;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;

public interface ActorController extends ControllerEntity<Actor> {

	default Actor readByElectronicMailAddress(String electronicMailAddress) {
		return EntityReader.getInstance().readOne(Actor.class,new Arguments<Actor>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation
				.Arguments().setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_ELECTRONIC_MAIL_ADDRESS)
						.addFilterField(ActorQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS,electronicMailAddress)
						)));
	}
	
}
