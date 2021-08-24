package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorProfileRequestFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorProfileRequestListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorScopeRequestFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.ActorScopeRequestListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.privilege.ProfileListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ProfileFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeListPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MyAccountIndexPage extends AbstractPageContainerManagedImpl implements MyAccountTheme, Serializable {

	private Actor actor;
	
	private TabMenu tabMenu;
	private TabMenu.Tab selectedTab;
	private Layout layout;
	private DataTable dataTable;	
	
	private ScopeFilterController scopeFilterController;
	private ProfileFilterController profileFilterController;
	private ActorScopeRequestFilterController actorScopeRequestFilterController;
	private ActorProfileRequestFilterController actorProfileRequestFilterController;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		actor = __inject__(ActorController.class).getLoggedIn();
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		buildTabMenu(cellsMaps);
		buildTab(cellsMaps);
		buildLayout(cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		String prefix = "Mon compte";
		if(selectedTab.getParameterValue().equals(TAB_MY_VISIBILITIES))
			return scopeFilterController.generateWindowTitleValue(prefix);
		else if(selectedTab.getParameterValue().equals(TAB_MY_PROFILES))
			return profileFilterController.generateWindowTitleValue(prefix, Boolean.TRUE);
		else if(selectedTab.getParameterValue().equals(TAB_MY_REQUESTS))
			return actorScopeRequestFilterController.generateWindowTitleValue(prefix);
		return prefix+" | "+actor.getCode()+" - "+actor.getNames();
	}
	
	/**/
	
	private void buildTabMenu(Collection<Map<Object,Object>> cellsMaps) {		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(TabMenu.Tab tab : TABS) {
			MenuItem menuItem = new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			tabMenuItems.add(menuItem);
		}
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		/*
		if(selectedTab != null && selectedTab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildFunctionsTabMenu(cellsMaps);
		*/
	}
	 
	private void buildTab(Collection<Map<Object,Object>> cellsMaps) {
		if(selectedTab.getParameterValue().equals(TAB_MY_VISIBILITIES))
			buildTabScopes(cellsMaps);
		else if(selectedTab.getParameterValue().equals(TAB_MY_PROFILES))
			buildTabProfiles(cellsMaps);
		else if(selectedTab.getParameterValue().equals(TAB_MY_REQUESTS))
			buildTabRequests(cellsMaps);
	}
	
	private void buildTabScopes(Collection<Map<Object,Object>> cellsMaps) {
		buildScopesDataTable(cellsMaps);
		buildScopeRequestsDataTable(cellsMaps,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION);
	}
	
	private void buildTabProfiles(Collection<Map<Object,Object>> cellsMaps) {
		buildProfilesDataTable(cellsMaps);
		buildProfileRequestsDataTable(cellsMaps);
	}
	
	private void buildTabRequests(Collection<Map<Object,Object>> cellsMaps) {
		buildScopeRequestsDataTable(cellsMaps,null);	
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	/**/
	
	private void buildScopesDataTable(Collection<Map<Object,Object>> cellsMaps) {
		scopeFilterController = MyAccountScopeListPage.instantiateFilterController().setScopeTypeRequestable(Boolean.TRUE);
		scopeFilterController.build();
		scopeFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_MY_VISIBILITIES);			
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return MyAccountScopeListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
						,new MyAccountScopeListPage.LazyDataModelListenerImpl().setFilterController(scopeFilterController),ScopeListPage.class,MyAccountScopeListPage.class
						,ScopeListPage.OUTCOME,OUTCOME,DataTable.ConfiguratorImpl.FIELD_TITLE_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Scope.LABEL);
			}
		},Cell.FIELD_WIDTH,12));
	}
	
	private void buildScopeRequestsDataTable(Collection<Map<Object,Object>> cellsMaps,String scopeTypeCode) {
		actorScopeRequestFilterController = MyAccountActorScopeRequestListPage.instantiateFilterController(scopeTypeCode).setScopeTypeRequestable(Boolean.TRUE);
		actorScopeRequestFilterController.build();
		actorScopeRequestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_MY_REQUESTS);
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				DataTable dataTable = MyAccountActorScopeRequestListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
						,new MyAccountActorScopeRequestListPage.LazyDataModelListenerImpl().setFilterController(actorScopeRequestFilterController)
						,ActorScopeRequestListPage.OUTCOME,OUTCOME
						,DataTable.ConfiguratorImpl.FIELD_TITLE_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.ActorScopeRequest.LABEL);
				if(selectedTab.getParameterValue().equals(TAB_MY_VISIBILITIES))
					dataTable.getFilterController().setRenderType(AbstractFilterController.RenderType.NONE);
				return dataTable;
			}
		},Cell.FIELD_WIDTH,12));		
	}
	
	private void buildProfilesDataTable(Collection<Map<Object,Object>> cellsMaps) {
		profileFilterController = MyAccountProfileListPage.instantiateFilterController();
		profileFilterController.build();
		profileFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_MY_PROFILES);			
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return MyAccountProfileListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
						,new MyAccountProfileListPage.LazyDataModelListenerImpl().setFilterController(profileFilterController),ProfileListPage.class,MyAccountProfileListPage.class
						,ProfileListPage.OUTCOME,OUTCOME,DataTable.ConfiguratorImpl.FIELD_TITLE_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.LABEL);
			}
		},Cell.FIELD_WIDTH,12));
	}
	
	private void buildProfileRequestsDataTable(Collection<Map<Object,Object>> cellsMaps) {
		actorProfileRequestFilterController = MyAccountActorProfileRequestListPage.instantiateFilterController();
		actorProfileRequestFilterController.build();
		actorProfileRequestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_MY_REQUESTS);
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				DataTable dataTable = MyAccountActorProfileRequestListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
						,new MyAccountActorProfileRequestListPage.LazyDataModelListenerImpl().setFilterController(actorProfileRequestFilterController)
						,ActorProfileRequestListPage.OUTCOME,OUTCOME
						,DataTable.ConfiguratorImpl.FIELD_TITLE_VALUE,ci.gouv.dgbf.system.actor.server.persistence.entities.ActorProfileRequest.LABEL);
				if(selectedTab.getParameterValue().equals(TAB_MY_VISIBILITIES))
					dataTable.getFilterController().setRenderType(AbstractFilterController.RenderType.NONE);
				return dataTable;
			}
		},Cell.FIELD_WIDTH,12));		
	}
	
	/**/
	
	private static final String TAB_MY_VISIBILITIES = "mesvisibilites";	
	private static final String TAB_MY_PROFILES = "mesprofiles";
	private static final String TAB_MY_REQUESTS = "mesdemandes";
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Mes visibilités",TAB_MY_VISIBILITIES)
		,new TabMenu.Tab("Mes profiles",TAB_MY_PROFILES)
		,new TabMenu.Tab("Mes demandes",TAB_MY_REQUESTS)
	);

	public static final String OUTCOME = "myAccountIndexView";
}