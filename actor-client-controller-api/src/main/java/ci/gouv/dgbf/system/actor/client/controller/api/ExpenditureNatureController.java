package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.ExpenditureNature;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExpenditureNatureQuerier;

public interface ExpenditureNatureController extends ControllerEntity<ExpenditureNature> {

	default Collection<ExpenditureNature> readAllForUI() {
		return EntityReader.getInstance().readMany(ExpenditureNature.class, ExpenditureNatureQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}	
	
}