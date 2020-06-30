package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorScopeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfilePrivilegeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfilePrivilegeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeDto;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class EntitySaverImpl extends EntitySaver.AbstractImpl implements Serializable {

	@Override
	protected <T> void prepare(Class<T> controllerEntityClass,Arguments<T> arguments) {
		if(arguments.getRepresentationArguments() != null) {
			if(ProfilePrivilegeBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()) && arguments.getRepresentation() == null)
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			else if(ActorScopeBusiness.DELETE_BY_ACTOR_BY_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()) && arguments.getRepresentation() == null)
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
		}
		super.prepare(controllerEntityClass, arguments);
	}
	
	@Override
	protected <T> Response save(Object representation, Collection<?> creatables, Collection<?> updatables,Collection<?> deletables, org.cyk.utility.__kernel__.representation.Arguments arguments) {
		if(arguments != null && ProfilePrivilegeBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			Collection<ProfilePrivilegeDto> dtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables)
					dtos.add((ProfilePrivilegeDto) index);
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables)
					dtos.add((ProfilePrivilegeDto) index);
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables)
					dtos.add(((ProfilePrivilegeDto) index).set__deletable__(Boolean.TRUE));
			return ((ProfilePrivilegeRepresentation)representation).save(dtos);
		}if(arguments != null && ActorScopeBusiness.DELETE_BY_ACTOR_BY_SCOPES.equals(arguments.getActionIdentifier())) {
			ActorDto actor = null;
			Collection<ScopeDto> scopes = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables) {
					ScopeDto scope = ((ScopeDto) index).set__deletable__(Boolean.TRUE);
					actor = scope.getActor();
					scopes.add(scope.setActor(actor));
				}
			return ((ActorScopeRepresentation)representation).deleteByScopes(scopes);
		}
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}