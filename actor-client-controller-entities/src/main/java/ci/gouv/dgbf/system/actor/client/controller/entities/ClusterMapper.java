package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ClusterDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ClusterMapper extends AbstractMapperSourceDestinationImpl<Cluster, ClusterDto> {
	private static final long serialVersionUID = 1L;
    	
}