package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.server.business.api.AccountRequestBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ServiceBusiness;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorProfileRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorScopeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfileFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfilePrivilegeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfileRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ServiceRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.AccountRequestDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorProfileDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorScopeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.FunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfileDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfileFunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfilePrivilegeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ServiceDto;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class EntitySaverImpl extends EntitySaver.AbstractImpl implements Serializable {

	@Override
	protected <T> void prepare(Class<T> controllerEntityClass,Arguments<T> arguments) {
		if(arguments.getRepresentationArguments() != null && arguments.getRepresentation() == null) {
			if(ProfilePrivilegeBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			
			else if(ProfileBusiness.SAVE_PRIVILEGES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfileRepresentation.getProxy());
			
			else if(ProfilePrivilegeBusiness.CREATE_FROM_FUNCTIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			else if(ProfilePrivilegeBusiness.CREATE_FROM_PROFILES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			else if(ProfileFunctionBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfileFunctionRepresentation.getProxy());
			else if(ActorProfileBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorProfileRepresentation.getProxy());
			else if(ActorScopeBusiness.CREATE_BY_ACTOR_BY_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			else if(ActorScopeBusiness.CREATE_BY_ACTORS_BY_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			else if(ActorScopeBusiness.DELETE_BY_ACTOR_BY_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			else if(ActorScopeBusiness.DELETE_BY_ACTORS_BY_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			else if(AccountRequestBusiness.RECORD.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AccountRequestRepresentation.getProxy());
			else if(AccountRequestBusiness.SUBMIT.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AccountRequestRepresentation.getProxy());
			
			else if(ActorBusiness.CREATE_PRIVILEGES_FROM_FUNCTIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorRepresentation.getProxy());
			else if(ActorBusiness.CREATE_PROFILES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorRepresentation.getProxy());
			else if(ActorBusiness.DELETE_PROFILES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorRepresentation.getProxy());
			
			else if(ActorBusiness.CREATE_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			else if(ActorBusiness.DELETE_SCOPES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			
			else if(ServiceBusiness.DELETE_ALL_KEYCLOAK_AUTHORIZATION_POLICIES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DELETE_ALL_KEYCLOAK_AUTHORIZATION_RESOURCES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATION_POLICIES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATION_RESOURCES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATION_PERMISSIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DELETE_KEYCLOAK_AUTHORIZATIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATIONS_FROM_SCRATCH.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_ALL_KEYCLOAK_AUTHORIZATIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
			else if(ServiceBusiness.DERIVE_ALL_KEYCLOAK_AUTHORIZATIONS_FROM_SCRATCH.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ServiceRepresentation.getProxy());
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
		
		if(arguments != null && ActorProfileBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			Collection<ActorProfileDto> dtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables)
					dtos.add((ActorProfileDto) index);
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables)
					dtos.add((ActorProfileDto) index);
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables)
					dtos.add(((ActorProfileDto) index).set__deletable__(Boolean.TRUE));
			return ((ActorProfileRepresentation)representation).save(dtos);
		}
		
		if(arguments != null && ActorScopeBusiness.CREATE_BY_ACTOR_BY_SCOPES.equals(arguments.getActionIdentifier())) {
			ActorDto actor = null;
			Collection<ScopeDto> scopes = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables) {
					ActorScopeDto actorScope = ((ActorScopeDto) index);
					actor = actorScope.getActor();
					scopes.add(actorScope.getScope().setActor(actor));
				}
			return ((ActorScopeRepresentation)representation).createByScopes(scopes);
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
		
		if(arguments != null && ActorBusiness.CREATE_SCOPES.equals(arguments.getActionIdentifier()))
			return ((ActorScopeRepresentation)representation).createByActors(CollectionHelper.cast(ActorDto.class, creatables));
		
		if(arguments != null && ActorBusiness.DELETE_SCOPES.equals(arguments.getActionIdentifier()))
			return ((ActorScopeRepresentation)representation).deleteByActors(CollectionHelper.cast(ActorDto.class, deletables));
		
		
		if(arguments != null && AccountRequestBusiness.RECORD.equals(arguments.getActionIdentifier())) {
			Collection<AccountRequestDto> accountRequests = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables) {
					AccountRequestDto accountRequest = (AccountRequestDto) index;
					accountRequests.add(accountRequest);
				}
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables) {
					AccountRequestDto accountRequest = (AccountRequestDto) index;
					accountRequests.add(accountRequest);
				}
			return ((AccountRequestRepresentation)representation).record(accountRequests);
		}
		
		if(arguments != null && AccountRequestBusiness.SUBMIT.equals(arguments.getActionIdentifier())) {
			Collection<AccountRequestDto> accountRequests = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables) {
					AccountRequestDto accountRequest = (AccountRequestDto) index;
					accountRequests.add(accountRequest);
				}
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables) {
					AccountRequestDto accountRequest = (AccountRequestDto) index;
					accountRequests.add(accountRequest);
				}
			return ((AccountRequestRepresentation)representation).submit(accountRequests);
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
		
		if(arguments != null && ActorBusiness.CREATE_PRIVILEGES_FROM_FUNCTIONS.equals(arguments.getActionIdentifier())) {
			Collection<ActorDto> actors = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables) {
					ActorDto actor = (ActorDto) index;
					actors.add(actor);
				}
			return ((ActorRepresentation)representation).createPrivilegesFromFunctions(actors);
		}
		
		if(arguments != null && ActorBusiness.CREATE_PROFILES.equals(arguments.getActionIdentifier())) {
			Collection<ActorDto> actors = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables) {
					ActorDto actor = (ActorDto) index;
					actors.add(actor);
				}
			return ((ActorRepresentation)representation).createProfiles(actors);
		}
		
		if(arguments != null && ActorBusiness.DELETE_PROFILES.equals(arguments.getActionIdentifier())) {
			Collection<ActorDto> actors = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables) {
					ActorDto actor = (ActorDto) index;
					actors.add(actor);
				}
			return ((ActorRepresentation)representation).deleteProfiles(actors);
		}
		
		if(arguments != null && ProfileBusiness.SAVE_PRIVILEGES.equals(arguments.getActionIdentifier())) {
			Collection<ProfileDto> profiles = new ArrayList<>();
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Profiles à mettre à jour obligatoire");		
			for(Object index : updatables) {
				ProfileDto profile = (ProfileDto) index;
				profiles.add(profile);
			}
			return ((ProfileRepresentation)representation).savePrivileges(profiles);
		}
		
		if(arguments != null && ServiceBusiness.DELETE_ALL_KEYCLOAK_AUTHORIZATION_POLICIES.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deleteAllKeycloakAuthorizationPolicies(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATION_POLICIES.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deriveKeycloakAuthorizationPolicies(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DELETE_ALL_KEYCLOAK_AUTHORIZATION_RESOURCES.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deleteAllKeycloakAuthorizationResources(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATION_RESOURCES.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deriveKeycloakAuthorizationResources(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATION_PERMISSIONS.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deriveKeycloakAuthorizationPermissions(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATIONS.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deriveKeycloakAuthorizations(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DELETE_KEYCLOAK_AUTHORIZATIONS.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deleteKeycloakAuthorizations(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DERIVE_KEYCLOAK_AUTHORIZATIONS_FROM_SCRATCH.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Services à mettre à jour obligatoire");		
			return ((ServiceRepresentation)representation).deriveKeycloakAuthorizationsFromScratch(CollectionHelper.cast(ServiceDto.class, updatables));
		}
		
		if(arguments != null && ServiceBusiness.DERIVE_ALL_KEYCLOAK_AUTHORIZATIONS.equals(arguments.getActionIdentifier()))
			return ((ServiceRepresentation)representation).deriveAllKeycloakAuthorizations();
		
		if(arguments != null && ServiceBusiness.DERIVE_ALL_KEYCLOAK_AUTHORIZATIONS_FROM_SCRATCH.equals(arguments.getActionIdentifier()))
			return ((ServiceRepresentation)representation).deriveAllKeycloakAuthorizationsFromScratch();
		
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}