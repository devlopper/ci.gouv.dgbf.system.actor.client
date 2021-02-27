package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ClusterAdministratorDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ClusterAdministratorMapper extends AbstractMapperSourceDestinationImpl<ClusterAdministrator, ClusterAdministratorDto> {
	private static final long serialVersionUID = 1L;
    	
}