package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.klass.ClassHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.throwable.ThrowableHelper;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.entities.AbstractActorRequest;
import ci.gouv.dgbf.system.actor.server.business.api.AbstractActorRequestBusiness;

public abstract class AbstractActorRequestControllerImpl<REQUEST extends AbstractActorRequest,REQUESTABLE> extends AbstractControllerEntityImpl<REQUEST> implements AbstractActorRequestController<REQUEST>,Serializable {
	private static final long serialVersionUID = 1L;

	protected Class<REQUEST> requestableClass;
	protected String requestablesFieldName,requestablesIdentifiersFieldName;
	protected String recordActionIdentifier,cancelActionIdentifier,processActionIdentifier;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		requestableClass = (Class<REQUEST>) ClassHelper.getParameterAt(getClass(), 1);
		requestablesFieldName = StringHelper.getVariableNameFrom(requestableClass.getSimpleName())+"s";
		requestablesIdentifiersFieldName = requestablesFieldName+"Identifiers";
	}
	
	@Override
	public Object record(REQUEST request) {
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("request", request);
		request.setActorsIdentifiers(FieldHelper.readSystemIdentifiersAsStrings(request.getActors()));
		request.setIgnoreExisting(Boolean.TRUE);
		request.set__auditWho__(SessionHelper.getUserName());
		Collection<?> requestables = (Collection<?>) FieldHelper.read(request, requestablesFieldName);
		FieldHelper.write(request, requestablesIdentifiersFieldName, FieldHelper.readSystemIdentifiersAsStrings(requestables));
		
		@SuppressWarnings("unchecked")
		Arguments<REQUEST> arguments = new Arguments<REQUEST>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(request);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(recordActionIdentifier));
		EntitySaver.getInstance().save(__entityClass__, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	public Object cancel(REQUEST request) {
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("request", request);
		request.set__auditWho__(SessionManager.getInstance().getUserName());
		
		Arguments<REQUEST> arguments = new Arguments<REQUEST>().setResponseEntityClass(String.class).setUpdatables(List.of(request));
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(cancelActionIdentifier));
		EntitySaver.getInstance().save(__entityClass__, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	public Object process(Collection<REQUEST> requests,String ignoreChoice,String yesChoice) {
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("requests", requests);
		Collection<REQUEST> processables = null;
		for(REQUEST request : requests) {
			if(ignoreChoice.equals(request.getGrantedAsString()))							
				continue;
			request.setGranted(yesChoice.equals(request.getGrantedAsString()));
			AbstractActorRequestBusiness.validate(request.getGranted(), request.getProcessingComment());
			request.set__auditWho__(SessionManager.getInstance().getUserName());
			if(processables == null)
				processables = new ArrayList<>();
			processables.add(request);
		}
		if(CollectionHelper.isEmpty(processables))
			throw new RuntimeException("Vous n'avez fait aucune modification");
		
		Arguments<REQUEST> arguments = new Arguments<REQUEST>().setResponseEntityClass(String.class).setUpdatables(processables);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(processActionIdentifier));
		EntitySaver.getInstance().save(__entityClass__, arguments);
		return arguments.get__responseEntity__();
	}

	@Override
	public Object grant(REQUEST request) {
		ThrowableHelper.throwIllegalArgumentExceptionIfNull("request", request);
		request.setGranted(Boolean.TRUE);
		request.set__auditWho__(SessionManager.getInstance().getUserName());
		@SuppressWarnings("unchecked")
		Arguments<REQUEST> arguments = new Arguments<REQUEST>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(request);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(processActionIdentifier));
		EntitySaver.getInstance().save(__entityClass__, arguments);
		return arguments.get__responseEntity__();
	}
}