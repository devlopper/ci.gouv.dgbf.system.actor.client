package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeTypeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.business.api.ScopeFunctionBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionListPage extends AbstractEntityListPageContainerManagedImpl<ScopeFunction> implements Serializable {

	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,Helper.buildFunctionListPageTabMenu(null),Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)					
						));
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ScopeFunctionListPage.class,Boolean.TRUE);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des postes";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ScopeFunction.class);
		String functionIdentifier = (String) MapHelper.readByKey(arguments, FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER));
		Collection<String> columnsNames = new ArrayList<>();
		columnsNames.addAll(List.of(ScopeFunction.FIELD_CODE,ScopeFunction.FIELD_NAME,ScopeFunction.FIELD_SCOPE_AS_STRING));
		if(StringHelper.isBlank(functionIdentifier))
			columnsNames.addAll(List.of(ScopeFunction.FIELD_FUNCTION_AS_STRING));
		columnsNames.addAll(List.of(ScopeFunction.FIELD_SHARED_AS_STRING));
		
		
		//Function function = StringHelper.isBlank(functionIdentifier) ? null : __inject__(FunctionController.class).readBySystemIdentifier(functionIdentifier);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl().setFunctionIdentifier(functionIdentifier));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
				.setFunctionIdentifier(functionIdentifier));
		DataTable dataTable = DataTable.build(arguments);
		if(Boolean.TRUE.equals(MapHelper.readByKey(arguments, ScopeFunctionListPage.class))) {
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
			
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Initialiser",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.FIELD_ICON,"fa fa-database"
					,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
					,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							@SuppressWarnings("unchecked")
							LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) ((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener();
							Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(new ScopeFunction()
									.setFunctionsIdentifiers(List.of(listener.getFunctionIdentifier())));
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(ScopeFunctionBusiness.DERIVE_BY_FUNCTIONS_IDENTIFIERS));					
							EntitySaver.getInstance().save(ScopeFunction.class, arguments);
							return null;
						}
					});
			
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Recodifier",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.FIELD_ICON,"fa fa-cubes"
					,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
					,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							@SuppressWarnings("unchecked")
							LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) ((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener();
							Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(new ScopeFunction()
									.setFunctionsIdentifiers(List.of(listener.getFunctionIdentifier())));
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(ScopeFunctionBusiness.CODIFY_BY_FUNCTIONS_IDENTIFIERS));					
							EntitySaver.getInstance().save(ScopeFunction.class, arguments);
							return null;
						}
					});
			
			dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Supprimer",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
					,MenuItem.FIELD_ICON,"fa fa-trash"
					,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
					,List.of(RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						@Override
						protected Object __runExecuteFunction__(AbstractAction action) {
							@SuppressWarnings("unchecked")
							LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) ((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener();
							Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(new ScopeFunction()
									.setFunctionsIdentifiers(List.of(listener.getFunctionIdentifier())));
							arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(ScopeFunctionBusiness.DELETE_BY_FUNCTIONS_IDENTIFIERS));					
							EntitySaver.getInstance().save(ScopeFunction.class, arguments);
							return null;
						}
					});
			
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
			dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
					
			dataTable.setAreColumnsChoosable(Boolean.TRUE);
		}
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private String functionIdentifier;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(ScopeFunction.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "75");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
			}else if(ScopeFunction.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
			}else if(ScopeFunction.FIELD_SHARED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Partagé");
				map.put(Column.FIELD_WIDTH, "100");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ScopeFunction.FIELD_SCOPE_AS_STRING.equals(fieldName)) {
				Collection<ScopeTypeFunction> scopeTypeFunctions = EntityReader.getInstance().readMany(ScopeTypeFunction.class, ScopeTypeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_FUNCTIONS_IDENTIFIERS
						,ScopeTypeFunctionQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS, List.of(functionIdentifier));
				String name;
				if(CollectionHelper.isEmpty(scopeTypeFunctions))
					name = "Domaine";
				else
					name = scopeTypeFunctions.stream().map(x -> x.getScopeType().getName()).collect(Collectors.joining(" / "));
				map.put(Column.FIELD_HEADER_TEXT, name);
				map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_CODE_NAME);
			}else if(ScopeFunction.FIELD_FUNCTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ScopeFunction> implements Serializable {				
		private String functionIdentifier;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<ScopeFunction> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ScopeFunction> lazyDataModel) {
			return ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ScopeFunction> lazyDataModel) {
			Filter.Dto filter =  super.instantiateFilter(lazyDataModel);
			if(filter == null)
				filter = new Filter.Dto();
			//filter.addField(ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_CODE_NAME, lazyDataModel.get__filters__().get("scope"));
			//filter.addField(ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_NAME, lazyDataModel.get__filters__().get("scope"));
			if(StringHelper.isNotBlank(functionIdentifier))
				filter.addField(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER, functionIdentifier);
			return filter;
		}
	}
}