package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActivityDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActivityMapper extends AbstractMapperSourceDestinationImpl<Activity, ActivityDto> {
	private static final long serialVersionUID = 1L;
    	
}