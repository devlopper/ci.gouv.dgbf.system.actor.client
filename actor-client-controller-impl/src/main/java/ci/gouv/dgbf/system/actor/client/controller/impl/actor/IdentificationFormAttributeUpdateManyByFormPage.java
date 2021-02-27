package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputNumber;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import ci.gouv.dgbf.system.actor.server.business.api.IdentificationFormAttributeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationFormAttributeQuerier;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributeUpdateManyByFormPage extends AbstractIdentificationFormAttributeCreateOrUpdateOrDeleteManyByFormPage<IdentificationFormAttribute> implements Serializable {

	private SelectBooleanButton requiredSelectBooleanButton;
	private InputNumber orderNumberInputNumber;
	
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
		return IdentificationFormAttributeQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_EDIT;
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
		return Action.UPDATE;
	}
	
	@Override
	protected AbstractFormConfiguratorListener getFormConfiguratorListener() {
		requiredSelectBooleanButton = SelectBooleanButton.build();
		requiredSelectBooleanButton.setBindingByDerivation("identificationFormAttributeUpdateManyPage.requiredSelectBooleanButton", "record."+IdentificationFormAttribute.FIELD_REQUIRED);
		orderNumberInputNumber = InputNumber.build(InputNumber.FIELD_DECIMAL_PLACES,0);
		orderNumberInputNumber.setBindingByDerivation("identificationFormAttributeUpdateManyPage.orderNumberInputNumber", "record."+IdentificationFormAttribute.FIELD_ORDER_NUMBER);
		return new FormConfiguratorListener().setDataTable(attributesDataTable = IdentificationFormAttributeListPage
				.buildDataTable(DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,CollectionHelper.listOf(IdentificationFormAttribute.FIELD_ATTRIBUTE
						,IdentificationFormAttribute.FIELD_REQUIRED,IdentificationFormAttribute.FIELD_ORDER_NUMBER)
						,DataTable.FIELD_LISTENER,new AttributesDataTableListenerImpl()
						,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new AttributesLazyDataModelListenerImpl()
						,DataTable.FIELD_RENDER_TYPE,AbstractCollection.RenderType.INPUT
						));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected AbstractFormListener getFormListener() {
		return new FormListener()
				.setAttributesLazyDataModelListener((AttributesLazyDataModelListenerImpl)((LazyDataModel<IdentificationFormAttribute>) attributesDataTable.getValue()).getListener());
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormListener extends AbstractFormListener {
		private AttributesLazyDataModelListenerImpl attributesLazyDataModelListener;
		
		@Override
		protected Collection<IdentificationFormAttribute> computeFormAttributes(Form form,Data data) {
			return attributesLazyDataModelListener.computeChanges();
		}
		
		@Override
		protected String getRequiredAttributeMessage() {
			return "Veuillez modifier au moins un attribut";
		}
		
		@Override
		protected void act(Form form,Data data, Collection<IdentificationFormAttribute> formAttributes) {
			EntitySaver.getInstance().save(IdentificationFormAttribute.class, new Arguments<IdentificationFormAttribute>()
					.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(IdentificationFormAttributeBusiness.SAVE))
					.addCreatablesOrUpdatables(formAttributes));
		}
	}
	
	@Getter @Setter @Accessors(chain=true) @NoArgsConstructor
	public static class FormConfiguratorListener extends AbstractFormConfiguratorListener {
		private DataTable dataTable;
		
		@Override
		public Map<Object, Object> getLayoutArguments(Form form, Collection<Map<Object, Object>> cellsArguments) {
			CollectionHelper.addElementAt(cellsArguments, identificationForm == null ? 2 : 0, MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
			return super.getLayoutArguments(form, cellsArguments);
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class AttributesDataTableListenerImpl extends IdentificationFormAttributeListPage.DataTableListenerImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(IdentificationFormAttribute.FIELD_REQUIRED.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
				map.put(Column.FIELD_WIDTH, "300");
			}else if(IdentificationFormAttribute.FIELD_ORDER_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	public static class AttributesLazyDataModelListenerImpl extends IdentificationFormAttributeListPage.LazyDataModelListenerImpl implements Serializable {		
		private List<IdentificationFormAttribute> list;
		private Map<Object,Object[]> initial = new HashMap<>();
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<IdentificationFormAttribute> lazyDataModel) {
			return IdentificationFormAttributeQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_EDIT;
		}
		
		@Override
		public List<IdentificationFormAttribute> read(LazyDataModel<IdentificationFormAttribute> lazyDataModel) {		
			list = super.read(lazyDataModel);
			initial.clear();
			if(CollectionHelper.isEmpty(list)) {
				
			}else {
				for(IdentificationFormAttribute index : list) {
					initial.put(index.getIdentifier(), new Object[] {index.getRequired(),index.getOrderNumber()});
					if(index.getRequired() == null)
						index.setRequiredAsString("0");
					else if(Boolean.TRUE.equals(index.getRequired()))
						index.setRequiredAsString("1");
					else if(Boolean.FALSE.equals(index.getRequired()))
						index.setRequiredAsString("2");
				}
			}
			return list;
		}
		
		public Collection<IdentificationFormAttribute> computeChanges() {
			if(CollectionHelper.isEmpty(list) || MapHelper.isEmpty(initial))
				return null;
			Collection<IdentificationFormAttribute> changes = null;
			for(IdentificationFormAttribute index : list) {
				if("0".equals(index.getRequiredAsString()))
					index.setRequired(null);
				else if("1".equals(index.getRequiredAsString()))
					index.setRequired(Boolean.TRUE);
				else if("2".equals(index.getRequiredAsString()))
					index.setRequired(Boolean.FALSE);
				Boolean initialRequired = (Boolean) initial.get(index.getIdentifier())[0];
				if(index.getRequired() == null)
					if(initialRequired == null)
						;
					else {
						if(changes == null)
							changes = new ArrayList<>();
						changes.add(index);
						continue;
					}
				else
					if(!index.getRequired().equals(initialRequired)) {
						if(changes == null)
							changes = new ArrayList<>();
						changes.add(index);
					}
				
				Integer initialOrderNumber = (Integer) initial.get(index.getIdentifier())[1];
				if(index.getOrderNumber() == null)
					if(initialOrderNumber == null)
						;
					else {
						if(changes == null)
							changes = new ArrayList<>();
						changes.add(index);
						continue;
					}
				else
					if(!index.getOrderNumber().equals(initialOrderNumber)) {
						if(changes == null)
							changes = new ArrayList<>();
						changes.add(index);
					}
			}
			return changes;
		}
	}
}