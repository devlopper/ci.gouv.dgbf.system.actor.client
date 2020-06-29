package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.SectionDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class SectionMapper extends AbstractMapperSourceDestinationImpl<Section, SectionDto> {
	private static final long serialVersionUID = 1L;
    	
}