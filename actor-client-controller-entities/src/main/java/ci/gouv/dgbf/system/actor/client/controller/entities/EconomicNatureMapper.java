package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.EconomicNatureDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class EconomicNatureMapper extends AbstractMapperSourceDestinationImpl<EconomicNature, EconomicNatureDto> {
	private static final long serialVersionUID = 1L;
    	
}