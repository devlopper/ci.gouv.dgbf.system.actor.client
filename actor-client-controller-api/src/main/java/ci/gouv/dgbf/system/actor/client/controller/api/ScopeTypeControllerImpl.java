package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

@ApplicationScoped
public class ScopeTypeControllerImpl extends AbstractControllerEntityImpl<ScopeType> implements ScopeTypeController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<ScopeType> read() {
		return EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>().queryIdentifierReadDynamicMany(ScopeType.class));
	}
	
	@Override
	public Collection<ScopeType> readRequestable() {
		return EntityReader.getInstance().readMany(ScopeType.class, new Arguments<ScopeType>().queryIdentifierReadDynamicMany(ScopeType.class)
				.filterFieldsValues(ScopeTypeQuerier.PARAMETER_NAME_REQUESTABLE,Boolean.TRUE));
	}
	
}