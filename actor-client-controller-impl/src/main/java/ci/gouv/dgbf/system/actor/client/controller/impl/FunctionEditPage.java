package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.FunctionType;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class FunctionEditPage extends AbstractEntityEditPageContainerManagedImpl<Function> implements Serializable {

	private FunctionType functionType;
	
	@Override
	protected void __listenPostConstruct__() {
		functionType = WebController.getInstance().getRequestParameterEntityAsParent(FunctionType.class);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(Function.FIELD_CODE,Function.FIELD_NAME);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> map = super.getInputArguments(form, fieldName);
				if(Function.FIELD_CODE.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Code");
				}else if(Function.FIELD_NAME.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Libellé");
				}
				return map;
			}		
		});
		arguments.put(Form.FIELD_LISTENER, new Form.Listener.AbstractImpl() {
			@Override
			public void act(Form form) {
				if(Action.CREATE.equals(action))
					((Function)form.getEntity()).setType(functionType);
				super.act(form);
			}
		});
		return arguments;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return super.__getWindowTitleValue__()+(functionType == null /*|| !Action.CREATE.equals(action)*/ ? ConstantEmpty.STRING : " de type <<"+functionType.getName()+">>");
	}
}