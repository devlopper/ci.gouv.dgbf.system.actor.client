package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.server.representation.api.RequestRepresentation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestUpdatePhotoPage extends AbstractRequestUpdateFilePage implements Serializable {

	@Override
	protected String getFileType() {
		return "photo";
	}
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(Form.FIELD_ACTION,Action.UPDATE,Form.FIELD_ENTITY,data);
	}
		
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		MapHelper.writeByKeyDoNotOverride(arguments,AbstractRequestUpdateFilePage.Data.FIELD_FILE, Request.FIELD_PHOTO);
		Form form = AbstractRequestUpdateFilePage.buildForm(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractRequestUpdateFilePage.AbstractFormListener {
		
		@Override
		protected void act(Form form, Request request) {
			RequestRepresentation.getProxy().recordPhotoByIdentifier(request.getIdentifier(), request.getPhoto());	
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractRequestUpdateFilePage.AbstractFormConfiguratorListener {
		
	}
	
	public static final String OUTCOME = "requestUpdatePhotoView";
}