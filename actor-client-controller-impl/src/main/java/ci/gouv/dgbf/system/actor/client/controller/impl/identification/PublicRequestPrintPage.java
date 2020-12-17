package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestPrintPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PublicRequestPrintPage extends AbstractPageContainerManagedImpl implements IdentificationTheme, Serializable {
	
	private Request request;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		request = RequestPrintPage.loadRequest();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestPrintPage.getWindowTitleValue(super.__getWindowTitleValue__());
	}
	
	public static final String OUTCOME = "publicRequestPrintView";
}