package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class RequestReadPage extends AbstractRequestReadPage implements Serializable {
	
	@Override
	protected Form __buildForm__() {		
		return buildForm(request);
	}

	public static Form buildForm(Request request) {
		Form form = AbstractRequestEditPage.buildForm(Form.FIELD_ACTION,Action.READ,Form.FIELD_ENTITY,request,Form.FIELD_LISTENER,new FormListener(request)
				,Form.ConfiguratorImpl.FIELD_LISTENER,new FormConfiguratorListener(request));
		return form;
	}
	
	public static Form buildForm() {
		return buildForm(AbstractRequestEditPage.getRequestFromParameter(Action.READ));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractRequestReadPage.FormConfiguratorListener {
		
		public FormConfiguratorListener(Request request) {
			super(request);
		}
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> fieldNames = super.getFieldsNames(form);
			if(CollectionHelper.isEmpty(fieldNames))
				return null;
			List<String> collection = new ArrayList<String>(fieldNames);
			collection.addAll(0, List.of(Request.FIELD_ACTOR_CODE,Request.FIELD_ACTOR_NAMES,Request.FIELD_TYPE_AS_STRING));
			return collection;
		}
		
		/**/
		
		public static Collection<String> getFieldsNames(Collection<String> fieldNames) {
			if(CollectionHelper.isEmpty(fieldNames))
				return null;
			List<String> collection = new ArrayList<String>(fieldNames);
			collection.addAll(0, List.of(Request.FIELD_ACTOR_CODE,Request.FIELD_ACTOR_NAMES,Request.FIELD_TYPE_AS_STRING));
			return collection;
		}
	}
}