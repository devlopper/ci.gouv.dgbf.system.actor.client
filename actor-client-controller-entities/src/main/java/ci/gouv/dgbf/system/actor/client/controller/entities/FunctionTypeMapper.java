package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.FunctionTypeDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FunctionTypeMapper extends AbstractMapperSourceDestinationImpl<FunctionType, FunctionTypeDto> {
	private static final long serialVersionUID = 1L;
    	
}