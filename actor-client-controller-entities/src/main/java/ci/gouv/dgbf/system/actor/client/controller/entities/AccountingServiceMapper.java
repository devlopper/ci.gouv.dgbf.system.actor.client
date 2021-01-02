package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.AccountingServiceDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class AccountingServiceMapper extends AbstractMapperSourceDestinationImpl<AccountingService, AccountingServiceDto> {
	private static final long serialVersionUID = 1L;
    	
}