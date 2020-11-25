package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.myaccount.RequestInitializePage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
public abstract class AbstractRequestReadPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	protected Request request;
	
	@Override
	protected void __listenPostConstruct__() {
		request = RequestInitializePage.getRequestFromParameter(Action.READ);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.READ;
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(request);
	}

	public static Form buildForm(Request request) {
		Form form = AbstractRequestEditPage.buildForm(Form.FIELD_ACTION,Action.READ,Form.FIELD_ENTITY,request,Form.FIELD_LISTENER,new FormListener(request)
				,Form.ConfiguratorImpl.FIELD_LISTENER,new FormConfiguratorListener(request));
		return form;
	}
	
	public static Form buildForm() {
		return buildForm(AbstractRequestEditPage.getRequestFromParameter(Action.READ));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return request.getType().getForm().getName();
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractRequestEditPage.FormListener {
		
		public FormListener(Request request) {
			super(request);
		}
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.FALSE;
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractRequestEditPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
		}
	}
}