package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputTextarea;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeRequestQuerier;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Named @ViewScoped
public class ActorScopeRequestRejectOnePage extends AbstractPageContainerManagedImpl implements Serializable {

	private ActorScopeRequest actorScopeRequest;
	private InputTextarea processingCommentInputTextarea;
	private CommandButton saveCommandButton;
	private Layout layout;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		actorScopeRequest = EntityReader.getInstance().readOneBySystemIdentifierAsParent(ActorScopeRequest.class, new Arguments<ActorScopeRequest>()
				.queryIdentifier(ActorScopeRequestQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.ActorScopeRequest.FIELDS_ACTOR_AS_STRING_SCOPE_AS_STRING_GRANTED_AND_GRANTED_AS_STRING)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER.getValue())));
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		processingCommentInputTextarea = buildProcessingCommentInputTextarea();
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
				,CommandButton.ConfiguratorImpl.FIELD_INPUTS,List.of(processingCommentInputTextarea)
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						actorScopeRequest.setGranted(Boolean.FALSE);
						actorScopeRequest.setProcessingComment(processingCommentInputTextarea.getValue());
						return save(actorScopeRequest);
					}
				});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,processingCommentInputTextarea.getOutputLabel(),Cell.FIELD_WIDTH,1)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,processingCommentInputTextarea,Cell.FIELD_WIDTH,11)

				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			));
	}
	
	public static Object save(ActorScopeRequest actorScopeRequest) {
		actorScopeRequest.setActorAsString(SessionHelper.getUserName());
		Arguments<ActorScopeRequest> arguments = new Arguments<ActorScopeRequest>().setResponseEntityClass(String.class).addCreatablesOrUpdatables(actorScopeRequest);
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorScopeRequestBusiness.PROCESS));
		EntitySaver.getInstance().save(ActorScopeRequest.class, arguments);
		return arguments.get__responseEntity__();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Rejet d'une demande de domaine | "+actorScopeRequest;
	}
	
	/**/
	
	private InputTextarea buildProcessingCommentInputTextarea() {
		InputTextarea input = InputTextarea.build(InputTextarea.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Motif",InputTextarea.FIELD_REQUIRED,Boolean.TRUE);		
		return input;
	}
	
	public static final String OUTCOME = "actorScopeRequestRejectOneView";
}