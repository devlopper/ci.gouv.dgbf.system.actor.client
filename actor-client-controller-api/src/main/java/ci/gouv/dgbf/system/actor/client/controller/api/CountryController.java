package ci.gouv.dgbf.system.actor.client.controller.api;

import java.util.Collection;

import org.cyk.utility.client.controller.ControllerEntity;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.Country;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.CountryQuerier;

public interface CountryController extends ControllerEntity<Country> {

	default Collection<Country> readAllForUI() {
		return EntityReader.getInstance().readMany(Country.class, CountryQuerier.QUERY_IDENTIFIER_READ_ALL_FOR_UI);
	}
	
}