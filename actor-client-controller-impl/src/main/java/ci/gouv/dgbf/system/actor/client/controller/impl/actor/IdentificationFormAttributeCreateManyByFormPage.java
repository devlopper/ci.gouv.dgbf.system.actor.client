package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;

import ci.gouv.dgbf.system.actor.client.controller.api.IdentificationFormAttributeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationAttributeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributeCreateManyByFormPage extends AbstractIdentificationFormAttributeCreateOrUpdateOrDeleteManyByFormPage<IdentificationAttribute> implements Serializable {

	@Override
	protected Class<IdentificationAttribute> getAttributeClass() {
		return IdentificationAttribute.class;
	}
	
	@Override
	protected String getAttributeLabel(IdentificationAttribute choice) {
		return choice.getName();
	}
	
	@Override
	protected String getAttributesFieldName() {
		return Data.FIELD_ATTRIBUTES;
	}
	
	@Override
	protected String getAttributesReadQueryIdentifier() {
		return IdentificationAttributeQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATION_DO_NOT_EXIST_BY_FORM_IDENTIFIER_FOR_UI;
	}
	
	@Override
	protected String getAttributesReadQueryParameterNameFormIdentifier() {
		return IdentificationAttributeQuerier.PARAMETER_NAME_FORM_IDENTIFIER;
	}
			
	@Override
	protected Action getAction() {
		return Action.CREATE;
	}
	
	@Override
	protected AbstractFormListener getFormListener() {
		return new FormListener();
	}
	
	@Override
	protected AbstractFormConfiguratorListener getFormConfiguratorListener() {
		return new FormConfiguratorListener();
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractFormListener {
		@Override
		protected Collection<IdentificationFormAttribute> computeFormAttributes(Form form, Data data) {
			if(CollectionHelper.isEmpty(data.getAttributes()))
				return null;
			return data.getAttributes().stream().map(attribute -> new IdentificationFormAttribute(data.getForm(),attribute)).collect(Collectors.toList());
		}
		
		@Override
		protected void act(Form form, Data data, Collection<IdentificationFormAttribute> formAttributes) {
			__inject__(IdentificationFormAttributeController.class).createMany(formAttributes);	
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractFormConfiguratorListener {		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> fieldsNames = super.getFieldsNames(form);
			fieldsNames.add(Data.FIELD_ATTRIBUTES);
			return fieldsNames;
		}
		
		@Override
		protected String getCommandButtonValue(Form form, Collection<AbstractInput<?>> inputs) {
			return "Ajouter";
		}
	}
}