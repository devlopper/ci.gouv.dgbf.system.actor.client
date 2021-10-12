package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.api.RequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	private RequestFilterController requestFilterController;
	private TabMenu.Tab selectedTab,selectedRequestTab;
	private Layout layout;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		requestFilterController = new RequestFilterController();		
	}
	
	@Override
	protected void __listenPostConstruct__() {
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		super.__listenPostConstruct__();
		if(requestFilterController.getProcessedInitial() == null && TAB_REQUESTS_TO_PROCESS.equals(selectedTab.getParameterValue()))
			requestFilterController.setProcessedInitial(Boolean.FALSE);
		requestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());
		buildLayout();
	}
	
	private TabMenu buildTabMenu() {
		Collection<MenuItem> tabMenuItems = new ArrayList<>();		
		Long total = __inject__(RequestController.class).countByProcessed(null);
		for(TabMenu.Tab tab : TABS) {
			MenuItem menuItem = new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			tabMenuItems.add(menuItem);
			Long count = null;
			String name;
			if(tab.getParameterValue().equals(TAB_REQUESTS_ALL)) {
				count = total;
				name = String.format("%s (%s)", tab.getName(),count);
			}else {
				if(NumberHelper.isEqualToZero(total)) {
					name = String.format("%s (%s)", tab.getName(),0);
				}else {
					count = __inject__(RequestController.class).countByProcessed(tab.getParameterValue().equals(TAB_REQUESTS_PROCESSED));
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}
			}
			menuItem.setValue(name);
			Map<String,List<String>> parameters = requestFilterController.asMap();
			if(tab.getParameterValue().equals(TAB_REQUESTS_ALL)) {
				parameters.remove(Request.FIELD_PROCESSED);
			}else if(tab.getParameterValue().equals(TAB_REQUESTS_TO_PROCESS)) {
				parameters.put(Request.FIELD_PROCESSED,List.of(Boolean.FALSE.toString()));
			}else if(tab.getParameterValue().equals(TAB_REQUESTS_PROCESSED)) {
				parameters.put(Request.FIELD_PROCESSED,List.of(Boolean.TRUE.toString()));
			}
			menuItem.getParameters(Boolean.TRUE).putAll(parameters);
		}
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		return tabMenu;
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps) {
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
		requestFilterController.ignore(RequestFilterController.FIELD_PROCESSED_SELECT_ONE);
		DataTable dataTable = RequestListPage.buildDataTable(RequestFilterController.class,requestFilterController);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsProcessed() {
		requestFilterController.ignore(RequestFilterController.FIELD_PROCESSED_SELECT_ONE);
		DataTable dataTable = RequestListPage.buildDataTable(RequestFilterController.class,requestFilterController);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsAll() {
		DataTable dataTable = RequestListPage.buildDataTable(RequestFilterController.class,requestFilterController);
		return dataTable;
	}
	
	/*
	private void buildGlobalFilters(Collection<Map<Object,Object>> cellsMaps) {
		requestFilterController = new RequestFilterController();
		requestFilterController.ignore(RequestFilterController.FIELD_NAME_SECTION_SELECT_ONE).build();
		requestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,requestFilterController.getLayout(),Cell.FIELD_WIDTH,12));
	}
	*/

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