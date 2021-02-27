package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.CivilityDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class CivilityMapper extends AbstractMapperSourceDestinationImpl<Civility, CivilityDto> {
	private static final long serialVersionUID = 1L;
    	
}