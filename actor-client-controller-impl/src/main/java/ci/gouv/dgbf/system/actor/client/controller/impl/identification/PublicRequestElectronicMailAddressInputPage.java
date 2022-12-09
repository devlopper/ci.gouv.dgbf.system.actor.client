package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestEditPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.BudgetCategoryQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class PublicRequestElectronicMailAddressInputPage extends AbstractPageContainerManagedImpl implements IdentificationTheme,Serializable {

	private Form form;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		form = buildForm();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		String typeIdentifier =  WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class));
		RequestType type = EntityReader.getInstance().readOne(RequestType.class, new Arguments<RequestType>()
				.queryIdentifier(RequestTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE).filterByIdentifier(typeIdentifier));
		return RequestEditPage.getWindowTitleValue(Action.CREATE,(Request) form.getEntity(),type, super.__getWindowTitleValue__());
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Request request = new Request();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, request);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(Request.FIELD_BUDGET_CATEGORY,Request.FIELD_ELECTRONIC_MAIL_ADDRESS));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_CONTROLLER_ENTITY_INJECTABLE, Boolean.FALSE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		@Override
		public void act(Form form) {			
			Request request = (Request) form.getEntity();
			Redirector.Arguments arguments =new Redirector.Arguments().outcome(PublicRequestEditPage.OUTCOME)
					.addParameter(Request.FIELD_ELECTRONIC_MAIL_ADDRESS, request.getElectronicMailAddress());
			if(request.getBudgetCategory() != null)
				arguments.addParameter(ParameterName.stringify(BudgetCategory.class), request.getBudgetCategory().getIdentifier());
			Redirector.getInstance().redirect(arguments);
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Request.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Email");
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
			}else if(Request.FIELD_BUDGET_CATEGORY.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.LABEL);
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.TRUE);
				Collection<BudgetCategory> choices = EntityReader.getInstance().readMany(BudgetCategory.class,new Arguments<BudgetCategory>().queryIdentifier(BudgetCategoryQuerier.QUERY_IDENTIFIER_READ_BY_CODES_FOR_UI)
						.filterFieldsValues(BudgetCategoryQuerier.PARAMETER_NAME_CODES, List.of(ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_GENERAL
								, ci.gouv.dgbf.system.actor.server.persistence.entities.BudgetCategory.CODE_EPN)));
				//CollectionHelper.addNullAtFirstIfSizeGreaterThanZero(choices);
				map.put(AbstractInputChoice.FIELD_CHOICES, choices);
			}
			return map;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, "Saisir une nouvelle demande");
			return map;
		}
	}
	
	public static final String OUTCOME = "publicRequestElectronicMailAddressInputView";
}