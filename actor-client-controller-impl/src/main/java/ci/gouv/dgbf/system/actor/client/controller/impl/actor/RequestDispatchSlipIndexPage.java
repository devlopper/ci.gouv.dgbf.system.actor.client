package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	private RequestDispatchSlipFilterController filterController,tabMenuFilterController;
	private TabMenu.Tab selectedTab;
	private Layout layout;
	private TabMenu tabMenu;
	private SelectOneCombo sectionSelectOne,functionSelectOne;
	private String sectionIdentifier,functionIdentifier;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new RequestDispatchSlipFilterController().initialize();	
	}
	
	@Override
	protected void __listenPostConstruct__() {
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		super.__listenPostConstruct__();
		filterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());		
		if(filterController.getSentInitial() == null)
			filterController.setSentInitial(Boolean.FALSE);
		if(filterController.getProcessedInitial() == null)
			filterController.setProcessedInitial(Boolean.FALSE);
		tabMenuFilterController = new RequestDispatchSlipFilterController(filterController);
		buildLayout();
	}
	
	/*private Long count(String tab) {
		Arguments<RequestDispatchSlip> arguments = new Arguments<RequestDispatchSlip>();
		arguments.queryIdentifierCountDynamic(RequestDispatchSlip.class);
		if(TAB_REQUEST_DISPATCH_SLIPS_TO_SEND.equals(tab)) {
			tabMenuFilterController.setSentInitial(null);
			tabMenuFilterController.setProcessedInitial(null);
		}else if(TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS.equals(tab)) {
			tabMenuFilterController.setSentInitial(Boolean.TRUE);
			tabMenuFilterController.setProcessedInitial(Boolean.FALSE);
		}else if(TAB_REQUEST_DISPATCH_SLIPS_PROCESSED.equals(tab)) {
			tabMenuFilterController.setSentInitial(Boolean.TRUE);
			tabMenuFilterController.setProcessedInitial(Boolean.TRUE);
		}
		arguments.getRepresentationArguments(Boolean.TRUE).getQueryExecutorArguments(Boolean.TRUE)
			.setFilter(RequestDispatchSlipFilterController.instantiateFilter(tabMenuFilterController, Boolean.TRUE));
		return EntityCounter.getInstance().count(RequestDispatchSlip.class, arguments);
	}*/
	
	private TabMenu buildTabMenu() {
		//Long total = 0l;//count(TAB_);
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(TabMenu.Tab tab : TABS) {
			//Long count = null;
			MenuItem menuItem = new MenuItem().setValue(tab.getName()).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			tabMenuItems.add(menuItem);
			
			String name;
			/*if(tab.getParameterValue().equals(TAB_REQUESTS_ALL)) {
				count = total;
				name = String.format("%s (%s)", tab.getName(),count);
			}else {
				if(NumberHelper.isEqualToZero(total)) {
					name = String.format("%s (%s)", tab.getName(),0);
				}else {					
					count = count(tab.getParameterValue());
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}
			}*/
			name = tab.getName();
			menuItem.setValue(name);
			
			if(TAB_REQUEST_DISPATCH_SLIPS_TO_SEND.equals(tab.getParameterValue())) {
				tabMenuFilterController.setSentInitial(null);
				tabMenuFilterController.setProcessedInitial(null);
			}else if(TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS.equals(tab.getParameterValue())) {
				tabMenuFilterController.setSentInitial(Boolean.TRUE);
				tabMenuFilterController.setProcessedInitial(Boolean.FALSE);
			}else if(TAB_REQUEST_DISPATCH_SLIPS_PROCESSED.equals(tab.getParameterValue())) {
				tabMenuFilterController.setSentInitial(Boolean.TRUE);
				tabMenuFilterController.setProcessedInitial(Boolean.TRUE);
			}
			Map<String,List<String>> parameters = tabMenuFilterController.asMap();
			if(parameters != null)
				menuItem.getParameters(Boolean.TRUE).putAll(parameters);
		}
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		return tabMenu;
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
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps) {
		if(selectedTab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_TO_SEND))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildTabRequestDispatchSlipsToSend();
				}
			},Cell.FIELD_WIDTH,12));
		else if(selectedTab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_SENT))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildTaRequestDispatchSlipsSent();
				}
			},Cell.FIELD_WIDTH,12));
		else if(selectedTab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildTabRequestDispatchSlipsToProcess();
				}
			},Cell.FIELD_WIDTH,12));
		else if(selectedTab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_PROCESSED))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildTaRequestDispatchSlipsProcessed();
				}
			},Cell.FIELD_WIDTH,12));
	}
	
	private DataTable buildTabRequestDispatchSlipsToSend() {
		filterController.setSentInitial(Boolean.FALSE);
		filterController.ignore(RequestDispatchSlipFilterController.FIELD_SENT_SELECT_ONE,RequestDispatchSlipFilterController.FIELD_PROCESSED_SELECT_ONE);
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,RequestDispatchSlipFilterController.class,filterController
				);
		return dataTable;
	}
	
	private DataTable buildTaRequestDispatchSlipsSent() {
		filterController.setSentInitial(Boolean.TRUE);
		filterController.ignore(RequestDispatchSlipFilterController.FIELD_SENT_SELECT_ONE,RequestDispatchSlipFilterController.FIELD_PROCESSED_SELECT_ONE);
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,RequestDispatchSlipFilterController.class,filterController
				);
		
		return dataTable;
	}
	
	private DataTable buildTabRequestDispatchSlipsToProcess() {
		filterController.setSentInitial(Boolean.TRUE).setProcessedInitial(Boolean.FALSE);
		filterController.ignore(RequestDispatchSlipFilterController.FIELD_SENT_SELECT_ONE,RequestDispatchSlipFilterController.FIELD_PROCESSED_SELECT_ONE);
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,RequestDispatchSlipFilterController.class,filterController
				);
		return dataTable;
	}
	
	private DataTable buildTaRequestDispatchSlipsProcessed() {
		filterController.setSentInitial(Boolean.TRUE).setProcessedInitial(Boolean.TRUE);
		filterController.ignore(RequestDispatchSlipFilterController.FIELD_SENT_SELECT_ONE,RequestDispatchSlipFilterController.FIELD_PROCESSED_SELECT_ONE);
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,RequestDispatchSlipFilterController.class,filterController
				);
		return dataTable;
	}

	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.RequestDispatchSlip.LABEL);
	}
	
	/**/
	
	public static final String TAB_REQUEST_DISPATCH_SLIPS_TO_SEND = "bordereaux_a_transmettre";
	public static final String TAB_REQUEST_DISPATCH_SLIPS_SENT = "bordereaux_transmis";
	public static final String TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS = "bordereaux_a_traiter";
	public static final String TAB_REQUEST_DISPATCH_SLIPS_PROCESSED = "bordereaux_traites";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Bordereaux à transmettre",TAB_REQUEST_DISPATCH_SLIPS_TO_SEND)
		//,new TabMenu.Tab("Bordereaux transmis",TAB_REQUEST_DISPATCH_SLIPS_SENT)
		,new TabMenu.Tab("Bordereaux à traiter",TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS)
		,new TabMenu.Tab("Bordereaux traités",TAB_REQUEST_DISPATCH_SLIPS_PROCESSED)
	);
	
	public static final String OUTCOME = "requestDispatchSlipIndexView";
}