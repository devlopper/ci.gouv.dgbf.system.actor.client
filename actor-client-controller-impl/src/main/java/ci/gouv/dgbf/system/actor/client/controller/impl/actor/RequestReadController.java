package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.object.AbstractObject;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestReadController extends AbstractObject implements Serializable {

	private Request request;
	private Layout commandsLayout;
	private Layout textsLayout;
	private Layout filesLayout;

	public RequestReadController(Boolean commandable,String editOutcome,String readOutcome,Request request) {
		if(request == null)
			request = loadRequest();
		if(Boolean.TRUE.equals(commandable))
			commandsLayout = RequestReadPage.buildCommandsLayout(request, editOutcome,readOutcome);
		textsLayout = RequestReadPage.buildTextsLayout(request);
		filesLayout = RequestReadPage.buildFilesLayout(request,readOutcome);
	}
	
	public RequestReadController(String editOutcome,String readOutcome) {
		this(Boolean.TRUE,editOutcome,readOutcome,null);
	}
	
	public static Request loadRequest() {
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue());
		return EntityReader.getInstance().readOne(Request.class, RequestQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI, RequestQuerier.PARAMETER_NAME_IDENTIFIER, identifier);
	}
}