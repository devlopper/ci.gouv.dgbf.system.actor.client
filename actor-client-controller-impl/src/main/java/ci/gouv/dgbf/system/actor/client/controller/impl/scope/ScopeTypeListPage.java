package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ScopeTypeListPage extends AbstractEntityListPageContainerManagedImpl<ScopeType> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		//dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();	
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.LABEL;
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ScopeType.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES
				, CollectionHelper.listOf(ScopeType.FIELD_CODE,ScopeType.FIELD_NAME,ScopeType.FIELD_ORDER_NUMBER,ScopeType.FIELD_REQUESTABLE_AS_STRING));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
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
			if(ScopeType.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ScopeType.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(ScopeType.FIELD_SCOPE_FUNCTION_CODE_SCRIPT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Script de génération du code des postes");
				map.put(Column.FIELD_WIDTH, "300");
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ScopeType.FIELD_SCOPE_FUNCTION_NAME_SCRIPT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Script de génération du libellé des postes");
				map.put(Column.FIELD_WIDTH, "300");
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ScopeType.FIELD_ORDER_NUMBER.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Numéro d'ordre");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ScopeType.FIELD_SCOPE_FUNCTION_DERIVABLE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Poste dérivable ?");
				map.put(Column.FIELD_WIDTH, "150");
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ScopeType.FIELD_REQUESTABLE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Demandable ?");
				map.put(Column.FIELD_WIDTH, "100");
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}
			return map;
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ScopeType> implements Serializable {
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<ScopeType> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ScopeType> lazyDataModel) {
			return ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		public Arguments<ScopeType> instantiateArguments(LazyDataModel<ScopeType> lazyDataModel) {
			Arguments<ScopeType> arguments = super.instantiateArguments(lazyDataModel);
			arguments.projections(ScopeType.FIELD_IDENTIFIER,ScopeType.FIELD_CODE,ScopeType.FIELD_NAME,ScopeType.FIELD_ORDER_NUMBER);
			arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.FIELDS_REQUESTABLE_AND_REQUESTABLE_AS_STRING);
			return arguments;
		}
	}
}