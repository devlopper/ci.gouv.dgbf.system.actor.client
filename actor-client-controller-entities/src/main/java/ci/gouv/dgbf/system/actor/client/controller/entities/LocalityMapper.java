package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.LocalityDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class LocalityMapper extends AbstractMapperSourceDestinationImpl<Locality, LocalityDto> {
	private static final long serialVersionUID = 1L;
    	
}