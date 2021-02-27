package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.controller.EntityReader;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInput;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectManyCheckbox;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationAttribute;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.IdentificationFormAttributeListPage.LazyDataModelListenerImpl;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationFormQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
public abstract class AbstractIdentificationFormAttributeCreateOrUpdateOrDeleteManyByFormPage<ATTRIBUTE> extends AbstractPageContainerManagedImpl implements Serializable {

	protected Form form;
	protected DataTable attributesDataTable;
	protected IdentificationForm identificationForm;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void __listenAfterPostConstruct__() {
		identificationForm = WebController.getInstance().getRequestParameterEntity(IdentificationForm.class);
		if(identificationForm == null)
			identificationForm = WebController.getInstance().getRequestParameterEntityAsParent(IdentificationForm.class);
		super.__listenAfterPostConstruct__();
		Data data = new Data();
		if(identificationForm != null) {
			data.form = identificationForm;
			data.forms = CollectionHelper.listOf(identificationForm);
		}
		form = __buidForm__(data);
		SelectOneCombo formSelectOneCombo = form.getInput(SelectOneCombo.class, Data.FIELD_FORM);
		if(attributesDataTable == null) {
			SelectManyCheckbox attributesSelectManyCheckbox = form.getInput(SelectManyCheckbox.class, getAttributesFieldName());
			if(identificationForm == null) {
				formSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
					@Override
					protected void select(AbstractAction action, Object value) {
						attributesSelectManyCheckbox.setChoices(null);
						attributesSelectManyCheckbox.setChoicesInitialized(Boolean.FALSE);
					}
				},List.of(attributesSelectManyCheckbox));
				
				attributesSelectManyCheckbox.setListener(new AbstractInputChoice.Listener.AbstractImpl<ATTRIBUTE>() {
					@Override
					public Collection<ATTRIBUTE> computeChoices(AbstractInputChoice<ATTRIBUTE> input) {
						if(formSelectOneCombo == null || formSelectOneCombo.getValue() == null)
							return null;
						IdentificationForm identificationForm = (IdentificationForm) formSelectOneCombo.getValue();
						return EntityReader.getInstance().readMany(getAttributeClass(), getAttributesReadQueryIdentifier()
								,getAttributesReadQueryParameterNameFormIdentifier(),identificationForm.getIdentifier());
					}
					
					@Override
					public Object getChoiceLabel(AbstractInputChoice<ATTRIBUTE> input, ATTRIBUTE choice) {
						return getAttributeLabel(choice);
					}
				});
			}else {
				attributesSelectManyCheckbox.setChoices((Collection<Object>) EntityReader.getInstance().readMany(getAttributeClass(), getAttributesReadQueryIdentifier()
						,getAttributesReadQueryParameterNameFormIdentifier(),identificationForm.getIdentifier()));
				attributesSelectManyCheckbox.setChoicesInitialized(Boolean.TRUE);
				
				attributesSelectManyCheckbox.setListener(new AbstractInputChoice.Listener.AbstractImpl<ATTRIBUTE>() {
					@Override
					public Object getChoiceLabel(AbstractInputChoice<ATTRIBUTE> input, ATTRIBUTE choice) {
						return getAttributeLabel(choice);
					}
				});
			}			
		}else {
			SelectManyCheckbox attributesSelectManyCheckbox = form.getInput(SelectManyCheckbox.class, getAttributesFieldName());
			attributesSelectManyCheckbox.setChoicesInitialized(Boolean.TRUE);
			
			if(formSelectOneCombo == null) {
				setAttributesDataTableFormIdentifier(attributesDataTable, identificationForm);
			}else {
				formSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
					@Override
					protected void select(AbstractAction action, Object value) {
						if(value == null)
							return;
						setAttributesDataTableFormIdentifier(attributesDataTable, (IdentificationForm)value);
					}
				},List.of(attributesDataTable));	
			}			
		}
		if(formSelectOneCombo != null)
			formSelectOneCombo.selectFirstChoice();
		if(attributesDataTable != null && formSelectOneCombo != null)
			setAttributesDataTableFormIdentifier(attributesDataTable, (IdentificationForm) formSelectOneCombo.getValue());
	}
	
	public static void setAttributesDataTableFormIdentifier(DataTable dataTable,IdentificationForm identificationForm) {
		@SuppressWarnings("unchecked")
		LazyDataModel<IdentificationFormAttribute> lazyDataModel = (LazyDataModel<IdentificationFormAttribute>)dataTable.getValue();
		IdentificationFormAttributeListPage.LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) lazyDataModel.getListener();
		listener.setFormIdentifier(identificationForm.getIdentifier());
	}
	
	protected Form __buidForm__(Data data) {
		return buildForm(Form.FIELD_ENTITY,data,Form.ConfiguratorImpl.FIELD_LISTENER,getFormConfiguratorListener().setAction(getAction()).setIdentificationForm(data.getForm())
				,Form.FIELD_LISTENER,getFormListener());
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return (Action.CREATE.equals(getAction()) ? "Ajout" : (Action.UPDATE.equals(getAction()) ? "Modification" : "Suppression"))
				+" d'attributs"+(identificationForm == null ? ConstantEmpty.STRING : " du formulaire "+identificationForm.getName());
	}
	
	protected abstract Action getAction();
	protected abstract AbstractFormConfiguratorListener getFormConfiguratorListener();
	protected abstract AbstractFormListener getFormListener();
	
	protected abstract Class<ATTRIBUTE> getAttributeClass();
	protected abstract String getAttributeLabel(ATTRIBUTE choice);
	
	protected abstract String getAttributesFieldName();	
	protected abstract String getAttributesReadQueryIdentifier();
	protected abstract String getAttributesReadQueryParameterNameFormIdentifier();
	
	/**/
	
	public static Form buildForm(Map<Object, Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ENTITY_CLASS, Data.class);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.FIELD_ACTION, Action.CREATE);
		MapHelper.writeByKeyDoNotOverride(arguments,Form.ConfiguratorImpl.FIELD_CONTROLLER_ENTITY_INJECTABLE, Boolean.FALSE);		
		Form form = Form.build(arguments);
		return form;
	}
	
	public static Form buildForm(Object...objects) {
		return buildForm(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static abstract class AbstractFormListener extends Form.Listener.AbstractImpl {
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
			Collection<IdentificationFormAttribute> formAttributes = computeFormAttributes(form,data);
			if(CollectionHelper.isEmpty(formAttributes))
				throw new RuntimeException(getRequiredAttributeMessage());			
			act(form,data, formAttributes);		
		}
		
		protected abstract Collection<IdentificationFormAttribute> computeFormAttributes(Form form,Data data);
		protected String getRequiredAttributeMessage() {
			return "Veuillez sélectionner au moins un attribut";
		}
		protected abstract void act(Form form,Data data,Collection<IdentificationFormAttribute> formAttributes);
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static abstract class AbstractFormConfiguratorListener extends Form.ConfiguratorImpl.Listener.AbstractImpl {
		protected IdentificationForm identificationForm;
		protected Action action;
		
		@Override
		public Collection<String> getFieldsNames(Form form) {
			Collection<String> fieldsNames = null;
			if(identificationForm == null) {
				if(fieldsNames == null)
					fieldsNames = new ArrayList<>();
				fieldsNames.add(Data.FIELD_FORM);
			}
			if(Action.CREATE.equals(action) || Action.UPDATE.equals(action) || Action.DELETE.equals(action)) {
				if(fieldsNames == null)
					fieldsNames = new ArrayList<>();
				if(Action.CREATE.equals(action))
					fieldsNames.add(Data.FIELD_ATTRIBUTES);
				else
					fieldsNames.add(Data.FIELD_FORM_ATTRIBUTES);
			}
			return fieldsNames;
		}
		
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
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_BUILDABLE, Boolean.FALSE);
			}else if(Data.FIELD_FORM_ATTRIBUTES.equals(fieldName)) {
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE, "Attributs");
				map.put(AbstractInputChoice.FIELD_CHOICE_CLASS, IdentificationFormAttribute.class);
				map.put(AbstractInput.AbstractConfiguratorImpl.FIELD_OUTPUT_LABEL_BUILDABLE, Boolean.FALSE);
			}
			return map;
		}
		
		@Override
		public Map<Object, Object> getCommandButtonArguments(Form form, Collection<AbstractInput<?>> inputs) {
			Map<Object, Object> map = super.getCommandButtonArguments(form, inputs);
			map.put(CommandButton.FIELD_VALUE, getCommandButtonValue(form, inputs));
			return map;
		}
		
		protected String getCommandButtonValue(Form form, Collection<AbstractInput<?>> inputs) {
			if(Action.CREATE.equals(action))
				return "Ajouter";
			if(Action.UPDATE.equals(action))
				return "Modifier";
			if(Action.DELETE.equals(action))
				return "Supprimer";
			return "Exécuter";
		}
	
		@Override
		public Map<Object, Object> getInputCellArguments(Form form, AbstractInput<?> input) {
			Map<Object, Object> map = super.getInputCellArguments(form, input);
			if(!input.getField().getName().equals(Data.FIELD_FORM))
				map.put(Cell.FIELD_WIDTH, 12);
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
		
		@Input @InputChoice @InputChoiceMany @InputChoiceManyCheck
		private Collection<IdentificationFormAttribute> formAttributes;
		
		public static final String FIELD_FORM = "form";
		public static final String FIELD_FORMS = "forms";
		public static final String FIELD_ATTRIBUTES = "attributes";
		public static final String FIELD_FORM_ATTRIBUTES = "formAttributes";
	}
}