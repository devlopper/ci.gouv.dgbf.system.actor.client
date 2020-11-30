package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceMany;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceManyCheck;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectManyCheckbox;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;

import ci.gouv.dgbf.system.actor.client.controller.api.IdentificationFormAttributeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationAttributeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationFormQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributeCreateManyPage extends AbstractPageContainerManagedImpl implements Serializable {

	private IdentificationForm identificationForm;
	private SelectManyCheckbox attributesSelectManyCheckbox;
	private Form form;	
	
	@Override
	protected void __listenAfterPostConstruct__() {
		identificationForm = WebController.getInstance().getRequestParameterEntity(IdentificationForm.class);
		super.__listenAfterPostConstruct__();
		Data data = new Data();
		if(identificationForm != null) {
			data.form = identificationForm;
			data.forms = CollectionHelper.listOf(identificationForm);
		}
		form = buildForm(Form.FIELD_ENTITY,data);
		SelectOneCombo formSelectOneCombo = form.getInput(SelectOneCombo.class, Data.FIELD_FORM);
		SelectManyCheckbox attributesSelectManyCheckbox = form.getInput(SelectManyCheckbox.class, Data.FIELD_ATTRIBUTES);
		/*
		formSelectOneCombo.setListener(new AbstractInputChoice.Listener.AbstractImpl<IdentificationForm>() {
			@Override
			public Collection<IdentificationForm> computeChoices(AbstractInputChoice<IdentificationForm> input) {
				return EntityReader.getInstance().readMany(IdentificationForm.class, IdentificationFormQuerier.QUERY_IDENTIFIER_READ_FOR_UI);
			}
		});
		*/
		formSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@Override
			protected void select(AbstractAction action, Object value) {
				attributesSelectManyCheckbox.setChoices(null);
				attributesSelectManyCheckbox.setChoicesInitialized(Boolean.FALSE);
			}
		},List.of(attributesSelectManyCheckbox));
		
		attributesSelectManyCheckbox.setListener(new AbstractInputChoice.Listener.AbstractImpl<IdentificationAttribute>() {
			@Override
			public Collection<IdentificationAttribute> computeChoices(AbstractInputChoice<IdentificationAttribute> input) {
				if(formSelectOneCombo == null || formSelectOneCombo.getValue() == null)
					return null;
				IdentificationForm identificationForm = (IdentificationForm) formSelectOneCombo.getValue();
				return EntityReader.getInstance().readMany(IdentificationAttribute.class, IdentificationAttributeQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATION_DO_NOT_EXIST_BY_FORM_IDENTIFIER_FOR_UI
						,IdentificationAttributeQuerier.PARAMETER_NAME_FORM_IDENTIFIER,identificationForm.getIdentifier());
			}
		});
		
		formSelectOneCombo.selectFirstChoice();
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout d'attribut";
	}
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		Data data = (Data) MapHelper.readByKey(arguments, Form.FIELD_ENTITY);
		Collection<String> inputFieldsNames = new ArrayList<>();
		inputFieldsNames.addAll(List.of(Data.FIELD_FORM,Data.FIELD_ATTRIBUTES));
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Data.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_INPUTS_FIELDS_NAMES, inputFieldsNames);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_LISTENER, new FormConfiguratorListener().setIdentificationForm(data.getForm()));		
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_LISTENER, new FormListener());
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_CONTROLLER_ENTITY_INJECTABLE, Boolean.FALSE);		
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends Form.Listener.AbstractImpl {
		@Override
		public void act(Form form) {
			Data data = (Data) form.getEntity();
			Collection<IdentificationForm> forms = null;
			if(data.form != null) {
				if(forms == null)
					forms = new ArrayList<>();
				forms.add(data.form);
			}
			if(CollectionHelper.isNotEmpty(data.forms)) {
				if(forms == null)
					forms = new ArrayList<>();
				forms.addAll(data.forms);
			}
			if(CollectionHelper.isEmpty(forms))
				throw new RuntimeException("Veuillez sélectionner au moins un formulaire");
			if(CollectionHelper.isEmpty(data.attributes))
				throw new RuntimeException("Veuillez sélectionner au moins un attribut");
			Collection<IdentificationFormAttribute> formAttributes = new ArrayList<>();
			forms.forEach(iForm -> {
				data.attributes.forEach(attribute -> {
					formAttributes.add(new IdentificationFormAttribute().setForm(iForm).setAttribute(attribute));
				});
			});
			__inject__(IdentificationFormAttributeController.class).createMany(formAttributes);		
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		private IdentificationForm identificationForm;
		@Override
		public Map<Object, Object> getInputArguments(Form form, String fieldName) {
			Map<Object, Object> map = super.getInputArguments(form, fieldName);
			if(Data.FIELD_FORMS.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Formulaires");
				map.put(AbstractInputChoice.FIELD_CHOICE_CLASS, IdentificationForm.class);
			}else if(Data.FIELD_FORM.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Formulaire");
				map.put(AbstractInputChoice.FIELD_CHOICE_CLASS, IdentificationForm.class);
				map.put(AbstractInputChoice.FIELD_CHOICES
						, identificationForm == null ? EntityReader.getInstance().readMany(IdentificationForm.class, IdentificationFormQuerier.QUERY_IDENTIFIER_READ_FOR_UI)
								 : List.of(identificationForm));
			}else if(Data.FIELD_ATTRIBUTES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Attributs");
				map.put(AbstractInputChoice.FIELD_CHOICE_CLASS, IdentificationAttribute.class);
			}
			return map;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, "Ajouter");
			return map;
		}
	}
	
	/**/
	
	@Getter @Setter
	public static class Data implements Serializable {
		
		@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo
		private IdentificationForm form;
		
		@Input @InputChoice @InputChoiceMany @InputChoiceManyCheck
		private Collection<IdentificationForm> forms;
		
		@Input @InputChoice @InputChoiceMany @InputChoiceManyCheck
		private Collection<IdentificationAttribute> attributes;
		
		public static final String FIELD_FORM = "form";
		public static final String FIELD_FORMS = "forms";
		public static final String FIELD_ATTRIBUTES = "attributes";
	}
}