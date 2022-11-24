package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.Exercise;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExerciseQuerier;

public interface ExerciseController extends ControllerEntity<Exercise> {

	default Collection<Exercise> readAllForUI() {
		return EntityReader.getInstance().readMany(Exercise.class, ExerciseQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}	
}