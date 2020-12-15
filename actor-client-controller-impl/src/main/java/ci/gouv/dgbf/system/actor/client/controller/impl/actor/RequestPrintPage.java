package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestPrintPage extends AbstractPageContainerManagedImpl implements Serializable {
	
	private String url;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		url = "http://localhost:8180/api/report/get?identifier=/reports/sigobe/Referentiel/ListeDesCUSB";
		//String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue());
	}
	
	
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Impression d'une dedmande";
	}
}