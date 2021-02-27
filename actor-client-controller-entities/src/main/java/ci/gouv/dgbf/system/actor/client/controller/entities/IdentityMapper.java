package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.IdentityDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class IdentityMapper extends AbstractMapperSourceDestinationImpl<Identity, IdentityDto> {
	private static final long serialVersionUID = 1L;
    	
}