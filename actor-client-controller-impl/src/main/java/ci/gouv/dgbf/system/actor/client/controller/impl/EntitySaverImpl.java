package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.server.business.api.AccountRequestBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorScopeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfileFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfilePrivilegeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.AccountRequestDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.FunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfileDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfileFunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfilePrivilegeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeDto;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class EntitySaverImpl extends EntitySaver.AbstractImpl implements Serializable {

	@Override
	protected <T> void prepare(Class<T> controllerEntityClass,Arguments<T> arguments) {
		if(arguments.getRepresentationArguments() != null && arguments.getRepresentation() == null) {
			if(ProfilePrivilegeBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			else if(ProfilePrivilegeBusiness.CREATE_FROM_FUNCTIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			else if(ProfilePrivilegeBusiness.CREATE_FROM_PROFILES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			else if(ProfileFunctionBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfileFunctionRepresentation.getProxy());
			else if(ActorScopeBusiness.DELETE_BY_ACTOR_BY_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
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
		}
		
		if(arguments != null && ProfilePrivilegeBusiness.CREATE_FROM_FUNCTIONS.equals(arguments.getActionIdentifier())) {
			return ((ProfilePrivilegeRepresentation)representation).createFromFunctions(CollectionHelper.cast(FunctionDto.class, creatables));
		}
		
		if(arguments != null && ProfilePrivilegeBusiness.CREATE_FROM_PROFILES.equals(arguments.getActionIdentifier())) {
			return ((ProfilePrivilegeRepresentation)representation).createFromProfiles(CollectionHelper.cast(ProfileDto.class, creatables));
		}
		
		if(arguments != null && ProfileFunctionBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			Collection<ProfileFunctionDto> dtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables)
					dtos.add((ProfileFunctionDto) index);
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables)
					dtos.add((ProfileFunctionDto) index);
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables)
					dtos.add(((ProfileFunctionDto) index).set__deletable__(Boolean.TRUE));
			return ((ProfileFunctionRepresentation)representation).save(dtos);
		}
		
		if(arguments != null && ActorScopeBusiness.DELETE_BY_ACTOR_BY_SCOPES.equals(arguments.getActionIdentifier())) {
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
		
		if(arguments != null && AccountRequestBusiness.ACCEPT.equals(arguments.getActionIdentifier())) {
			Collection<AccountRequestDto> accountRequests = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables) {
					AccountRequestDto accountRequest = (AccountRequestDto) index;
					accountRequests.add(accountRequest);
				}
			return ((AccountRequestRepresentation)representation).accept(accountRequests);
		}
		
		if(arguments != null && AccountRequestBusiness.REJECT.equals(arguments.getActionIdentifier())) {
			Collection<AccountRequestDto> accountRequests = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables) {
					AccountRequestDto accountRequest = (AccountRequestDto) index;
					accountRequests.add(accountRequest);
				}
			return ((AccountRequestRepresentation)representation).reject(accountRequests);
		}
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}