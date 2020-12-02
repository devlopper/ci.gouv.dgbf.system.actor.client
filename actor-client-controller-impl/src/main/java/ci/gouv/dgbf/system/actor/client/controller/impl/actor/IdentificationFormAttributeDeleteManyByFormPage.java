package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;

import ci.gouv.dgbf.system.actor.client.controller.api.IdentificationFormAttributeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationFormAttributeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributeDeleteManyByFormPage extends AbstractIdentificationFormAttributeCreateOrUpdateOrDeleteManyByFormPage<IdentificationFormAttribute> implements Serializable {

	@Override
	protected Class<IdentificationFormAttribute> getAttributeClass() {
		return IdentificationFormAttribute.class;
	}
	
	@Override
	protected String getAttributesFieldName() {
		return Data.FIELD_FORM_ATTRIBUTES;
	}
	
	@Override
	protected String getAttributesReadQueryIdentifier() {
		return IdentificationFormAttributeQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI;
	}
	
	@Override
	protected String getAttributesReadQueryParameterNameFormIdentifier() {
		return IdentificationFormAttributeQuerier.PARAMETER_NAME_FORM_IDENTIFIER;
	}
	
	@Override
	protected String getAttributeLabel(IdentificationFormAttribute choice) {
		if(choice == null)
			return null;
		return choice.getAttributeAsString();
	}
	
	@Override
	protected Action getAction() {
		return Action.DELETE;
	}
	
	@Override
	protected AbstractFormConfiguratorListener getFormConfiguratorListener() {
		return new FormConfiguratorListener();
	}
	
	@Override
	protected AbstractFormListener getFormListener() {
		return new FormListener();
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractFormListener {
		@Override
		protected Collection<IdentificationFormAttribute> computeFormAttributes(Form form,Data data) {
			return data.getFormAttributes();
		}
		
		@Override
		protected void act(Form form,Data data, Collection<IdentificationFormAttribute> formAttributes) {
			__inject__(IdentificationFormAttributeController.class).deleteMany(formAttributes);
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractFormConfiguratorListener {
		
	}
}