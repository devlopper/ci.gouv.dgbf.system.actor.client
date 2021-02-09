package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private TabMenu tabMenu;
	private String functionIdentifier;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
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
		
		functionIdentifier = Helper.addCreditManagersAuthorizingOfficersFinancialControllersAssistantsTabMenu(cellsMaps, OUTCOME, TABS, selectedTab.getParameterValue(),functionIdentifier);
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		if(tab.getParameterValue().equals(TAB_REQUESTS_TO_PROCESS))
			buildTabRequestsToProcess(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_REQUESTS_PROCESSED))
			buildTaRequestsProcessed(cellsMaps);
	}
	
	private void buildTabRequestsToProcess(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.TO_PROCESS
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestListPage.LazyDataModelListenerImpl()
				.setFunctionIdentifier(functionIdentifier)
				.setProcessingDateIsNullNullable(Boolean.FALSE)
				);	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTaRequestsProcessed(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.PROCESSED
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestListPage.LazyDataModelListenerImpl()
				.setFunctionIdentifier(functionIdentifier)
				.setProcessingDateIsNotNullNullable(Boolean.FALSE)
				);	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Demandes";
	}
	
	/**/
	
	public static final String TAB_REQUESTS_TO_PROCESS = "demandes_a_traiter";
	public static final String TAB_REQUESTS_PROCESSED = "demandes_traitees";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Demandes à traiter",TAB_REQUESTS_TO_PROCESS)
		,new TabMenu.Tab("Demandes traitées",TAB_REQUESTS_PROCESSED)
	);
	
	public static final String OUTCOME = "requestIndexView";
}