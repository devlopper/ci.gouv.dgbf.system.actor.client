package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ProfileTypeListPage extends AbstractEntityListPageContainerManagedImpl<ProfileType> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ProfileType.LABEL;
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();				
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl());		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ProfileType.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, List.of(ProfileType.FIELD_CODE,ProfileType.FIELD_NAME));
		DataTable dataTable = DataTable.build(arguments);
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
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
			if(ProfileType.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "100");
			}else if(ProfileType.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ProfileType> implements Serializable {
		@Override
		public Boolean getReaderUsable(LazyDataModel<ProfileType> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ProfileType> lazyDataModel) {
			return ProfileTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		public Arguments<ProfileType> instantiateArguments(LazyDataModel<ProfileType> lazyDataModel) {
			Arguments<ProfileType> arguments = super.instantiateArguments(lazyDataModel);
			//arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.FIELD_TYPE_AS_STRING);
			//arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.FIELD_NUMBER_OF_ACTORS);
			return arguments;
		}
	}
}