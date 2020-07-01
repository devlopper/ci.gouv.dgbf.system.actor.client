package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeTypeEditPage extends AbstractEntityEditPageContainerManagedImpl<ScopeType> implements Serializable {

	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(ScopeType.FIELD_CODE,ScopeType.FIELD_NAME);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> map = super.getInputArguments(form, fieldName);
				if(ScopeType.FIELD_CODE.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Code");
				}else if(ScopeType.FIELD_NAME.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Libellé");
				}
				return map;
			}
		});
		return arguments;
	}
}