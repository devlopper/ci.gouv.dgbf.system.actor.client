package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestPrintPage extends AbstractPageContainerManagedImpl implements Serializable {
	
	private Request request;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		request = loadRequest();
	}
	
	public static Request loadRequest() {
		return RequestReadPage.loadRequest();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return getWindowTitleValue(super.__getWindowTitleValue__());
	}
	
	/**/
	
	public static String getWindowTitleValue(String default_) {
		return "Impression d'une demande";
	}
}