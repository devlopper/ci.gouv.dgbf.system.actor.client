package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PublicRequestReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Request request;
	private Layout layout;

	@Override
	protected void __listenPostConstruct__() {
		request = WebController.getInstance().getRequestParameterEntity(Request.class);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(request == null)
			return super.__getWindowTitleValue__();
		return request.getType().getName();
	}
	
	/**/
	
	public static final String OUTCOME = "publicRequestReadView";
}