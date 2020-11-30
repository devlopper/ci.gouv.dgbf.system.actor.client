package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityEditPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributeEditPage extends AbstractEntityEditPageContainerManagedImpl<IdentificationFormAttribute> implements Serializable {

	@Override
	protected Map<Object, Object> __getFormArguments__() {
		Map<Object, Object> arguments = super.__getFormArguments__();
		arguments.put(Form.ConfiguratorImpl.FIELD_LISTENER, new Form.ConfiguratorImpl.Listener.AbstractImpl() {
			@Override
			public Collection<String> getFieldsNames(Form form) {
				return CollectionHelper.listOf(IdentificationFormAttribute.FIELD_FORM,IdentificationFormAttribute.FIELD_ATTRIBUTE
						,IdentificationFormAttribute.FIELD_REQUIRED,IdentificationFormAttribute.FIELD_ORDER_NUMBER);
			}
			
			@Override
			public Map<Object, Object> getInputArguments(Form form, String fieldName) {
				Map<Object, Object> map = super.getInputArguments(form, fieldName);
				if(IdentificationFormAttribute.FIELD_FORM.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Formulaire");
				}else if(IdentificationFormAttribute.FIELD_ATTRIBUTE.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Attribut");
				}else if(IdentificationFormAttribute.FIELD_REQUIRED.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Obligatoire");
				}else if(IdentificationFormAttribute.FIELD_ORDER_NUMBER.equals(fieldName)) {
					map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Numéro d'ordre");
				}
				return map;
			}
		});
		return arguments;
	}
}