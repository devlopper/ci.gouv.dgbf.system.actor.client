package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputFile;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.FileUpload;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.business.api.RequestBusiness;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestRepresentation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestUpdatePhotoPage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	private Data data;
	
	@Override
	protected void __listenPostConstruct__() {
		data = new Data(RequestEditPage.getRequestFromParameter(Action.UPDATE, null));
		super.__listenPostConstruct__();
	}
	
	@Override
	protected void setActionFromRequestParameter() {
		action = Action.UPDATE;
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION,Action.UPDATE,Form.FIELD_ENTITY,data);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Mise à jour de photo de "+data.getRequest().getFirstName();
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Data.class);
		//Request request = RequestEditPage.getRequestFromParameter((Action) arguments.get(Form.FIELD_ACTION), null);
		//MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY, new Data(request));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_CONTROLLER_ENTITY_INJECTABLE, Boolean.FALSE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(Data.FIELD_FILE));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		Form form = Form.build(arguments);
		form.getInput(FileUpload.class, Data.FIELD_FILE).setListener(new FileUploadListenerImpl( ((Data)form.getEntity()).getRequest(),form.getSubmitCommandButton() ));
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			Data data = (Data) form.getEntity();
			RequestRepresentation.getProxy().recordPhotoByIdentifier(data.getRequest().getIdentifier(), data.getRequest().getPhoto());	
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		
	}
	
	@Getter @Setter
	public static class Data implements Serializable {
		
		private Request request;
		
		public Data(Request request) {
			this.request = request;
		}
		
		@Input @InputFile
		@NotNull
		private UploadedFile file;
		
		public static final String FIELD_FILE = "file";
	}
	
	public static class FileUploadListenerImpl extends FileUpload.Listener.AbstractImpl implements Serializable {
		private Request request;
		private CommandButton commandButton;
		
		public FileUploadListenerImpl(Request request,CommandButton commandButton) {
			this.request = request;
			this.commandButton = commandButton;
			if(this.commandButton != null)
				this.commandButton.setRendered(Boolean.FALSE);
		}
		
		@Override
		protected void listenFileUploadedNotEmpty(FileUploadEvent event, byte[] bytes) {
			super.listenFileUploadedNotEmpty(event, bytes);
			request.setPhoto(bytes);
			if(commandButton != null)
				commandButton.act(null);
			//EntitySaver.getInstance().save(Request.class, new Arguments<Request>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
			//			.setActionIdentifier(RequestBusiness.RECORD_PHOTO)).addCreatablesOrUpdatables(request));
			//RequestRepresentation.getProxy().recordPhotoByIdentifier(request.getIdentifier(), bytes);
			//PrimeFaces.current().dialog().closeDynamic(null);
		}
		
	}
	
	public static final String OUTCOME = "requestUpdatePhotoView";
}