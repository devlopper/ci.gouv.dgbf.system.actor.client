package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.UserAccountDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserAccountMapper extends AbstractMapperSourceDestinationImpl<UserAccount, UserAccountDto> {
	private static final long serialVersionUID = 1L;
    	
}