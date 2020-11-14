package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ClusterPrivilegesDto;
import org.cyk.utility.__kernel__.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ClusterPrivilegesMapper extends AbstractMapperSourceDestinationImpl<ClusterPrivileges, ClusterPrivilegesDto> {
	private static final long serialVersionUID = 1L;
    	
}