package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.representation.RepresentationProxyGetter;

import ci.gouv.dgbf.system.actor.server.business.api.AccountRequestBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileRequestBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeRequestBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.AssignmentsBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ExecutionImputationBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.IdentificationFormAttributeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfileFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.RequestDispatchSlipBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.RequestScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeTypeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.business.api.ServiceBusiness;
import ci.gouv.dgbf.system.actor.server.representation.api.AccountRequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorProfileRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorProfileRequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorScopeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ActorScopeRequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.AssignmentsRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ExecutionImputationRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.IdentificationFormAttributeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfileFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfilePrivilegeRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ProfileRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestDispatchSlipRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestScopeFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ScopeFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ScopeTypeFunctionRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.api.ServiceRepresentation;
import ci.gouv.dgbf.system.actor.server.representation.entities.AccountRequestDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorProfileDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorProfileRequestDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorScopeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ActorScopeRequestDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.AssignmentsDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.FunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.IdentificationFormAttributeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfileDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfileFunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ProfilePrivilegeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.RequestDispatchSlipDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.RequestDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.RequestScopeFunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeFunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ScopeTypeFunctionDto;
import ci.gouv.dgbf.system.actor.server.representation.entities.ServiceDto;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class EntitySaverImpl extends EntitySaver.AbstractImpl implements Serializable {

	@Override
	protected <T> void prepare(Class<T> controllerEntityClass,Arguments<T> arguments) {
		if(arguments.getRepresentationArguments() != null && arguments.getRepresentation() == null) {
			if(ProfilePrivilegeBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfilePrivilegeRepresentation.getProxy());
			
			else if(ProfileBusiness.CREATE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfileRepresentation.getProxy());
			else if(ProfileBusiness.UPDATE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ProfileRepresentation.getProxy());
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
			else if(ActorScopeBusiness.VISIBLE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			else if(ActorScopeBusiness.UNVISIBLE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRepresentation.getProxy());
			
			else if(ActorScopeRequestBusiness.RECORD.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorScopeRequestRepresentation.getProxy());
			else if(ActorScopeRequestBusiness.CANCEL.equals(arguments.getRepresentationArguments().getActionIdentifier()))				
				arguments.setRepresentation(ActorScopeRequestRepresentation.getProxy());
			else if(ActorScopeRequestBusiness.PROCESS.equals(arguments.getRepresentationArguments().getActionIdentifier()))				
				arguments.setRepresentation(ActorScopeRequestRepresentation.getProxy());
			
			else if(ActorProfileRequestBusiness.RECORD.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorProfileRequestRepresentation.getProxy());
			else if(ActorProfileRequestBusiness.CANCEL.equals(arguments.getRepresentationArguments().getActionIdentifier()))				
				arguments.setRepresentation(ActorProfileRequestRepresentation.getProxy());
			else if(ActorProfileRequestBusiness.PROCESS.equals(arguments.getRepresentationArguments().getActionIdentifier()))				
				arguments.setRepresentation(ActorProfileRequestRepresentation.getProxy());
			
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
			else if(ActorBusiness.CREATE_FROM_PUBLIC.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ActorRepresentation.getProxy());
			
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
			
			else if(ScopeFunctionBusiness.DERIVE_BY_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.CODIFY_BY_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.DELETE_BY_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.DERIVE_HOLDERS_AND_ASSISTANTS_BY_HOLDERS_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.CODIFY_HOLDERS_AND_ASSISTANTS_BY_HOLDERS_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.DELETE_HOLDERS_AND_ASSISTANTS_BY_HOLDERS_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			
			else if(ScopeFunctionBusiness.CREATE_BY_SCOPE_IDENTIFIER_BY_CATEGORY_CODE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			else if(ScopeFunctionBusiness.SAVE_NAME.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeFunctionRepresentation.getProxy());
			
			else if(ScopeTypeFunctionBusiness.SAVE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ScopeTypeFunctionRepresentation.getProxy());
			
			else if(AssignmentsBusiness.INITIALIZE.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.APPLY_MODEL.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.APPLY_MODEL_THEN_EXPORT.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.SAVE_SCOPE_FUNCTIONS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.SAVE_SCOPE_FUNCTIONS_THEN_EXPORT.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.DELETE_ALL.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.DERIVE_ALL_VALUES.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.CLEAN.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.IMPORT.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.IMPORT_NEWS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			else if(AssignmentsBusiness.EXPORT.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(AssignmentsRepresentation.getProxy());
			
			else if(ExecutionImputationBusiness.REFRESH_MATERIALIZED_VIEW.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(ExecutionImputationRepresentation.getProxy());
			
			else if(RequestScopeFunctionBusiness.UPDATE_GRANTED_TO_FALSE_WHERE_TRUE_BY_SCOPE_FUNCTIONS_IDENTIFIERS.equals(arguments.getRepresentationArguments().getActionIdentifier()))
				arguments.setRepresentation(RequestScopeFunctionRepresentation.getProxy());
		}
		
		if(arguments.getRepresentation() == null && StringHelper.isNotBlank(arguments.getRepresentationArguments().getActionIdentifier())) {
			Object proxy = RepresentationProxyGetter.getInstance().getByActionIdentifier(arguments.getRepresentationArguments().getActionIdentifier());
			if(proxy != null)
				arguments.setRepresentation(proxy);
		}
		
		super.prepare(controllerEntityClass, arguments);
	}
	
	@Override
	protected <T> Response save(Object representation, Collection<?> creatables, Collection<?> updatables,Collection<?> deletables, org.cyk.utility.representation.Arguments arguments) {
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
		
		if(arguments != null && ScopeTypeFunctionBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			Collection<ScopeTypeFunctionDto> dtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				for(Object index : creatables)
					dtos.add((ScopeTypeFunctionDto) index);
			if(CollectionHelper.isNotEmpty(updatables))
				for(Object index : updatables)
					dtos.add((ScopeTypeFunctionDto) index);
			if(CollectionHelper.isNotEmpty(deletables))
				for(Object index : deletables)
					dtos.add(((ScopeTypeFunctionDto) index).set__deletable__(Boolean.TRUE));
			return ((ScopeTypeFunctionRepresentation)representation).save(dtos);
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
		
		if(arguments != null && ActorScopeBusiness.VISIBLE.equals(arguments.getActionIdentifier())) {
			ActorScopeDto actorScope = (ActorScopeDto) CollectionHelper.getFirst(creatables);
			return ((ActorScopeRepresentation)representation).visible(actorScope.getActorsIdentifiers(), actorScope.getScopesIdentifiers(),actorScope.getIgnoreExisting()
					, actorScope.getActorAsString());
		}
		
		if(arguments != null && ActorScopeBusiness.UNVISIBLE.equals(arguments.getActionIdentifier())) {
			ActorScopeDto actorScope = (ActorScopeDto) CollectionHelper.getFirst(creatables);
			return ((ActorScopeRepresentation)representation).unvisible(actorScope.getActorsIdentifiers(), actorScope.getScopesIdentifiers(),actorScope.getIgnoreExisting()
					, actorScope.getActorAsString());
		}
		
		if(arguments != null && ActorScopeRequestBusiness.RECORD.equals(arguments.getActionIdentifier())) {
			ActorScopeRequestDto actorScopeRequest = (ActorScopeRequestDto) CollectionHelper.getFirst(creatables);
			return ((ActorScopeRequestRepresentation)representation).record(actorScopeRequest.getActorsIdentifiers(), actorScopeRequest.getScopesIdentifiers()
					, actorScopeRequest.get__auditWho__(),actorScopeRequest.getIgnoreExisting());
		}
		
		if(arguments != null && ActorScopeRequestBusiness.CANCEL.equals(arguments.getActionIdentifier())) {
			ActorScopeRequestDto actorScopeRequest = (ActorScopeRequestDto) CollectionHelper.getFirst(updatables);
			return ((ActorScopeRequestRepresentation)representation).cancel(actorScopeRequest.getActorsIdentifiers()
					, actorScopeRequest.get__auditWho__(),actorScopeRequest.getIgnoreExisting());
		}
		
		if(arguments != null && ActorScopeRequestBusiness.PROCESS.equals(arguments.getActionIdentifier())) {
			ActorScopeRequestDto actorScopeRequest = (ActorScopeRequestDto) CollectionHelper.getFirst(updatables);
			return ((ActorScopeRequestRepresentation)representation).process((List<ActorScopeRequestDto>) CollectionHelper.cast(ActorScopeRequestDto.class, updatables)
					, actorScopeRequest == null ? null : actorScopeRequest.get__auditWho__());
		}
		
		if(arguments != null && ActorProfileRequestBusiness.RECORD.equals(arguments.getActionIdentifier())) {
			ActorProfileRequestDto actorScopeRequest = (ActorProfileRequestDto) CollectionHelper.getFirst(creatables);
			return ((ActorProfileRequestRepresentation)representation).record(actorScopeRequest.getActorsIdentifiers(), actorScopeRequest.getProfilesIdentifiers()
					, actorScopeRequest.get__auditWho__(),actorScopeRequest.getIgnoreExisting());
		}
		
		if(arguments != null && ActorProfileRequestBusiness.CANCEL.equals(arguments.getActionIdentifier())) {
			ActorProfileRequestDto actorScopeRequest = (ActorProfileRequestDto) CollectionHelper.getFirst(updatables);
			return ((ActorProfileRequestRepresentation)representation).cancel(actorScopeRequest.getActorsIdentifiers()
					, actorScopeRequest.get__auditWho__(),actorScopeRequest.getIgnoreExisting());
		}
		
		if(arguments != null && ActorProfileRequestBusiness.PROCESS.equals(arguments.getActionIdentifier())) {
			ActorProfileRequestDto actorScopeRequest = (ActorProfileRequestDto) CollectionHelper.getFirst(updatables);
			return ((ActorProfileRequestRepresentation)representation).process((List<ActorProfileRequestDto>) CollectionHelper.cast(ActorProfileRequestDto.class, updatables)
					, actorScopeRequest == null ? null : actorScopeRequest.get__auditWho__());
		}
		
		if(arguments != null && ActorBusiness.CREATE_SCOPES.equals(arguments.getActionIdentifier()))
			return ((ActorScopeRepresentation)representation).createByActors(CollectionHelper.cast(ActorDto.class, creatables));
		
		if(arguments != null && ActorBusiness.DELETE_SCOPES.equals(arguments.getActionIdentifier()))
			return ((ActorScopeRepresentation)representation).deleteByActors(CollectionHelper.cast(ActorDto.class, deletables));
		
		if(arguments != null && ActorBusiness.CREATE_FROM_PUBLIC.equals(arguments.getActionIdentifier()))
			return ((ActorRepresentation)representation).createByPublic((ActorDto)CollectionHelper.getFirst(creatables));
		
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
		
		if(arguments != null && ProfileBusiness.CREATE.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(creatables))
				throw new RuntimeException("Profiles à créer obligatoire");		
			ProfileDto profile = (ProfileDto) CollectionHelper.getFirst(creatables);
			return ((ProfileRepresentation)representation).create(profile.getCode(), profile.getName(), profile.getTypeIdentifier(), profile.getOrderNumber()
					, profile.getRequestable(), profile.get__auditWho__());
		}
		
		if(arguments != null && ProfileBusiness.UPDATE.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Profiles à mettre à jour obligatoire");		
			ProfileDto profile = (ProfileDto) CollectionHelper.getFirst(updatables);
			return ((ProfileRepresentation)representation).update(profile.getIdentifier(),profile.getCode(), profile.getName(), profile.getTypeIdentifier()
					, profile.getOrderNumber(), profile.getRequestable(), profile.get__auditWho__());
		}
		
		if(arguments != null && ProfileBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(creatables) && CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Profile à enregistrer obligatoire");		
			ProfileDto profile = (ProfileDto) (CollectionHelper.isEmpty(creatables) ? CollectionHelper.getFirst(updatables) : CollectionHelper.getFirst(creatables));
			return ((ProfileRepresentation)representation).save(profile.getIdentifier(),profile.getCode(), profile.getName(), profile.getTypeIdentifier()
					, profile.getOrderNumber(), profile.getRequestable(), profile.get__auditWho__());
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
		
		if(arguments != null && ScopeFunctionBusiness.DERIVE_BY_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier()))
			return ((ScopeFunctionRepresentation)representation).deriveByFunctionsIdentifiers(((ScopeFunctionDto) CollectionHelper.getFirst(creatables)).getFunctionsIdentifiers());
		
		if(arguments != null && ScopeFunctionBusiness.CODIFY_BY_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier()))
			return ((ScopeFunctionRepresentation)representation).codifyByFunctionsIdentifiers(((ScopeFunctionDto) CollectionHelper.getFirst(creatables)).getFunctionsIdentifiers());
		
		if(arguments != null && ScopeFunctionBusiness.DELETE_BY_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier()))
			return ((ScopeFunctionRepresentation)representation).deleteByFunctionsIdentifiers(((ScopeFunctionDto) CollectionHelper.getFirst(creatables)).getFunctionsIdentifiers());
		
		if(arguments != null && ScopeFunctionBusiness.DERIVE_HOLDERS_AND_ASSISTANTS_BY_HOLDERS_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier()))
			return ((ScopeFunctionRepresentation)representation).deriveHoldersAndAssistantsByHoldersFunctionsIdentifiers(((ScopeFunctionDto) CollectionHelper.getFirst(creatables)).getFunctionsIdentifiers());
		
		if(arguments != null && ScopeFunctionBusiness.CODIFY_HOLDERS_AND_ASSISTANTS_BY_HOLDERS_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier()))
			return ((ScopeFunctionRepresentation)representation).codifyHoldersAndAssistantsByHoldersFunctionsIdentifiers(((ScopeFunctionDto) CollectionHelper.getFirst(creatables)).getFunctionsIdentifiers());
		
		if(arguments != null && ScopeFunctionBusiness.DELETE_HOLDERS_AND_ASSISTANTS_BY_HOLDERS_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier()))
			return ((ScopeFunctionRepresentation)representation).deleteHoldersAndAssistantsByHoldersFunctionsIdentifiers(((ScopeFunctionDto) CollectionHelper.getFirst(creatables)).getFunctionsIdentifiers());
		
		if(arguments != null && ScopeFunctionBusiness.CREATE_BY_SCOPE_IDENTIFIER_BY_CATEGORY_CODE.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(creatables))
				throw new RuntimeException("Poste à enregistrer obligatoire");
			ScopeFunctionDto scopeFunctionDto = (ScopeFunctionDto) CollectionHelper.getFirst(creatables);
			return ((ScopeFunctionRepresentation)representation).createByScopeIdentifierByCategoryCode(scopeFunctionDto.getScopeIdentifier()
					, scopeFunctionDto.getCodePrefix(), scopeFunctionDto.getName(), scopeFunctionDto.get__auditWho__());
		}
		
		if(arguments != null && ScopeFunctionBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(creatables) && CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Poste à enregistrer obligatoire");
			Collection<ScopeFunctionDto> scopeFunctionDtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				scopeFunctionDtos.addAll(CollectionHelper.cast(ScopeFunctionDto.class, creatables));
			if(CollectionHelper.isNotEmpty(updatables))
				scopeFunctionDtos.addAll(CollectionHelper.cast(ScopeFunctionDto.class, updatables));
			return ((ScopeFunctionRepresentation)representation).save(scopeFunctionDtos);
		}
		
		if(arguments != null && ScopeFunctionBusiness.SAVE_NAME.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(creatables) && CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Poste à enregistrer obligatoire");
			Collection<ScopeFunctionDto> scopeFunctionDtos = new ArrayList<>();
			if(CollectionHelper.isNotEmpty(creatables))
				scopeFunctionDtos.addAll(CollectionHelper.cast(ScopeFunctionDto.class, creatables));
			if(CollectionHelper.isNotEmpty(updatables))
				scopeFunctionDtos.addAll(CollectionHelper.cast(ScopeFunctionDto.class, updatables));
			scopeFunctionDtos.forEach(dto -> {
				dto.set__auditFunctionality__("Modification de libellé");
			});
			return ((ScopeFunctionRepresentation)representation).save(scopeFunctionDtos);
		}
		
		if(arguments != null && AssignmentsBusiness.INITIALIZE.equals(arguments.getActionIdentifier()))
			return ((AssignmentsRepresentation)representation).initialize(SessionHelper.getUserName());
		if(arguments != null && AssignmentsBusiness.APPLY_MODEL.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Model obligatoire");
			AssignmentsDto assignments = (AssignmentsDto) updatables.iterator().next();
			return ((AssignmentsRepresentation)representation).applyModel(assignments,SessionHelper.getUserName());
		}
		if(arguments != null && AssignmentsBusiness.APPLY_MODEL_THEN_EXPORT.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Model obligatoire");
			AssignmentsDto assignments = (AssignmentsDto) updatables.iterator().next();
			return ((AssignmentsRepresentation)representation).applyModelThenExport(assignments,SessionHelper.getUserName());
		}
		if(arguments != null && AssignmentsBusiness.SAVE_SCOPE_FUNCTIONS.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Affectations obligatoire");
			Collection<AssignmentsDto> collection = CollectionHelper.cast(AssignmentsDto.class, updatables);
			if(CollectionHelper.isEmpty(collection))
				throw new RuntimeException("Affectations obligatoire");
			return ((AssignmentsRepresentation)representation).saveScopeFunctions((List<AssignmentsDto>) collection);
		}
		if(arguments != null && AssignmentsBusiness.SAVE_SCOPE_FUNCTIONS_THEN_EXPORT.equals(arguments.getActionIdentifier())) {
			if(CollectionHelper.isEmpty(updatables))
				throw new RuntimeException("Affectations obligatoire");
			Collection<AssignmentsDto> collection = CollectionHelper.cast(AssignmentsDto.class, updatables);
			if(CollectionHelper.isEmpty(collection))
				throw new RuntimeException("Affectations obligatoire");
			return ((AssignmentsRepresentation)representation).saveScopeFunctionsThenExport((List<AssignmentsDto>) collection);
		}
		if(arguments != null && AssignmentsBusiness.DERIVE_ALL_VALUES.equals(arguments.getActionIdentifier())) {
			AssignmentsDto assignments = (AssignmentsDto) CollectionHelper.getFirst(creatables);
			Boolean overridable = assignments == null ? null : assignments.getOverridable();
			Boolean holdersSettable = assignments == null ? null : assignments.getHoldersSettable();
			Boolean assistantsSettable = assignments == null ? null : assignments.getAssistantsSettable();
			return ((AssignmentsRepresentation)representation).deriveAllValues(holdersSettable,assistantsSettable,overridable,SessionHelper.getUserName());
		}
		//if(arguments != null && AssignmentsBusiness.DELETE_ALL.equals(arguments.getActionIdentifier()))
		//	return ((AssignmentsRepresentation)representation).deleteAll();
		
		if(arguments != null && AssignmentsBusiness.CLEAN.equals(arguments.getActionIdentifier())) {
			//AssignmentsDto assignments = (AssignmentsDto) CollectionHelper.getFirst(creatables);
			return ((AssignmentsRepresentation)representation).clean(SessionHelper.getUserName());
		}
		if(arguments != null && AssignmentsBusiness.IMPORT.equals(arguments.getActionIdentifier()))
			return ((AssignmentsRepresentation)representation).import_(SessionHelper.getUserName());
		
		if(arguments != null && AssignmentsBusiness.IMPORT_NEWS.equals(arguments.getActionIdentifier()))
			return ((AssignmentsRepresentation)representation).importNews(SessionHelper.getUserName());
		
		if(arguments != null && AssignmentsBusiness.EXPORT.equals(arguments.getActionIdentifier()))
			return ((AssignmentsRepresentation)representation).export(SessionHelper.getUserName());
		
		if(arguments != null && RequestBusiness.INITIALIZE.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(creatables);
			return ((RequestRepresentation)representation).initialize(request);
		}
		
		if(arguments != null && RequestBusiness.RECORD.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(updatables);
			return ((RequestRepresentation)representation).record(request);
		}
		
		if(arguments != null && RequestBusiness.RECORD_PHOTO.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(updatables);
			return ((RequestRepresentation)representation).recordPhoto(request);
		}
		
		if(arguments != null && RequestBusiness.SUBMIT.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(updatables);
			return ((RequestRepresentation)representation).submitByIdentifier(request.getIdentifier(),request.getReadPageURL());
		}
		
		if(arguments != null && RequestBusiness.RETURN.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(updatables);
			return ((RequestRepresentation)representation).returnByIdentifier(request.getIdentifier(),request.getReadPageURL());
		}
		
		if(arguments != null && RequestBusiness.ACCEPT.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(updatables);
			//request.set__auditWho__(SessionHelper.getUserName());
			return ((RequestRepresentation)representation).acceptByIdentifier(request.getIdentifier(),request.getBudgetariesScopeFunctionsAsStrings()
					,request.getComment(),request.getReadPageURL(),SessionHelper.getUserName());
		}
		
		if(arguments != null && RequestBusiness.REJECT.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(updatables);
			return ((RequestRepresentation)representation).rejectByIdentifier(request.getIdentifier(), request.getRejectionReason(),request.getReadPageURL()
					,SessionHelper.getUserName());
		}
		
		if(arguments != null && RequestBusiness.NOTIFY_ACCESS_TOKENS.equals(arguments.getActionIdentifier())) {
			RequestDto request = (RequestDto) CollectionHelper.getFirst(creatables);
			return ((RequestRepresentation)representation).notifyAcessTokens(request.getElectronicMailAddress(),request.getReadPageURL());
		}
		
		if(arguments != null && RequestBusiness.EXPORT_FOR_ACCOUNT_CREATION.equals(arguments.getActionIdentifier()))
			return ((RequestRepresentation)representation).exportForAccountCreation(SessionHelper.getUserName());
		
		if(arguments != null && RequestDispatchSlipBusiness.RECORD.equals(arguments.getActionIdentifier())) {
			RequestDispatchSlipDto requestDispatchSlipDto = (RequestDispatchSlipDto) CollectionHelper.getFirst(updatables);
			if(requestDispatchSlipDto == null)
				requestDispatchSlipDto = (RequestDispatchSlipDto) CollectionHelper.getFirst(creatables);
			return ((RequestDispatchSlipRepresentation)representation).record(requestDispatchSlipDto);
		}
		
		if(arguments != null && RequestDispatchSlipBusiness.SEND.equals(arguments.getActionIdentifier())) {
			RequestDispatchSlipDto requestDispatchSlipDto = (RequestDispatchSlipDto) CollectionHelper.getFirst(updatables);
			return ((RequestDispatchSlipRepresentation)representation).send(requestDispatchSlipDto);
		}
		
		if(arguments != null && RequestDispatchSlipBusiness.PROCESS.equals(arguments.getActionIdentifier())) {
			RequestDispatchSlipDto requestDispatchSlipDto = (RequestDispatchSlipDto) CollectionHelper.getFirst(updatables);
			return ((RequestDispatchSlipRepresentation)representation).process(requestDispatchSlipDto);
		}
		
		if(arguments != null && ActorBusiness.SAVE_PROFILE.equals(arguments.getActionIdentifier())) {
			ActorDto actor = (ActorDto) CollectionHelper.getFirst(updatables);
			return ((ActorRepresentation)representation).saveProfile(actor);
		}
		
		if(arguments != null && IdentificationFormAttributeBusiness.SAVE.equals(arguments.getActionIdentifier())) {
			return ((IdentificationFormAttributeRepresentation)representation).save(CollectionHelper.cast(IdentificationFormAttributeDto.class, updatables));
		}
		
		if(arguments != null && RequestScopeFunctionBusiness.UPDATE_GRANTED_TO_FALSE_WHERE_TRUE_BY_SCOPE_FUNCTIONS_IDENTIFIERS.equals(arguments.getActionIdentifier())) {
			RequestScopeFunctionDto requestScopeFunctionDto = (RequestScopeFunctionDto) CollectionHelper.getFirst(creatables);
			return ((RequestScopeFunctionRepresentation)representation).updateGrantedToFalseWhereTrueByScopeFunctionsIdentifiers(
					requestScopeFunctionDto.getScopeFunctionsIdentifiers(),requestScopeFunctionDto.get__auditWho__());
		}
		
		return super.save(representation, creatables, updatables, deletables, arguments);
	}
}