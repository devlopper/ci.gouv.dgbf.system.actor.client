package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.throwable.ThrowableHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeRequestBusiness;

@ApplicationScoped
public class ActorScopeRequestControllerImpl extends AbstractControllerEntityImpl<ActorScopeRequest> implements ActorScopeRequestController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public Object record(ActorScopeRequest actorScopeRequest) {
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("actorScopeRequest", actorScopeRequest);
		actorScopeRequest.setActorsIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(actorScopeRequest.getActors()));
		actorScopeRequest.setScopesIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(actorScopeRequest.getScopes()));
		actorScopeRequest.setIgnoreExisting(Boolean.TRUE);
		actorScopeRequest.setActorAsString(SessionHelper.getUserName());
		Arguments<ActorScopeRequest> arguments = new Arguments<ActorScopeRequest>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(actorScopeRequest);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorScopeRequestBusiness.RECORD));
		EntitySaver.getInstance().save(ActorScopeRequest.class, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	public Object cancel(ActorScopeRequest actorScopeRequest) {
		if(actorScopeRequest == null)
			throw new RuntimeException("Sélectionner une demande de domaine");
		actorScopeRequest.setActorAsString(SessionManager.getInstance().getUserName());
		actorScopeRequest.setActorsIdentifiers(List.of(actorScopeRequest.getIdentifier()));
		Arguments<ActorScopeRequest> arguments = new Arguments<ActorScopeRequest>().setResponseEntityClass(String.class).setUpdatables(List.of(actorScopeRequest));
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorScopeRequestBusiness.CANCEL));
		EntitySaver.getInstance().save(ActorScopeRequest.class, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	public Object process(Collection<ActorScopeRequest> actorScopeRequests,String ignoreChoice,String yesChoice) {
		if(CollectionHelper.isEmpty(actorScopeRequests))
			throw new RuntimeException("Aucune demandes de domaines à traiter");
		Collection<ActorScopeRequest> processables = null;
		for(ActorScopeRequest actorScopeRequest : actorScopeRequests) {
			if(ignoreChoice.equals(actorScopeRequest.getGrantedAsString()))							
				continue;
			actorScopeRequest.setGranted(yesChoice.equals(actorScopeRequest.getGrantedAsString()));
			ActorScopeRequestBusiness.validate(actorScopeRequest.getGranted(), actorScopeRequest.getProcessingComment());
			if(processables == null)
				processables = new ArrayList<>();
			processables.add(actorScopeRequest);
		}
		if(CollectionHelper.isEmpty(processables))
			throw new RuntimeException("Vous n'avez fait aucune modification");
		Arguments<ActorScopeRequest> arguments = new Arguments<ActorScopeRequest>().setResponseEntityClass(String.class).setUpdatables(processables);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorScopeRequestBusiness.PROCESS));
		EntitySaver.getInstance().save(ActorScopeRequest.class, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	public Object processOne(ActorScopeRequest actorScopeRequest) {
		if(actorScopeRequest == null)
			throw new RuntimeException("Sélectionner une demande de domaine");
		actorScopeRequest.setGranted(Boolean.TRUE);
		actorScopeRequest.setActorAsString(SessionManager.getInstance().getUserName());
		Arguments<ActorScopeRequest> arguments = new Arguments<ActorScopeRequest>().setResponseEntityClass(String.class)
				.addCreatablesOrUpdatables(actorScopeRequest);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorScopeRequestBusiness.PROCESS));
		EntitySaver.getInstance().save(ActorScopeRequest.class, arguments);
		return arguments.get__responseEntity__();
	}
}