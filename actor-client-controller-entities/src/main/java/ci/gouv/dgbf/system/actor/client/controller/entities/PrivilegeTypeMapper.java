package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.PrivilegeTypeDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class PrivilegeTypeMapper extends AbstractMapperSourceDestinationImpl<PrivilegeType, PrivilegeTypeDto> {
	private static final long serialVersionUID = 1L;
    	
}