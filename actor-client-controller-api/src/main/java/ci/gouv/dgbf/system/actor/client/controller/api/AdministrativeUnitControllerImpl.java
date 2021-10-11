package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.AdministrativeUnit;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AdministrativeUnitQuerier;

@ApplicationScoped
public class AdministrativeUnitControllerImpl extends AbstractControllerEntityImpl<AdministrativeUnit> implements AdministrativeUnitController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<AdministrativeUnit> read() {
		return EntityReader.getInstance().readMany(AdministrativeUnit.class,AdministrativeUnitQuerier.QUERY_IDENTIFIER_READ_DYNAMIC);
	}
	
}