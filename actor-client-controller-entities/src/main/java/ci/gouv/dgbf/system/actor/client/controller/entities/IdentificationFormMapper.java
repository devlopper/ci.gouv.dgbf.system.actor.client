package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.IdentificationFormDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class IdentificationFormMapper extends AbstractMapperSourceDestinationImpl<IdentificationForm, IdentificationFormDto> {
	private static final long serialVersionUID = 1L;
    	
}