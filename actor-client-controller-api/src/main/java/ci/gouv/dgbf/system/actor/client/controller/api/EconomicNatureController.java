package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.client.controller.ControllerEntity;

import ci.gouv.dgbf.system.actor.client.controller.entities.EconomicNature;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.EconomicNatureQuerier;

public interface EconomicNatureController extends ControllerEntity<EconomicNature> {

	default Collection<EconomicNature> readAllForUI() {
		return EntityReader.getInstance().readMany(EconomicNature.class, EconomicNatureQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}	
}