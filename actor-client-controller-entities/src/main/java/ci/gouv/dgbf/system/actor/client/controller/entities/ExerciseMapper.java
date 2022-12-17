package ci.gouv.dgbf.system.actor.client.controller.entities;
import ci.gouv.dgbf.system.actor.server.representation.entities.ExerciseDto;
import org.cyk.utility.controller.AbstractMapperSourceDestinationImpl;
import org.mapstruct.Mapper;

@Mapper
public abstract class ExerciseMapper extends AbstractMapperSourceDestinationImpl<Exercise, ExerciseDto> {
	private static final long serialVersionUID = 1L;
    	
}