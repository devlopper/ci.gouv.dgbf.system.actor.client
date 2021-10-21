package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.TypeHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.server.representation.api.ScopeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeDto;

@ApplicationScoped
public class ScopeControllerImpl extends AbstractControllerEntityImpl<Scope> implements ScopeController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Collection<Scope> getVisibleSectionsByActorCode(String actorCode) {
		Response response = ScopeRepresentation.getProxy().getVisibleSectionsByActorCode(actorCode);
		@SuppressWarnings("unchecked")
		Collection<ScopeDto> dtos = (Collection<ScopeDto>) response.readEntity(TypeHelper.instantiateGenericCollectionParameterizedTypeForJaxrs(Collection.class,ScopeDto.class));
		if(CollectionHelper.isEmpty(dtos))
			return null;
		Collection<Scope> scopes = new ArrayList<>();
		dtos.forEach(dto -> {
			Scope scope = new Scope();
			scope.setIdentifier(dto.getIdentifier());
			scope.setCode(dto.getCode());
			scope.setName(dto.getName());
			scopes.add(scope);
		});
		return scopes;
	}
	
	@Override
	public Collection<Scope> getLoggedInVisibleSections() {
		return getVisibleSectionsByActorCode(SessionHelper.getUserName());
	}
	
	@Override
	public Collection<String> getVisibleSectionsIdentifiersByActorCode(String actorCode) {
		Response response = ScopeRepresentation.getProxy().getVisibleSectionsIdentifiersByActorCode(actorCode);
		@SuppressWarnings("unchecked")
		Collection<String> identifiers = (Collection<String>) response.readEntity(TypeHelper.instantiateGenericCollectionParameterizedTypeForJaxrs(Collection.class,String.class));
		return identifiers;
	}
	
	@Override
	public Collection<String> getLoggedInVisibleSectionsIdentifiers() {
		return getVisibleSectionsIdentifiersByActorCode(SessionHelper.getUserName());
	}
}