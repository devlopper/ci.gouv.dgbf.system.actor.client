package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
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

import ci.gouv.dgbf.system.actor.client.controller.api.ActorProfileRequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfileRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfileType;
import ci.gouv.dgbf.system.actor.client.controller.impl.myaccount.MyAccountActorProfileRequestRecordPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorProfileRequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorProfileRequestListPage extends AbstractEntityListPageContainerManagedImpl<ActorProfileRequest> implements Serializable {

	private ActorProfileRequestFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ActorProfileRequestFilterController();	
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ActorProfileRequestFilterController.class,filterController,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.ActorProfileRequest.LABEL);
	}
		
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		ActorProfileRequestFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (ActorProfileRequestFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new ActorProfileRequestFilterController());		
		lazyDataModelListenerImpl.enableFilterController();
		String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
		filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl());
		dataTableListenerImpl.setFilterController(filterController);
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ActorProfileRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());	
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		dataTable.getOrderNumberColumn().setWidth("10");
		
		if(Boolean.TRUE.equals(filterController.getIsUsedForLoggedUser())) {
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialog(MyAccountActorProfileRequestRecordPage.OUTCOME, MenuItem.FIELD_VALUE,"Demander"
					, MenuItem.FIELD_ICON,"fa fa-plus");
			dataTable.addRecordMenuItemByArgumentsExecuteFunction("Annuler","fa fa-trash",new AbstractAction.Listener.AbstractImpl() {
				@Override
				protected Object __runExecuteFunction__(AbstractAction action) {
					return __inject__(ActorProfileRequestController.class).cancel((ActorProfileRequest)action.readArgument());			
				}
			});
		}else {	
			dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate(MenuItem.FIELD___OUTCOME__,ActorProfileRequestRecordPage.OUTCOME);
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ActorProfileRequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter", MenuItem.FIELD_ICON,"fa fa-eye");
			dataTable.addRecordMenuItemByArgumentsExecuteFunction("Accepter","fa fa-check",new AbstractAction.Listener.AbstractImpl() {
				@Override
				protected Object __runExecuteFunction__(AbstractAction action) {
					return __inject__(ActorProfileRequestController.class).grant((ActorProfileRequest)action.readArgument());			
				}
			});
			dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ActorProfileRequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Rejeter", MenuItem.FIELD_ICON,"fa fa-close"
					,MenuItem.FIELD___PARAMETERS__,Map.of(ActorProfileRequest.FIELD_GRANTED,List.of(Boolean.FALSE.toString())));	
		}
				
		return dataTable;
	}
		
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private ActorProfileRequestFilterController filterController;		
		private ProfileType scopeType;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(ActorProfileRequest.FIELD_ACTOR_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, ci.gouv.dgbf.system.actor.server.persistence.entities.Actor.LABEL);
			}else if(ActorProfileRequest.FIELD_PROFILE_TYPE_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, ci.gouv.dgbf.system.actor.server.persistence.entities.ProfileType.LABEL);
				map.put(Column.FIELD_WIDTH, "300");
			}else if(ActorProfileRequest.FIELD_PROFILE_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.LABEL);
			}else if(ActorProfileRequest.FIELD_GRANTED_AS_STRING.equals(fieldName) || ActorProfileRequest.FIELD_GRANTED.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Accordé");
			}else if(ActorProfileRequest.FIELD_PROCESSING_COMMENT.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Commentaire");
			}
			return map;
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ActorProfileRequest> implements Serializable {
		protected ProfileType scopeType;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<ActorProfileRequest> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ActorProfileRequest> lazyDataModel) {
			return ActorProfileRequestQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ActorProfileRequest> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			filter = ActorProfileRequestFilterController.populateFilter(filter, (ActorProfileRequestFilterController) filterController,Boolean.FALSE);
			return filter;
		}
		
		@Override
		public Arguments<ActorProfileRequest> instantiateArguments(LazyDataModel<ActorProfileRequest> lazyDataModel) {
			Arguments<ActorProfileRequest> arguments = super.instantiateArguments(lazyDataModel);
			//if( ((ActorProfileRequestFilterController)filterController).getGranted() == null )
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.ActorProfileRequest.FIELDS_ACTOR_AS_STRING_PROFILE_TYPE_AS_STRING_PROFILE_AS_STRING_GRANTED_AND_GRANTED_AS_STRING);
			return arguments;
		}
	
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new ActorProfileRequestFilterController();
			filterController.build();
			return this;
		}
	}

	/**/
		
	public static final String OUTCOME = "actorProfileRequestListView";
}