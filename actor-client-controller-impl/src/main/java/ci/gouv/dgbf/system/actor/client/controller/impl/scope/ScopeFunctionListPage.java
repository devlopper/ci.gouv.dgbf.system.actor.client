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

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeTypeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeFunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.entities.Profile;
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
	
	public static String buildWindowTitleValue(String prefix,Function function) {
		Collection<String> strings = new ArrayList<>();
		strings.add(prefix);
		if(function != null) {
			strings.add(function.getName());
		}
		return StringHelper.concatenate(strings, " | ");
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		RenderType renderType = (RenderType) MapHelper.readByKey(arguments, RenderType.class);
		ScopeFunction scopeFunction = (ScopeFunction) MapHelper.readByKey(arguments, ScopeFunction.class);
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ScopeFunction.class);
		String functionIdentifier = (String) MapHelper.readByKey(arguments, FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER));
		Function function = (Function) MapHelper.readByKey(arguments, Function.class);
		if(function != null) {
			functionIdentifier = function.getIdentifier();
		}
		Collection<String> columnsNames = new ArrayList<>();
		columnsNames.addAll(List.of(ScopeFunction.FIELD_CODE,ScopeFunction.FIELD_NAME,ScopeFunction.FIELD_SCOPE_AS_STRING));
		if(StringHelper.isBlank(functionIdentifier))
			columnsNames.addAll(List.of(ScopeFunction.FIELD_FUNCTION_AS_STRING));
		else {
			if(function != null) {
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.EXECUTION_HOLDERS_CODES.contains(function.getCode())) {
					columnsNames.addAll(List.of(ScopeFunction.FIELD_CHILDREN_CODES_NAMES));
				}else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.EXECUTION_ASSISTANTS_CODES.contains(function.getCode())) {
					columnsNames.addAll(List.of(ScopeFunction.FIELD_PARENT_AS_STRING));
				}
			}
		}
		columnsNames.addAll(List.of(ScopeFunction.FIELD_SHARED_AS_STRING,ScopeFunction.FIELD_REQUESTED_AS_STRING,ScopeFunction.FIELD_GRANTED_AS_STRING
				,ScopeFunction.FIELD_ACTORS_CODES));
		
		//Function function = StringHelper.isBlank(functionIdentifier) ? null : __inject__(FunctionController.class).readBySystemIdentifier(functionIdentifier);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl().setFunctionIdentifier(functionIdentifier));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
				.setFunctionIdentifier(functionIdentifier));
		DataTable dataTable = DataTable.build(arguments);
		dataTable.set__parentElement__(scopeFunction);
		if(Boolean.TRUE.equals(SessionManager.getInstance().isUserHasRole(Profile.CODE_ADMINISTRATEUR))) {
			if(scopeFunction == null) {
				dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
			}else {
				dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog(ScopeFunctionAssistantEditPage.OUTCOME,CommandButton.FIELD___ACTION__,Action.CREATE);
			}
			if(Boolean.TRUE.equals(MapHelper.readByKey(arguments, ScopeFunctionListPage.class))) {
				//dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
				
				/*
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Initialiser",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,MenuItem.FIELD_ICON,"fa fa-database"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(org.cyk.utility.__kernel__.user.interface_.message.RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								@SuppressWarnings("unchecked")
								LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) ((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener();
								Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(new ScopeFunction()
										.setFunctionsIdentifiers(List.of(listener.getFunctionIdentifier())));
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(ScopeFunctionBusiness.DERIVE_BY_FUNCTIONS_IDENTIFIERS));					
								EntitySaver.getInstance().save(ScopeFunction.class, arguments);
								return null;
							}
						});
				
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Recodifier",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,MenuItem.FIELD_ICON,"fa fa-cubes"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(org.cyk.utility.__kernel__.user.interface_.message.RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								@SuppressWarnings("unchecked")
								LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) ((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener();
								Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(new ScopeFunction()
										.setFunctionsIdentifiers(List.of(listener.getFunctionIdentifier())));
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(ScopeFunctionBusiness.CODIFY_BY_FUNCTIONS_IDENTIFIERS));					
								EntitySaver.getInstance().save(ScopeFunction.class, arguments);
								return null;
							}
						});
				
				dataTable.addHeaderToolbarLeftCommandsByArguments(MenuItem.FIELD_VALUE,"Supprimer",MenuItem.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,MenuItem.FIELD_ICON,"fa fa-trash"
						,MenuItem.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE,MenuItem.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES
						,List.of(org.cyk.utility.__kernel__.user.interface_.message.RenderType.GROWL),MenuItem.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								@SuppressWarnings("unchecked")
								LazyDataModelListenerImpl listener = (LazyDataModelListenerImpl) ((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener();
								Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().addCreatablesOrUpdatables(new ScopeFunction()
										.setFunctionsIdentifiers(List.of(listener.getFunctionIdentifier())));
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
										.setActionIdentifier(ScopeFunctionBusiness.DELETE_BY_FUNCTIONS_IDENTIFIERS));					
								EntitySaver.getInstance().save(ScopeFunction.class, arguments);
								return null;
							}
						});
				*/
				dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ScopeFunctionReadAssistantsPage.OUTCOME, MenuItem.FIELD_VALUE,"Assistants",MenuItem.FIELD_ICON,"fa fa-user");
				
				//dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
				//dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
			}
			
			if(Boolean.TRUE.equals(MapHelper.readByKey(arguments, ScopeFunctionListPage.class)) || RenderType.LIST.equals(renderType) || scopeFunction != null) {
				dataTable.addRecordMenuItemByArgumentsOpenViewInDialogUpdate();
				dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
			}
		}
		
		if(Boolean.TRUE.equals(MapHelper.readByKey(arguments, ScopeFunctionListPage.class))) {
			if(!Boolean.TRUE.equals(SessionManager.getInstance().isUserHasOneOfRoles(Profile.CODE_ADMINISTRATEUR)) 
					&&  Boolean.TRUE.equals(SessionManager.getInstance().isUserHasOneOfRoles(Profile.CODE_CHARGE_ETUDE_DAS))) {
				dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ScopeFunctionEditNamePage.OUTCOME, MenuItem.FIELD_VALUE,"Modifier le libellé",MenuItem.FIELD_ICON,"fa fa-edit");
			}
		}
		
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
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
				Collection<ScopeTypeFunction> scopeTypeFunctions = StringHelper.isBlank(functionIdentifier) ? null
						: EntityReader.getInstance().readMany(ScopeTypeFunction.class, ScopeTypeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_FUNCTIONS_IDENTIFIERS
						,ScopeTypeFunctionQuerier.PARAMETER_NAME_FUNCTIONS_IDENTIFIERS, List.of(functionIdentifier));
				String name;
				if(CollectionHelper.isEmpty(scopeTypeFunctions))
					name = "Domaine";
				else
					name = scopeTypeFunctions.stream().map(x -> x.getScopeType().getName()).collect(Collectors.joining(" / "));
				map.put(Column.FIELD_HEADER_TEXT, String.format("Champ d'action (%s)",name));
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, ScopeFunctionQuerier.PARAMETER_NAME_SCOPE_CODE_NAME);
			}else if(ScopeFunction.FIELD_FUNCTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ScopeFunction.FIELD_PARENT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Titulaire");
				map.put(Column.FIELD_WIDTH, "75");
			}else if(ScopeFunction.FIELD_CHILDREN_CODES_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Assistant");
				map.put(Column.FIELD_WIDTH, "75");
			}else if(ScopeFunction.FIELD_REQUESTED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Demandée?");
				map.put(Column.FIELD_WIDTH, "80");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ScopeFunction.FIELD_GRANTED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Accordée?");
				map.put(Column.FIELD_WIDTH, "80");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(ScopeFunction.FIELD_ACTOR_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Utilisateur");
				map.put(Column.FIELD_WIDTH, "200");
				map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
			}else if(ScopeFunction.FIELD_ACTORS_AS_STRINGS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Utilisateur(s)");
				map.put(Column.FIELD_WIDTH, "400");
				map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
			}else if(ScopeFunction.FIELD_ACTORS_CODES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Utilisateur(s)");
				map.put(Column.FIELD_WIDTH, "400");
				map.put(Column.FIELD_VISIBLE, Boolean.TRUE);
			}
			return map;
		}
		
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
		
		@Override
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(ScopeFunction.FIELD_CHILDREN_CODES_NAMES.equals(column.getFieldName())) {
				if(CollectionHelper.isEmpty(((ScopeFunction)record).getChildrenCodesNames()))
					return null;
				return StringHelper.concatenate(((ScopeFunction)record).getChildrenCodesNames().stream().map(x -> StringUtils.substringBefore(x, " "))
						.collect(Collectors.toList()),",");
			}else if(ScopeFunction.FIELD_PARENT_AS_STRING.equals(column.getFieldName())) {
				if(StringHelper.isBlank(((ScopeFunction)record).getParentAsString()))
					return null;
				return StringUtils.substringBefore(((ScopeFunction)record).getParentAsString(), " ");
			}else if(ScopeFunction.FIELD_ACTORS_CODES.equals(column.getFieldName())) {
				if(CollectionHelper.isEmpty(((ScopeFunction)record).getActorsCodes()))
					return null;
				return StringHelper.concatenate(((ScopeFunction)record).getActorsCodes().stream().map(x -> StringUtils.substringBefore(x, " "))
						.collect(Collectors.toList()),",");
			}
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		/*@Override
		public String getStyleClassByRecord(Object record, Integer recordIndex) {
			if(record instanceof ScopeFunction) {
				ScopeFunction scopeFunction = (ScopeFunction) record;
				if(Boolean.TRUE.equals(scopeFunction.getGranted()))
					return "cyk-background-highlight";
				if(Boolean.TRUE.equals(scopeFunction.getRequested()))
					return "background: red !important";
			}
			return super.getStyleClassByRecord(record, recordIndex);
		}*/
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
			return ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_FOR_UI;
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
		
		@Override
		public Arguments<ScopeFunction> instantiateArguments(LazyDataModel<ScopeFunction> lazyDataModel) {
			Arguments<ScopeFunction> arguments = super.instantiateArguments(lazyDataModel);
			ArrayList<String> list = new ArrayList<>();
			list.addAll(List.of(ScopeFunction.FIELD_REQUESTED,ScopeFunction.FIELD_GRANTED,ScopeFunction.FIELD_ACTORS_CODES));
			arguments.getRepresentationArguments().getQueryExecutorArguments().setProcessableTransientFieldsNames(list);
			return arguments;
		}
	}
	
	public static enum RenderType {
		LIST
		,ASSISTANTS
	}
}