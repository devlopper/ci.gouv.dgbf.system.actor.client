package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputFile;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.FileUpload;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public abstract class AbstractRequestUpdateFilePage extends AbstractEntityEditPageContainerManagedImpl<Request> implements Serializable {

	protected Data data;
	
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
	protected String __getWindowTitleValue__() {
		return String.format("Mise à jour de %s de "+data.getRequest().getFirstName(),getFileType());
	}
	
	protected abstract String getFileType();
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Data.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_CONTROLLER_ENTITY_INJECTABLE, Boolean.FALSE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, List.of(Data.FIELD_FILE));
		Form form = Form.build(arguments);
		form.getInput(FileUpload.class, Data.FIELD_FILE).setListener(new FileUploadListenerImpl(((Data)form.getEntity()).getRequest(),(String)arguments.get(Data.FIELD_FILE)
				,form.getSubmitCommandButton() ));
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static abstract class AbstractFormListener extends Form.Listener.AbstractImpl {
		
		@Override
		public void act(Form form) {
			Data data = (Data) form.getEntity();
			act(form, data.getRequest());
		}
		
		protected abstract void act(Form form,Request request);
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static abstract class AbstractFormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		
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
		private String fieldName;
		private CommandButton commandButton;
		
		public FileUploadListenerImpl(Request request,String fieldName,CommandButton commandButton) {
			this.request = request;
			this.fieldName = fieldName;
			this.commandButton = commandButton;
			if(this.commandButton != null)
				this.commandButton.setRendered(Boolean.FALSE);
		}
		
		@Override
		protected void listenFileUploadedNotEmpty(FileUploadEvent event, byte[] bytes) {
			super.listenFileUploadedNotEmpty(event, bytes);
			FieldHelper.write(request, fieldName, bytes);
			if(commandButton != null)
				commandButton.act(null);
		}		
	}
	
}