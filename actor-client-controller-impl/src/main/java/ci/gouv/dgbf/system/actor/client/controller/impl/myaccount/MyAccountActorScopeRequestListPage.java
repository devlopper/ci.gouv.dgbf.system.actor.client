package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorScopeRequestFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorScopeRequestListPage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class MyAccountActorScopeRequestListPage extends AbstractEntityListPageContainerManagedImpl<ActorScopeRequest> implements MyAccountTheme,Serializable{

	private ActorScopeRequestFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ActorScopeRequestFilterController();
		filterController.setActorInitial(__inject__(ActorController.class).getLoggedIn())/*.ignore(ActorScopeRequestFilterController.FIELD_ACTOR_SELECT_ONE)*/;
		filterController.setRenderType(AbstractFilterController.RenderType.NONE);
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ActorScopeRequestFilterController.class,filterController,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFilterController(filterController),ActorScopeRequestListPage.class,MyAccountActorScopeRequestListPage.class);
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
		DataTable dataTable = ActorScopeRequestListPage.buildDataTable(arguments);
		return dataTable;
	}
		
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends ActorScopeRequestListPage.DataTableListenerImpl implements Serializable {
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ActorScopeRequestListPage.LazyDataModelListenerImpl implements Serializable {
		
	}

	/**/
		
	public static final String OUTCOME = "myAccountActorScopeRequestListView";
}