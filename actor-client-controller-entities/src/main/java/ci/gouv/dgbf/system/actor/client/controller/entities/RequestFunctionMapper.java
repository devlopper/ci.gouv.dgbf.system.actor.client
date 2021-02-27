package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.RequestFunctionDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class RequestFunctionMapper extends AbstractMapperSourceDestinationImpl<RequestFunction, RequestFunctionDto> {
	private static final long serialVersionUID = 1L;
    	
}