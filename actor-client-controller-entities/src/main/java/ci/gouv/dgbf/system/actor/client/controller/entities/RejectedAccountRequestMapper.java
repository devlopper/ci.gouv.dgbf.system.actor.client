package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.RejectedAccountRequestDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class RejectedAccountRequestMapper extends AbstractMapperSourceDestinationImpl<RejectedAccountRequest, RejectedAccountRequestDto> {
	private static final long serialVersionUID = 1L;
    	
}