package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.computation.SortOrder;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityCounter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	protected RequestFilterController requestFilterController,tabMenuRequestFilterController;
	private TabMenu.Tab selectedTab,selectedRequestTab;
	private Layout layout;
	
	protected String outcome;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		outcome = OUTCOME;
		requestFilterController = new RequestFilterController().initialize();	
	}
	
	@Override
	protected void __listenPostConstruct__() {
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		super.__listenPostConstruct__();
		if(requestFilterController.getProcessedInitial() == null && TAB_REQUESTS_TO_PROCESS.equals(selectedTab.getParameterValue()))
			requestFilterController.setProcessedInitial(Boolean.FALSE);
		requestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());
		tabMenuRequestFilterController = new RequestFilterController(requestFilterController);
		buildLayout();
	}
	
	private Long count(String tab) {
		if(requestFilterController.isAdministrativeUnitRequired() && requestFilterController.getAdministrativeUnitInitial() == null) {
			return 0L;
		}
		Arguments<Request> arguments = new Arguments<Request>();
		arguments.queryIdentifierCountDynamic(Request.class);		
		if(TAB_REQUESTS_ALL.equals(tab)) {
			tabMenuRequestFilterController.setProcessedInitial(null);
		}else if(TAB_REQUESTS_TO_PROCESS.equals(tab)) {
			tabMenuRequestFilterController.setProcessedInitial(Boolean.FALSE);
		}else if(TAB_REQUESTS_PROCESSED.equals(tab)) {
			tabMenuRequestFilterController.setProcessedInitial(Boolean.TRUE);
		}
		arguments.getRepresentationArguments(Boolean.TRUE).getQueryExecutorArguments(Boolean.TRUE)
			.setFilter(RequestFilterController.instantiateFilter(tabMenuRequestFilterController, Boolean.TRUE));
		return EntityCounter.getInstance().count(Request.class, arguments);
	}
	
	private TabMenu buildTabMenu() {
		Long total = count(TAB_REQUESTS_ALL);
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(TabMenu.Tab tab : TABS) {
			Long count = null;
			MenuItem menuItem = new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			tabMenuItems.add(menuItem);
			
			String name;
			if(tab.getParameterValue().equals(TAB_REQUESTS_ALL)) {
				count = total;
				name = String.format("%s (%s)", tab.getName(),count);
			}else {
				if(NumberHelper.isEqualToZero(total)) {
					name = String.format("%s (%s)", tab.getName(),0);
				}else {					
					count = count(tab.getParameterValue());
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}
			}
			menuItem.setValue(name);
			
			if(TAB_REQUESTS_ALL.equals(tab.getParameterValue())) {
				tabMenuRequestFilterController.setProcessedInitial(null);
			}else if(TAB_REQUESTS_TO_PROCESS.equals(tab.getParameterValue())) {
				tabMenuRequestFilterController.setProcessedInitial(Boolean.FALSE);
			}else if(TAB_REQUESTS_PROCESSED.equals(tab.getParameterValue())) {
				tabMenuRequestFilterController.setProcessedInitial(Boolean.TRUE);
			}
			Map<String,List<String>> parameters = tabMenuRequestFilterController.asMap();
			if(parameters != null)
				menuItem.getParameters(Boolean.TRUE).putAll(parameters);
		}
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,outcome,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		return tabMenu;
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps) {
		buildFilter(cellsMaps);
		
		if (requestFilterController.isAdministrativeUnitRequired() && requestFilterController.getAdministrativeUnitInitial() == null) {
			return;
		}
		if(selectedTab.getParameterValue().equals(TAB_REQUESTS_TO_PROCESS))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsToProcess();
				}
			},Cell.FIELD_WIDTH,12));
		else if(selectedTab.getParameterValue().equals(TAB_REQUESTS_PROCESSED))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsProcessed();
				}
			},Cell.FIELD_WIDTH,12));
		else if(selectedTab.getParameterValue().equals(TAB_REQUESTS_ALL))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsAll();
				}
			},Cell.FIELD_WIDTH,12));
	}
	
	private DataTable buildDataTableRequestsToProcess() {
		requestFilterController.setProcessedInitial(Boolean.FALSE);
		requestFilterController.ignore(RequestFilterController.FIELD_PROCESSED_SELECT_ONE);
		requestFilterController.order(Request.FIELD_CREATION_DATE, SortOrder.DESCENDING);
		DataTable dataTable = RequestListPage.buildDataTable(RequestFilterController.class,requestFilterController);
		dataTable.setIsFilterControllerGettable(false);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsProcessed() {
		requestFilterController.setProcessedInitial(Boolean.TRUE);
		requestFilterController.ignore(RequestFilterController.FIELD_PROCESSED_SELECT_ONE);
		requestFilterController.order(Request.FIELD_PROCESSING_DATE, SortOrder.DESCENDING);
		DataTable dataTable = RequestListPage.buildDataTable(RequestFilterController.class,requestFilterController);
		dataTable.setIsFilterControllerGettable(false);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsAll() {
		requestFilterController.order(Request.FIELD_CREATION_DATE, SortOrder.DESCENDING);
		DataTable dataTable = RequestListPage.buildDataTable(RequestFilterController.class,requestFilterController);
		dataTable.setIsFilterControllerGettable(false);
		return dataTable;
	}
	
	private void buildFilter(Collection<Map<Object,Object>> cellsMaps) {		
		requestFilterController.build();
		requestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(outcome).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());			
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,requestFilterController.getLayout(),Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildTabMenu();
			}
		},Cell.FIELD_WIDTH,12));
		
		buildTab(cellsMaps);
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(requestFilterController == null)
			return super.__getWindowTitleValue__();
		return requestFilterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.Request.LABEL);
	}
	
	/**/
	
	public static final String TAB_REQUESTS_TO_PROCESS = "demandes_a_traiter";
	public static final String TAB_REQUESTS_PROCESSED = "demandes_traitees";
	public static final String TAB_REQUESTS_ALL = "demandes_toutes";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Demandes à traiter",TAB_REQUESTS_TO_PROCESS)
		,new TabMenu.Tab("Demandes traitées",TAB_REQUESTS_PROCESSED)
		,new TabMenu.Tab("Toutes les demandes",TAB_REQUESTS_ALL)
	);
	
	public static final String OUTCOME = "requestIndexView";
}