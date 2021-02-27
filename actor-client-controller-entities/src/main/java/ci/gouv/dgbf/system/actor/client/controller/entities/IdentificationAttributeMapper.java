package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.IdentificationAttributeDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class IdentificationAttributeMapper extends AbstractMapperSourceDestinationImpl<IdentificationAttribute, IdentificationAttributeDto> {
	private static final long serialVersionUID = 1L;
    	
}