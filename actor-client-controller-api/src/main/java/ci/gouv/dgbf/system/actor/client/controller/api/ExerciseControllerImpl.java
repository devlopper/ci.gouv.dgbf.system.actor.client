package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Exercise;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class ExerciseControllerImpl extends AbstractControllerEntityImpl<Exercise> implements ExerciseController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
