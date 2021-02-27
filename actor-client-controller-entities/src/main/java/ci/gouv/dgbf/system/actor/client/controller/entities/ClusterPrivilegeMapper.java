package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ClusterPrivilegeDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ClusterPrivilegeMapper extends AbstractMapperSourceDestinationImpl<ClusterPrivilege, ClusterPrivilegeDto> {
	private static final long serialVersionUID = 1L;
    	
}