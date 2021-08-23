package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfileRequest;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorProfileRequestQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorProfileRequestProcessPage extends AbstractEntityEditPageContainerManagedImpl<ActorProfileRequest> implements Serializable {

	private ActorProfileRequest actorProfileRequest;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		Arguments<ActorProfileRequest> arguments = new Arguments<>();
		arguments.queryIdentifier(ActorProfileRequestQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
			.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
		actorProfileRequest = EntityReader.getInstance().readOne(ActorProfileRequest.class, arguments);
		String grantedAsString = WebController.getInstance().getRequestParameter(ActorProfileRequest.FIELD_GRANTED);
		actorProfileRequest.setGranted(StringHelper.isBlank(grantedAsString) ? null : ValueHelper.convertToBoolean(grantedAsString));
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		//form.getInput(SelectOneRadio.class, ActorProfileRequest.FIELD_GRANTED).enableChangeListener(List.of());
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.UPDATE;
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION,action,Form.FIELD_ENTITY,actorProfileRequest,ActorProfileRequestProcessPage.class,this);
	}
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Collection<String> fieldsNames = new ArrayList<>();
		ActorProfileRequest actorProfileRequest = (ActorProfileRequest) arguments.get(Form.FIELD_ENTITY);
		if(actorProfileRequest.getGranted() == null)
			fieldsNames.addAll(List.of(ActorProfileRequest.FIELD_GRANTED));
		fieldsNames.addAll(List.of(ActorProfileRequest.FIELD_PROCESSING_COMMENT));
		MapHelper.writeByKeyDoNotOverride(arguments, Form.FIELD_ENTITY_CLASS, ActorProfileRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments, Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, fieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener().setPageContainerManaged(MapHelper.readByKey(arguments, ActorProfileRequestProcessPage.class)));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return (actorProfileRequest.getGranted() == null ? "Traitement" : (actorProfileRequest.getGranted() ? "Acceptation" : "Rejet") )+" "
				+ci.gouv.dgbf.system.actor.server.persistence.entities.ActorProfileRequest.LABEL;
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			ActorProfileRequest actorProfileRequest = (ActorProfileRequest) form.getEntity();
			if(!actorProfileRequest.getGranted() && StringHelper.isEmpty(actorProfileRequest.getProcessingComment()))
				throw new RuntimeException("Veuillez saisir un commentaire");
			actorProfileRequest.setActorAsString(SessionManager.getInstance().getUserName());
			Arguments<ActorProfileRequest> arguments = new Arguments<ActorProfileRequest>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(actorProfileRequest);
			arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorProfileRequestBusiness.PROCESS));
			EntitySaver.getInstance().save(ActorProfileRequest.class, arguments);
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {

		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(ActorProfileRequest.FIELD_GRANTED.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Accorder");
				map.put(SelectOneRadio.ConfiguratorImpl.FIELD_CHOICES_ARE_YES_NO_ONLY,Boolean.TRUE);
			}else if(ActorProfileRequest.FIELD_PROCESSING_COMMENT.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Commentaire");
				map.put(AbstractInput.FIELD_REQUIRED, Boolean.FALSE.equals(((ActorProfileRequestProcessPage)pageContainerManaged).getActorProfileRequest().getGranted()));
			}
			return map;
		}
		
		@Override
		public Map<Object,Object> getCommandButtonArguments(Form form,Collection<AbstractInput<?>> inputs) {
			Map<Object,Object> map = super.getCommandButtonArguments(form, inputs);
			MapHelper.writeByKey(map,CommandButton.FIELD_VALUE, "Enregistrer");
			return map;
		}
	}
	
	public static final String OUTCOME = "actorProfileRequestProcessView";
}