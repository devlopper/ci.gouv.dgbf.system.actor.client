package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.FinancialControllerServiceDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class FinancialControllerServiceMapper extends AbstractMapperSourceDestinationImpl<FinancialControllerService, FinancialControllerServiceDto> {
	private static final long serialVersionUID = 1L;
    	
}