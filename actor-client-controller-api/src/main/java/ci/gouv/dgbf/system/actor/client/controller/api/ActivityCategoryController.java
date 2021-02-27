package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActivityCategoryQuerier;

public interface ActivityCategoryController extends ControllerEntity<ActivityCategory> {

	default Collection<ActivityCategory> readAllForUI() {
		return EntityReader.getInstance().readMany(ActivityCategory.class, ActivityCategoryQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}
	
}
