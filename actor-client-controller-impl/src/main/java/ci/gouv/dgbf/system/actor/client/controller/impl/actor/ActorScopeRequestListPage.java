package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
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
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeRequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorScopeRequestListPage extends AbstractEntityListPageContainerManagedImpl<ActorScopeRequest> implements Serializable {

	private ActorScopeRequestFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ActorScopeRequestFilterController();
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ActorScopeRequestFilterController.class,filterController);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue("Domaines");
	}
		
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		ActorScopeRequestFilterController filterController = null;		
		LazyDataModelListenerImpl lazyDataModelListenerImpl = (LazyDataModelListenerImpl) MapHelper.readByKey(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER);
		if(lazyDataModelListenerImpl == null)
			arguments.put(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListenerImpl = new LazyDataModelListenerImpl());
		filterController = (ActorScopeRequestFilterController) lazyDataModelListenerImpl.getFilterController();
		if(filterController == null)
			lazyDataModelListenerImpl.setFilterController(filterController = new ActorScopeRequestFilterController());		
		lazyDataModelListenerImpl.enableFilterController();
		String outcome = ValueHelper.defaultToIfBlank((String)MapHelper.readByKey(arguments,OUTCOME),OUTCOME);
		filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome);
		
		DataTableListenerImpl dataTableListenerImpl = (DataTableListenerImpl) MapHelper.readByKey(arguments, DataTable.FIELD_LISTENER);
		if(dataTableListenerImpl == null)
			arguments.put(DataTable.FIELD_LISTENER, dataTableListenerImpl = new DataTableListenerImpl());
		dataTableListenerImpl.setFilterController(filterController);
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ActorScopeRequest.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, filterController.generateColumnsNames());	
		
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		dataTable.getOrderNumberColumn().setWidth("10");
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ActorScopeRequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Traiter", MenuItem.FIELD_ICON,"fa fa-eye");
		dataTable.addRecordMenuItemByArgumentsExecuteFunction("Accepter","fa fa-check",new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				ActorScopeRequest actorScopeRequest = (ActorScopeRequest)action.readArgument();						
				if(actorScopeRequest == null)
					throw new RuntimeException("Sélectionner une demande de domaine");
				actorScopeRequest.setGranted(Boolean.TRUE);
				actorScopeRequest.setActorAsString(SessionManager.getInstance().getUserName());
				Arguments<ActorScopeRequest> arguments = new Arguments<ActorScopeRequest>().setResponseEntityClass(String.class)
						.addCreatablesOrUpdatables(actorScopeRequest);
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorScopeRequestBusiness.PROCESS));
				EntitySaver.getInstance().save(ActorScopeRequest.class, arguments);
				return arguments.get__responseEntity__();
			}
		});
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog(ActorScopeRequestProcessPage.OUTCOME, MenuItem.FIELD_VALUE,"Rejeter", MenuItem.FIELD_ICON,"fa fa-close"
				,MenuItem.FIELD___PARAMETERS__,Map.of(ActorScopeRequest.FIELD_GRANTED,List.of(Boolean.FALSE.toString())));	
		return dataTable;
	}
		
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		private ActorScopeRequestFilterController filterController;		
		private ScopeType scopeType;
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(ActorScopeRequest.FIELD_ACTOR_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Acteur");
			}else if(ActorScopeRequest.FIELD_SCOPE_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Domaine");
			}else if(ActorScopeRequest.FIELD_GRANTED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Accordé");
			}
			return map;
		}
		
		@Override
		public Class<? extends AbstractMenu> getRecordMenuClass(AbstractCollection collection) {
			return ContextMenu.class;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<ActorScopeRequest> implements Serializable {
		protected ScopeType scopeType;
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<ActorScopeRequest> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ActorScopeRequest> lazyDataModel) {
			return ActorScopeRequestQuerier.QUERY_IDENTIFIER_READ_DYNAMIC;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ActorScopeRequest> lazyDataModel) {
			Filter.Dto filter = super.instantiateFilter(lazyDataModel);			
			filter = ActorScopeRequestFilterController.populateFilter(filter, (ActorScopeRequestFilterController) filterController,Boolean.FALSE);
			return filter;
		}
		
		@Override
		public Arguments<ActorScopeRequest> instantiateArguments(LazyDataModel<ActorScopeRequest> lazyDataModel) {
			Arguments<ActorScopeRequest> arguments = super.instantiateArguments(lazyDataModel);
			//if( ((ActorScopeRequestFilterController)filterController).getGranted() == null )
				arguments.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.ActorScopeRequest.FIELDS_ACTOR_AS_STRING_SCOPE_AS_STRING_GRANTED_AND_GRANTED_AS_STRING);
			return arguments;
		}
	
		public LazyDataModelListenerImpl enableFilterController(){
			if(filterController == null)
				filterController = new ActorScopeRequestFilterController();
			filterController.build();
			return this;
		}
	}

	/**/
		
	public static final String OUTCOME = "actorScopeRequestListView";
}