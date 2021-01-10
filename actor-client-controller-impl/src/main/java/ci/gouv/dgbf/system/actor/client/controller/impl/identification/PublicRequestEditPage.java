package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestEditPage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class PublicRequestEditPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements IdentificationTheme,Serializable {

	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		RequestEditPage.redirectIfTypeIsNull(form,"publicRequestSelectTypeView");
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		super.setActionFromRequestParameter();
		action = RequestEditPage.getAction(action);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestEditPage.getWindowTitleValue(form,super.__getWindowTitleValue__());
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm(Form.FIELD_ACTION,action);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		Request request = (Request) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(request == null)
			request = RequestEditPage.getRequestFromParameter((Action) MapHelper.readByKey(arguments, Form.FIELD_ACTION),null);			
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
	public static class FormListener extends RequestEditPage.FormListener {
		
		public FormListener(Request request) {
			super(request);
		}
		
		@Override
		public void redirect(Form form, Object request) {
			if(Action.CREATE.equals(form.getAction())) {
				Redirector.getInstance().redirect(RequestNotifyAfterInitializePage.OUTCOME
						,Map.of(Request.FIELD_ELECTRONIC_MAIL_ADDRESS,List.of( ((Request)form.getEntity()).getElectronicMailAddress())));				
				//Redirector.getInstance().redirect(PublicRequestReadPage.OUTCOME
				//		,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of( ((Request)form.getEntity()).getIdentifier())));
			}else if(Action.UPDATE.equals(form.getAction()))
				Redirector.getInstance().redirect(PublicRequestReadPage.OUTCOME
						,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of( ((Request)form.getEntity()).getIdentifier())));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends RequestEditPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
		}
	
	}
	
	public static final String OUTCOME = "publicRequestEditView";
}