package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestEditPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestEditSignatureSpecimenInformationsPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class PublicRequestEditSignatureSpecimenInformationsPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements IdentificationTheme,Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		RequestEditSignatureSpecimenInformationsPage.postConstruct(form);
	}

	@Override
	protected void setActionFromRequestParameter() {
		super.setActionFromRequestParameter();
		action = RequestEditSignatureSpecimenInformationsPage.getAction(action);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestEditSignatureSpecimenInformationsPage.getWindowTitleValue(form,super.__getWindowTitleValue__());
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,Action.UPDATE);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		arguments.put(Form.FIELD_ACTION, Action.UPDATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		Request request = (Request) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(request == null)
			request = RequestEditPage.getRequestFromParameter(Action.UPDATE,null);			
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener(request));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener(request));
		
		Form form = RequestEditPage.buildForm(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends RequestEditSignatureSpecimenInformationsPage.FormListener {
		
		public FormListener(Request request) {
			super(request);
		}
		
		@Override
		public void redirect(Form form, Object request) {
			if(Action.UPDATE.equals(form.getAction()))
				Redirector.getInstance().redirect(new Redirector.Arguments().outcome(PublicRequestScopeFunctionListPage.OUTCOME));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends RequestEditSignatureSpecimenInformationsPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
		}
	
	}
	
	public static final String OUTCOME = "publicRequestEditSignatureSpecimenInformationsView";
}