package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorScopeDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActorScopeMapper extends AbstractMapperSourceDestinationImpl<ActorScope, ActorScopeDto> {
	private static final long serialVersionUID = 1L;
    	
}