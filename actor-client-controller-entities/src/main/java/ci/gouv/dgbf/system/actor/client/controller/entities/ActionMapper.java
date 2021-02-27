package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActionDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActionMapper extends AbstractMapperSourceDestinationImpl<Action, ActionDto> {
	private static final long serialVersionUID = 1L;
    	
}