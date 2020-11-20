package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputTextarea;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribut;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestEditPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	private RequestType requestType;
	private Request request;
	private Map<String,IdentificationAttribut> fieldsNames;
	
	@Override
	protected void __listenPostConstruct__() {
		String requestTypeIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(RequestType.class));
		if(StringHelper.isNotBlank(requestTypeIdentifier))
			requestType = EntityReader.getInstance().readOne(RequestType.class,RequestTypeQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_REQUEST_CREATION
				, RequestTypeQuerier.PARAMETER_NAME_IDENTIFIER, requestTypeIdentifier);
		if(requestType == null) {
			fieldsNames = null;//List.of(Request.FIELD_ADMINISTRATIVE_UNIT);
		}else {
			
		}
		//WebController.getInstance().getRequestParameterEntityAsParent(RequestType.class);		
		super.__listenPostConstruct__();
		
	}
	
	@Override
	protected Form __buildForm__() {
		Form form = buildForm(Form.FIELD_ACTION,action,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener().setFieldsNames(fieldsNames));
		return form;
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return "Création d'une demande"+(requestType == null ? ConstantEmpty.STRING : " : "+requestType.getName());
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Request.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		private Map<String,IdentificationAttribut> fieldsNames;
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			return fieldsNames == null ? null : fieldsNames.keySet();
		}
		
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			IdentificationAttribut attribut = fieldsNames == null ? null : fieldsNames.get(fieldName);
			if(attribut != null) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, attribut.getName());
				map.put(AbstractInput.FIELD_REQUIRED, attribut.getRequired());
			}
			if(Request.FIELD_FUNCTIONS.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<Function>() {
					@Override
					public Collection<Function> computeChoices(AbstractInputChoice<Function> input) {
						Collection<Function> functions = EntityReader.getInstance().readMany(Function.class,FunctionQuerier.QUERY_IDENTIFIER_READ);
						return functions;
					}
				});
			}else if(Request.FIELD_ACTOR.equals(fieldName)) {
				
			}else if(Request.FIELD_COMMENT.equals(fieldName)) {
				map.put(InputTextarea.FIELD_REQUIRED, Boolean.TRUE);
			}else if(Request.FIELD_TYPE.equals(fieldName)) {
				map.put(AbstractInputChoice.FIELD_LISTENER, new AbstractInputChoice.Listener.AbstractImpl<RequestType>() {
					@Override
					public Collection<RequestType> computeChoices(AbstractInputChoice<RequestType> input) {
						Collection<RequestType> types = EntityReader.getInstance().readMany(RequestType.class,RequestTypeQuerier.QUERY_IDENTIFIER_READ_ALL);
						return types;
					}
				});
			}
			return map;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKeyDoNotOverride(map,CommandButton.FIELD_VALUE, "Créer");
			return map;
		}
	}
	
}