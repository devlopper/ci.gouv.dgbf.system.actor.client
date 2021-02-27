package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.AuthorizingOfficerServiceDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class AuthorizingOfficerServiceMapper extends AbstractMapperSourceDestinationImpl<AuthorizingOfficerService, AuthorizingOfficerServiceDto> {
	private static final long serialVersionUID = 1L;
    	
}