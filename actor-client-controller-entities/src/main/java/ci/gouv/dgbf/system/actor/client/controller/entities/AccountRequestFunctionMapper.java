package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.AccountRequestFunctionDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class AccountRequestFunctionMapper extends AbstractMapperSourceDestinationImpl<AccountRequestFunction, AccountRequestFunctionDto> {
	private static final long serialVersionUID = 1L;
    	
}