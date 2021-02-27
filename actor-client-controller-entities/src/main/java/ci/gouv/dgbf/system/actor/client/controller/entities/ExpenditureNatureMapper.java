package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ExpenditureNatureDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ExpenditureNatureMapper extends AbstractMapperSourceDestinationImpl<ExpenditureNature, ExpenditureNatureDto> {
	private static final long serialVersionUID = 1L;
    	
}