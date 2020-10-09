package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
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

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.FunctionType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class FunctionListPage extends AbstractEntityListPageContainerManagedImpl<Function> implements Serializable {

	private FunctionType functionType;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		functionType = WebController.getInstance().getRequestParameterEntityAsParent(FunctionType.class);
		Collection<FunctionType> functionTypes = EntityReader.getInstance().readMany(FunctionType.class, new Arguments<FunctionType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(FunctionTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		if(functionType == null)
			functionType = CollectionHelper.getFirst(functionTypes);
		super.__listenPostConstruct__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
					MapHelper.instantiate(Cell.FIELD_CONTROL,Helper.buildFunctionListPageTabMenu(functionType),Cell.FIELD_WIDTH,12)
					,MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)
					)
			);
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(MapHelper.instantiate(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFunctionType(functionType)));
		dataTable.set__parentElement__(functionType);		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(CommandButton.FIELD_LISTENER
				,new CommandButton.Listener.AbstractImpl() {
			@Override
			protected Map<String, List<String>> getViewParameters(AbstractAction action) {
				Map<String, List<String>> parameters = super.getViewParameters(action);
				if(functionType != null) {
					if(parameters == null)
						parameters = new HashMap<>();
					parameters.put(ParameterName.stringify(FunctionType.class), List.of(((FunctionType)functionType).getIdentifier()));
				}					
				return parameters;
			}
			
			@Override
			protected String getOutcome(AbstractAction action) {
				return "functionEditView";
			}
		});		
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("functionEditProfilesView", CommandButton.FIELD_VALUE,"Profiles"
				,CommandButton.FIELD_ICON,"fa fa-user");
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();		
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des catégories de fonctions";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Function.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES
				, CollectionHelper.listOf(Function.FIELD_CODE,Function.FIELD_NAME,Function.FIELD_SHARED_AS_STRING
						,Function.FIELD_SCOPE_TYPES_AS_STRING,Function.FIELD_NUMBER_OF_SCOPES,Function.FIELD_PROFILES_AS_STRING));
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
			if(Function.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Function.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(Function.FIELD_PROFILES_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Profile(s)");
			}else if(Function.FIELD_SCOPE_TYPES_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Type de domaine(s)");
			}else if(Function.FIELD_NUMBER_OF_SCOPES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nombre de poste(s)");
			}else if(Function.FIELD_SHARED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Partagé");
				map.put(Column.FIELD_WIDTH, "100");
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Function> implements Serializable {		
		private FunctionType functionType;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Function> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Function> lazyDataModel) {
			return functionType == null ? FunctionQuerier.QUERY_IDENTIFIER_READ_WITH_PROFILES : FunctionQuerier.QUERY_IDENTIFIER_READ_WITH_ALL_BY_TYPE_IDENTIFIER;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Function> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(functionType != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(FunctionQuerier.PARAMETER_NAME_TYPE_IDENTIFIER, functionType.getIdentifier());
			}
			return filter;
		}
	}
}