package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribute;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentificationFormAttributeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class IdentificationFormAttributeListPage extends AbstractEntityListPageContainerManagedImpl<IdentificationFormAttribute> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("identificationFormAttributeCreateManyByFormView", MenuItem.FIELD_VALUE,"Ajouter",MenuItem.FIELD_ICON,"fa fa-plus");
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("identificationFormAttributeUpdateManyByFormView", MenuItem.FIELD_VALUE,"Modifier",MenuItem.FIELD_ICON,"fa fa-edit");
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog("identificationFormAttributeDeleteManyByFormView", MenuItem.FIELD_VALUE,"Retirer",MenuItem.FIELD_ICON,"fa fa-minus");
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des associations des formulaires et attributs";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		LazyDataModelListenerImpl lazyDataModelListener = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListener == null)
			lazyDataModelListener = new LazyDataModelListenerImpl();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, IdentificationFormAttribute.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES
				, CollectionHelper.listOf(IdentificationFormAttribute.FIELD_FORM_AS_STRING,IdentificationFormAttribute.FIELD_ATTRIBUTE_AS_STRING
						,IdentificationFormAttribute.FIELD_REQUIRED_AS_STRING));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener);
		DataTable dataTable = DataTable.build(arguments);
		
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(IdentificationFormAttribute.FIELD_FORM_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Formulaire");
			}else if(IdentificationFormAttribute.FIELD_ATTRIBUTE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Attribut");
			}else if(IdentificationFormAttribute.FIELD_REQUIRED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Requis");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(IdentificationFormAttribute.FIELD_REQUIRED.equals(fieldName)) {
				map.put(Column.FIELD_WIDTH, "150");
			}else if(IdentificationFormAttribute.FIELD_ORDER_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_WIDTH, "150");
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<IdentificationFormAttribute> implements Serializable {
		private String formIdentifier;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<IdentificationFormAttribute> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<IdentificationFormAttribute> lazyDataModel) {
			return IdentificationFormAttributeQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<IdentificationFormAttribute> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(filter == null)
				filter = new Filter.Dto();
			if(StringHelper.isNotBlank(formIdentifier))
				filter.addField(IdentificationFormAttributeQuerier.PARAMETER_NAME_FORM_IDENTIFIER, formIdentifier);
			return filter;
		}
	}
}