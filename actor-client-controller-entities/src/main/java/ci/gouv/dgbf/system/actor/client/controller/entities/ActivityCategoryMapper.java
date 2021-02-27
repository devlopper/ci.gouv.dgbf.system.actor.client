package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActivityCategoryDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ActivityCategoryMapper extends AbstractMapperSourceDestinationImpl<ActivityCategory, ActivityCategoryDto> {
	private static final long serialVersionUID = 1L;
    	
}