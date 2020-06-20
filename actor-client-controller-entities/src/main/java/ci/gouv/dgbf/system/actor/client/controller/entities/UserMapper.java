package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.UserDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class UserMapper extends AbstractMapperSourceDestinationImpl<User, UserDto> {
	private static final long serialVersionUID = 1L;
    	
}