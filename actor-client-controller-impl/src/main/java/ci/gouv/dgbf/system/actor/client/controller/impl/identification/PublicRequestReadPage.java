package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestReadController;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestReadPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PublicRequestReadPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private RequestReadController controller;
	private Request request;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		request = RequestReadPage.loadRequest();
		if(request == null || request.getType() == null)
			Redirector.getInstance().redirect(PublicRequestOpenPage.OUTCOME, null);
		super.__listenPostConstruct__();
		if(request != null)
			controller = new RequestReadController(Boolean.TRUE,PublicRequestEditPage.OUTCOME,OUTCOME
			,Boolean.TRUE.equals(request.hasStatusInitialized()) ? PublicRequestUpdatePhotoPage.OUTCOME : null
			,Boolean.TRUE.equals(request.hasStatusInitialized()) ? PublicRequestUpdateActOfAppointmentPage.OUTCOME : null
			,Boolean.TRUE.equals(request.hasStatusInitialized()) ? PublicRequestUpdateSignedRequestSheetPage.OUTCOME : null
			,request
			);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestReadPage.getWindowTitleValue(request, super.__getWindowTitleValue__());
	}
	
	/**/
	
	public static final String OUTCOME = "publicRequestReadView";
}