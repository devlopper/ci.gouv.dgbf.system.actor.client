package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.AdministrativeUnitDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class AdministrativeUnitMapper extends AbstractMapperSourceDestinationImpl<AdministrativeUnit, AdministrativeUnitDto> {
	private static final long serialVersionUID = 1L;
    	
}