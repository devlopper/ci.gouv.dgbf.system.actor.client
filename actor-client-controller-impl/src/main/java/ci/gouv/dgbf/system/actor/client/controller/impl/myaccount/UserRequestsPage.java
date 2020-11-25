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
import org.cyk.utility.client.controller.web.jsf.primefaces.data.Form;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestListPage;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class UserRequestsPage extends AbstractPageContainerManagedImpl implements MyAccountTheme,Serializable {

	private Layout layout;
	private TabMenu tabMenu;
	
	@Override
	protected void __listenPostConstruct__() {		
		super.__listenPostConstruct__();
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		TabMenu.Tab selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		buildTabMenu(cellsMaps,selectedTab);
		buildTab(cellsMaps,selectedTab);
		buildLayout(cellsMaps);
	}
	
	private void buildTabMenu(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(TabMenu.Tab tab : TABS)
			tabMenuItems.add(new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue()));	
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		if(tab.getParameterValue().equals(TAB_LIST))
			buildTabRequestList(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_CREATE))
			buildTabNewRequest(cellsMaps);
	}
	
	private void buildTabRequestList(Collection<Map<Object,Object>> cellsMaps) {
		Actor actor = __inject__(ActorController.class).getLoggedIn();
		DataTable dataTable = RequestListPage.buildDataTable(
				DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestListPage.LazyDataModelListenerImpl().setActorIdentifier(actor.getIdentifier())
				,RequestListPage.class,UserRequestsPage.class
				);	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabNewRequest(Collection<Map<Object,Object>> cellsMaps) {
		Form form = RequestInitializePage.buildForm();
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,form.getLayout(),Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Mes demandes";
	}
	
	/**/
	
	public static final String TAB_LIST = "list";
	public static final String TAB_CREATE = "create";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Liste des demandes",TAB_LIST)
		,new TabMenu.Tab("Nouvelle demande",TAB_CREATE)
	);
	
	public static final String OUTCOME = "myAccountRequestsView";
}