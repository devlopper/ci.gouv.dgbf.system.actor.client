package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Service;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ProfileListPage extends AbstractEntityListPageContainerManagedImpl<Profile> implements Serializable {

	private ProfileType profileType;
	private Boolean isService;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		profileType = WebController.getInstance().getRequestParameterEntityAsParent(ProfileType.class);
		isService = ValueHelper.convertToBoolean(WebController.getInstance().getRequestParameter(ParameterName.stringify(Service.class)));
		super.__listenPostConstruct__();		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,Helper.buildProfileListPageTabMenu(profileType),Cell.FIELD_WIDTH,12)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,Boolean.TRUE.equals(isService) ? ServiceListPage.buildDataTable() : dataTable,Cell.FIELD_WIDTH,12)
					)
			);
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		if(Boolean.TRUE.equals(isService))
			return null;
		DataTable dataTable = buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setProfileType(profileType));		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_LISTENER
				,new CommandButton.Listener.AbstractImpl() {
			@Override
			protected Map<String, List<String>> getViewParameters(AbstractAction action) {
				Map<String, List<String>> parameters = super.getViewParameters(action);
				if(profileType != null) {
					if(parameters == null)
						parameters = new HashMap<>();
					parameters.put(ParameterName.stringify(ProfileType.class), List.of(((ProfileType)profileType).getIdentifier()));
				}					
				return parameters;
			}
			
			@Override
			protected String getOutcome(AbstractAction action) {
				return "profileEditView";
			}
		});
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		if(profileType != null && !profileType.getCode().equals(ci.gouv.dgbf.system.actor.server.persistence.entities.ProfileType.CODE_SYSTEME)) {
			dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		}
		dataTable.addRecordMenuItemByArgumentsNavigateToView(null,"profileEditPrivilegesView", CommandButton.FIELD_VALUE,"Privilèges"
				, CommandButton.FIELD_ICON,"fa fa-lock");
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des profiles";
	}
		
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Profile.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ROWS, 100);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(Profile.FIELD_CODE,Profile.FIELD_NAME));
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
			if(Profile.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Profile.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(Profile.FIELD_PRIVILEGES_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Privilèges");
			}
			return map;
		}
		
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column, Integer columnIndex) {
			if(column != null && Profile.FIELD_PRIVILEGES_AS_STRINGS.equals(column.getFieldName())) {
				Profile profile = (Profile) record;
				return CollectionHelper.isEmpty(profile.getPrivilegesAsStrings()) ? ConstantEmpty.STRING : StringHelper.concatenate(profile.getPrivilegesAsStrings(), "<br/>");
			}
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Profile> implements Serializable {		
		private ProfileType profileType;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Profile> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Profile> lazyDataModel) {
			return profileType == null ? ProfileQuerier.QUERY_IDENTIFIER_READ_WHERE_TYPE_IS_SYSTEME : ProfileQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Profile> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(profileType != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(ProfileQuerier.PARAMETER_NAME_TYPES_CODES, List.of(profileType.getCode()));
			}
			return filter;
		}
	}
}