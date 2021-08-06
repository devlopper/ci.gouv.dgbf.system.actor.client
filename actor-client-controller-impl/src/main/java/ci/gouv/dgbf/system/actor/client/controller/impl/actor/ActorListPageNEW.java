package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.AbstractMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.ContextMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.impl.privilege.ActorEditProfilesPage;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorListPageNEW extends AbstractEntityListPageContainerManagedImpl<Actor> implements Serializable {

	private ActorFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ActorFilterController();
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ActorFilterController.class,filterController);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue("Acteurs");
	}
		
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		ActorFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (ActorFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new ActorFilterController());		
		lazyDataModelListenerImpl.enableFilterController();
		String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
		filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl());
		dataTableListenerImpl.setFilterController(filterController);
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Actor.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());	
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		dataTable.getOrderNumberColumn().setWidth("15");
		
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
		
		final ActorFilterController finalFilterController = filterController;
		Map<String,List<String>> parameters = finalFilterController.asMap();
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog(ActorScopeCreatePage.OUTCOME, MenuItem.FIELD_VALUE,"Assigner domaine"
				,MenuItem.FIELD___PARAMETERS__,parameters,MenuItem.FIELD_ICON,"fa fa-plus-square");
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog(ActorScopeDeletePage.OUTCOME, MenuItem.FIELD_VALUE,"Retirer domaine"
				,MenuItem.FIELD___PARAMETERS__,parameters,MenuItem.FIELD_ICON,"fa fa-minus-square");
		
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ActorEditProfilesPage.OUTCOME, MenuItem.FIELD_VALUE,"Modifier Profile(s)", MenuItem.FIELD_ICON,"fa fa-lock");
		
		if(filterController.getVisible() != null) {
			if(filterController.getVisible()) {
				addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.TRUE);
			}else {
				addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.FALSE);
			}
		}else {
			addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.TRUE);
			addVisibleRecordMenuItem(dataTable, finalFilterController, Boolean.FALSE);
		}
		return dataTable;
	}
	
	private static void addVisibleRecordMenuItem(DataTable dataTable,ActorFilterController filterController,Boolean visible) {
		dataTable.addRecordMenuItemByArgumentsExecuteFunction(Boolean.TRUE.equals(visible) ? "Retirer domaine" : "Assigner domaine"
			,Boolean.TRUE.equals(visible) ? "fa fa-minus" : "fa fa-plus",new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				return AbstractActorScopeCreateOrDeletePage.save(Boolean.TRUE.equals(visible) ? ActorScopeBusiness.UNVISIBLE : ActorScopeBusiness.VISIBLE
						, CollectionHelper.listOf(Boolean.TRUE, (Actor)action.readArgument()), CollectionHelper.listOf(Boolean.TRUE, filterController.getScope()));
			}
		});
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private ActorFilterController filterController;		
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(Actor.FIELD_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prenom(s)");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom d'utilisateur");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_PROFILES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Profile(s)");
			}else if(Actor.FIELD_FUNCTIONS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s)");
			}else if(Actor.FIELD_VISIBLE_SECTIONS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section(s) visible(s)");
			}else if(Actor.FIELD_VISIBLE_MODULES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Module(s) visible(s)");
			}else if(Actor.FIELD_SERVICE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Service");
				map.put(Column.FIELD_WIDTH, "300");
			}else if(Actor.FIELD_SECTION_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Actor.FIELD_PROFILES_CODES.equals(fieldName) || Actor.FIELD_PROFILES_CODES_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Profile(s)");
				map.put(Column.FIELD_WIDTH, "150");
			}else if(Actor.FIELD_ADMINISTRATIVE_UNIT_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Unité administrative");
				map.put(Column.FIELD_VISIBLE, Boolean.FALSE);
			}else if(Actor.FIELD_ADMINISTRATIVE_FUNCTION.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction administrative");
				map.put(Column.FIELD_WIDTH, "200");
			}
			return map;
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
		
		@Override
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(record != null && CollectionHelper.isNotEmpty(((Actor)record).getProfilesCodes()) && column != null && Actor.FIELD_PROFILES_CODES.equals(column.getFieldName()))
				return StringHelper.concatenate(((Actor)record).getProfilesCodes(),",");
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
		
		/*
		public String getStyleClassByRecord(Object record, Integer recordIndex) {
			if(record == null || recordIndex == null)
				return null;
			if(filterController.getVisible() == null && Boolean.TRUE.equals(((Scope)record).getVisible()))
				return "cyk-background-highlight";
			return null;
		}*/
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Actor> implements Serializable {
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<Actor> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Actor> lazyDataModel) {
			return ActorQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Actor> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			filter = ActorFilterController.populateFilter(filter, (ActorFilterController) filterController,Boolean.FALSE);
			return filter;
		}
		
		@Override
		public Arguments<Actor> instantiateArguments(LazyDataModel<Actor> lazyDataModel) {
			Arguments<Actor> arguments = super.instantiateArguments(lazyDataModel);
			arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities
					.Actor.FIELDS_CODE_FIRST_NAME_LAST_NAMES_SECTION_ADMINISTRATIVE_UNIT_ADMINISTRATIVE_FUNCTION,ci.gouv.dgbf.system.actor.server.persistence.entities
					.Actor.FIELD_PROFILES_CODES);
			return arguments;
		}
	
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new ActorFilterController();
			filterController.build();
			return this;
		}
	}

	/**/
	
	public static final String OUTCOME = "actorListView";
}