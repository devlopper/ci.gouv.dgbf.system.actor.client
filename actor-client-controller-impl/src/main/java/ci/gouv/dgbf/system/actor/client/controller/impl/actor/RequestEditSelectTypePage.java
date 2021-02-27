package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestEditSelectTypePage extends AbstractPageContainerManagedImpl implements Serializable {

	private Collection<RequestType> types;
	private String parameterName = ParameterName.stringify(RequestType.class);
	
	@Override
	protected void __listenAfterPostConstruct__() {
		super.__listenAfterPostConstruct__();
		types = EntityReader.getInstance().readMany(RequestType.class, RequestTypeQuerier.QUERY_IDENTIFIER_READ_FOR_UI);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		//return "Choix du type de la demande";
		return "Quelle demande voulez-vous faire ?";
	}
	
	public static final String OUTCOME = "requestEditSelectTypeView";
}