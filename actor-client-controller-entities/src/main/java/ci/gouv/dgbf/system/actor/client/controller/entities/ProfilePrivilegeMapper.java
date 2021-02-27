package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfilePrivilegeDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ProfilePrivilegeMapper extends AbstractMapperSourceDestinationImpl<ProfilePrivilege, ProfilePrivilegeDto> {
	private static final long serialVersionUID = 1L;
    	
}