package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ProfileListPage extends AbstractEntityListPageContainerManagedImpl<Profile> implements Serializable {

	private ProfileType profileType;
	private MenuModel tabMenu;
	private Integer tabMenuActiveIndex;
	
	@Override
	protected void __listenPostConstruct__() {
		profileType = WebController.getInstance().getRequestParameterEntityAsParent(ProfileType.class);
		Collection<ProfileType> profileTypes = EntityReader.getInstance().readMany(ProfileType.class, new Arguments<ProfileType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(ProfileTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		if(profileType == null)
			profileType = CollectionHelper.getFirst(profileTypes);
		super.__listenPostConstruct__();				
		if(CollectionHelper.isNotEmpty(profileTypes)) {		
			tabMenu = new DefaultMenuModel();
			tabMenuActiveIndex = ((List<ProfileType>)profileTypes).indexOf(profileType);	
			for(ProfileType index : profileTypes) {
				DefaultMenuItem item = new DefaultMenuItem();
				item.setValue(index.getName());
				item.setOutcome("profileListView");
				item.setParam(ParameterName.stringify(ProfileType.class), index.getIdentifier());
				tabMenu.addElement(item);
			}
		}
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable(null,null,new LazyDataModelListenerImpl().setProfileType(profileType));
		if(profileType == null || profileType.getCode().equals(ci.gouv.dgbf.system.actor.server.persistence.entities.ProfileType.CODE_SYSTEME)) {
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
			dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();	
		}
			
		@SuppressWarnings("unchecked")
		LazyDataModel<Profile> lazyDataModel = (LazyDataModel<Profile>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des profiles";
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(Profile.FIELD_CODE,Profile.FIELD_NAME);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Profile.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener);
		
		LazyDataModel<Profile> lazyDataModel = (LazyDataModel<Profile>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES);
		}
		lazyDataModel.setListener(lazyDataModelListener);
		return dataTable;
	}
	
	public static DataTable instantiateDataTable() {
		return instantiateDataTable(null, null, null);
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
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Profile> implements Serializable {		
		private ProfileType profileType;
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