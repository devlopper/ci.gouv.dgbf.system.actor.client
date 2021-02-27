package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.server.business.api.RequestDispatchSlipBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipProcessPage extends AbstractEntityEditPageContainerManagedImpl<RequestDispatchSlip> implements Serializable {
	
	private Collection<SelectItem> actions;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actions = List.of(new SelectItem(ACCEPT, "Accepter"),new SelectItem(REJECT, "Rejeter"),new SelectItem(IGNORE, "Ignorer"));
	}
	
	@Override
	protected Form __buildForm__() {
		return buildForm();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Traitement du bordereau de demandes N° "+((RequestDispatchSlip)form.getEntity()).getCode();
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, RequestDispatchSlip.class);
		RequestDispatchSlip requestDispatchSlip = (RequestDispatchSlip) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		if(requestDispatchSlip == null) {
			requestDispatchSlip = EntityReader.getInstance().readOne(RequestDispatchSlip.class,RequestDispatchSlipQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_PROCESS
				,RequestDispatchSlipQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
			if(CollectionHelper.isNotEmpty(requestDispatchSlip.getRequests())) {
				requestDispatchSlip.getRequests().forEach(request -> {
					request.setTreatment(ACCEPT);
				});
			}
			MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY,requestDispatchSlip);
		}
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(RequestDispatchSlip.FIELD_REQUESTS));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends RequestDispatchSlipEditPage.FormListener {
		
		@Override
		public void act(Form form) {
			String actionIdentifier = getRepresentationActionIdentifier();
			RequestDispatchSlip requestDispatchSlip = (RequestDispatchSlip) form.getEntity();
			requestDispatchSlip.writeIdentifiers(actionIdentifier);
			Arguments<RequestDispatchSlip> arguments = new Arguments<RequestDispatchSlip>().setRepresentationArguments(new org.cyk.utility.representation.Arguments()
					.setActionIdentifier(actionIdentifier)).addCreatablesOrUpdatables(requestDispatchSlip);
			EntitySaver.getInstance().save(RequestDispatchSlip.class,arguments);
		}
		
		@Override
		protected String getRepresentationActionIdentifier() {
			return RequestDispatchSlipBusiness.PROCESS;
		}
		
		@Override
		public Boolean isSubmitButtonShowable(Form form) {
			return Boolean.TRUE;
		}		
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends RequestDispatchSlipEditPage.FormConfiguratorListener {
		
		@Override
		protected String __getRequestsControlTemplate__() {
			return "/private/requestdispatchslip/process/__requests__.xhtml";
		}
	}
	
	/**/
	
	public static final String OUTCOME = "requestDispatchSlipProcessView";
	public static final String ACCEPT = "0";
	public static final String REJECT = "1";
	public static final String IGNORE = "2";
}