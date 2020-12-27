package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.GraphicImage;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestReadPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PublicRequestReadPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private Request request;
	private Layout layout;
	private Layout commandsLayout;
	private Layout textsLayout;
	private Layout filesLayout;
	private Collection<GraphicImage> files;
	
	@Override
	protected void __listenPostConstruct__() {
		request = RequestReadPage.loadRequest();
		if(request == null || request.getType() == null)
			Redirector.getInstance().redirect(PublicRequestOpenPage.OUTCOME, null);
		super.__listenPostConstruct__();
		commandsLayout = RequestReadPage.buildCommandsLayout(request, PublicRequestEditPage.OUTCOME,OUTCOME);
		textsLayout = RequestReadPage.buildTextsLayout(request);
		filesLayout = RequestReadPage.buildFilesLayout(request,OUTCOME);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestReadPage.getWindowTitleValue(request, super.__getWindowTitleValue__());
	}
	
	/**/
	
	public static final String OUTCOME = "publicRequestReadView";
}