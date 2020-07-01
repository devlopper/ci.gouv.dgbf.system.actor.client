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
	private MenuModel tabMenu;
	private Integer tabMenuActiveIndex;
	
	@Override
	protected void __listenPostConstruct__() {
		functionType = WebController.getInstance().getRequestParameterEntityAsParent(FunctionType.class);
		Collection<FunctionType> functionTypes = EntityReader.getInstance().readMany(FunctionType.class, new Arguments<FunctionType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(FunctionTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));
		if(functionType == null)
			functionType = CollectionHelper.getFirst(functionTypes);
		super.__listenPostConstruct__();				
		if(CollectionHelper.isNotEmpty(functionTypes)) {		
			tabMenu = new DefaultMenuModel();
			tabMenuActiveIndex = ((List<FunctionType>)functionTypes).indexOf(functionType);	
			for(FunctionType index : functionTypes) {
				DefaultMenuItem item = new DefaultMenuItem();
				item.setValue(index.getName());
				item.setOutcome("functionListView");
				item.setParam(ParameterName.stringify(FunctionType.class), index.getIdentifier());
				tabMenu.addElement(item);
			}
		}
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = instantiateDataTable(null,null,new LazyDataModelListenerImpl().setFunctionType(functionType),functionType);
		dataTable.set__parentElement__(functionType);
		@SuppressWarnings("unchecked")
		LazyDataModel<Function> lazyDataModel = (LazyDataModel<Function>) dataTable.getValue();
		lazyDataModel.setReadQueryIdentifier(FunctionQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des fonctions";
	}
	
	/**/
	
	@SuppressWarnings("unchecked")
	public static DataTable instantiateDataTable(Collection<String> columnsFieldsNames,DataTableListenerImpl listener,LazyDataModelListenerImpl lazyDataModelListener,FunctionType functionType) {
		if(listener == null)
			listener = new DataTableListenerImpl();
		if(columnsFieldsNames == null) {
			columnsFieldsNames = CollectionHelper.listOf(Function.FIELD_CODE,Function.FIELD_NAME);
		}
		
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Function.class
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,columnsFieldsNames,DataTable.FIELD_LISTENER,listener
				,DataTable.FIELD_STYLE_CLASS,"cyk-ui-datatable-footer-visibility-hidden");
		
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
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
		
		LazyDataModel<Function> lazyDataModel = (LazyDataModel<Function>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		if(lazyDataModelListener == null) {
			lazyDataModelListener = new LazyDataModelListenerImpl();
			lazyDataModel.setReadQueryIdentifier(FunctionQuerier.QUERY_IDENTIFIER_READ_BY_TYPES_CODES);
		}
		lazyDataModel.setListener(lazyDataModelListener);
		return dataTable;
	}
	
	public static DataTable instantiateDataTable() {
		return instantiateDataTable(null, null, null,null);
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
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Function> implements Serializable {		
		private FunctionType functionType;
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Function> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);
			if(functionType != null) {
				if(filter == null)
					filter = new Filter.Dto();
				filter.addField(FunctionQuerier.PARAMETER_NAME_TYPES_CODES, List.of(functionType.getCode()));
			}
			return filter;
		}
	}
}