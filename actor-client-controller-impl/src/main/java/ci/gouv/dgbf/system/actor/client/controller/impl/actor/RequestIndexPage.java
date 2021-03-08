package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.number.NumberHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityCounter;
import org.cyk.utility.persistence.query.QueryExecutorArguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestListPage.ContentType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	private TabMenu.Tab selectedTab,selectedRequestTab;
	private Layout layout;
	
	private RequestFilterController requestFilterController;
	
	@Override
	protected void __listenPostConstruct__() {
		selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		super.__listenPostConstruct__();
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();		
		buildGlobalFilters(cellsMaps);	
		buildTabMenu(cellsMaps,selectedTab);
		buildTab(cellsMaps,selectedTab);
		buildLayout(cellsMaps);
	}
	
	private void buildTabMenu(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		String functionIdentifier = (String) FieldHelper.readSystemIdentifier(requestFilterController.getFunction());
		String sectionIdentifier = (String) FieldHelper.readSystemIdentifier(requestFilterController.getSection());
		String administrativeUnitIdentifier = (String) FieldHelper.readSystemIdentifier(requestFilterController.getAdministrativeUnit());
		String budgetSpecializationUnitIdentifier = (String) FieldHelper.readSystemIdentifier(requestFilterController.getBudgetSpecializationUnit());
		Long total = count(TAB_REQUESTS_ALL,functionIdentifier, sectionIdentifier,administrativeUnitIdentifier, budgetSpecializationUnitIdentifier);
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
					count = count(tab.getParameterValue(), functionIdentifier, sectionIdentifier, administrativeUnitIdentifier, budgetSpecializationUnitIdentifier);
					name = String.format("%s (%s|%s)", tab.getName(),count,NumberHelper.computePercentageAsInteger(count, total)+"%");
				}				
			}
			menuItem.setValue(name);
			menuItem.addParameterFromInstance(requestFilterController.getSection());
			menuItem.addParameterFromInstance(requestFilterController.getFunction());
		}
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		
		//functionIdentifier = Helper.addCreditManagersAuthorizingOfficersFinancialControllersAssistantsTabMenu(cellsMaps, OUTCOME, TABS, selectedTab.getParameterValue(),functionIdentifier);
	}
	
	private Long count(String value,String functionIdentifier,String sectionIdentifier, String administrativeUnitIdentifier, String budgetSpecializationUnitIdentifier) {
		Arguments<Request> arguments = new Arguments<>();
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setQueryExecutorArguments(
				new QueryExecutorArguments.Dto().setQueryIdentifier(RequestQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)));
		if(value.equals(TAB_REQUESTS_PROCESSED)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NOT_NULL,Boolean.TRUE);
		}else if(value.equals(TAB_REQUESTS_TO_PROCESS)) {
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_PROCESSING_DATE_IS_NULL,Boolean.TRUE);
		}else if(value.equals(TAB_REQUESTS_ALL)) {
			
		}
		if(StringHelper.isNotBlank(sectionIdentifier))
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT_SECTION_IDENTIFIER, sectionIdentifier);
		if(StringHelper.isNotBlank(functionIdentifier))
			arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_FUNCTION_IDENTIFIER, functionIdentifier);
		//arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_ADMINISTRATIVE_UNIT, administrativeUnitIdentifier);
		//arguments.getRepresentationArguments().getQueryExecutorArguments().addFilterField(RequestQuerier.PARAMETER_NAME_BUDGET_SPECIALIZATION_UNIT, budgetSpecializationUnitCode);			
		return EntityCounter.getInstance().count(Request.class,arguments);
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		RequestListPage.LazyDataModelListenerImpl lazyDataModelListener = new RequestListPage.LazyDataModelListenerImpl();
		lazyDataModelListener.setSectionIdentifier(StringHelper.get(FieldHelper.readSystemIdentifier(requestFilterController.getSection())));
		lazyDataModelListener.setAdministrativeUnitIdentifier(StringHelper.get(FieldHelper.readSystemIdentifier(requestFilterController.getAdministrativeUnit())));
		lazyDataModelListener.setFunctionIdentifier(StringHelper.get(FieldHelper.readSystemIdentifier(requestFilterController.getFunction())));
		if(tab.getParameterValue().equals(TAB_REQUESTS_TO_PROCESS))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsToProcess(lazyDataModelListener);
				}
			},Cell.FIELD_WIDTH,12));
		else if(tab.getParameterValue().equals(TAB_REQUESTS_PROCESSED))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsProcessed(lazyDataModelListener);
				}
			},Cell.FIELD_WIDTH,12));
		else if(tab.getParameterValue().equals(TAB_REQUESTS_ALL))
			cellsMaps.add(MapHelper.instantiate(Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
				@Override
				public Object buildControl(Cell cell) {
					return buildDataTableRequestsAll(lazyDataModelListener);
				}
			},Cell.FIELD_WIDTH,12));
	}
	
	private DataTable buildDataTableRequestsToProcess(RequestListPage.LazyDataModelListenerImpl lazyDataModelListener) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.TO_PROCESS
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,RequestListPage.DataTableListenerImpl
					.buildColumnsNames(requestFilterController.getFunction(),requestFilterController.getSection(), requestFilterController.getAdministrativeUnit()
							, requestFilterController.getBudgetSpecializationUnit(), ContentType.TO_PROCESS)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				.setProcessingDateIsNullNullable(Boolean.FALSE)
				);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsProcessed(RequestListPage.LazyDataModelListenerImpl lazyDataModelListener) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.PROCESSED
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,RequestListPage.DataTableListenerImpl
				.buildColumnsNames(requestFilterController.getFunction(),requestFilterController.getSection(), requestFilterController.getAdministrativeUnit()
						, requestFilterController.getBudgetSpecializationUnit(), ContentType.PROCESSED)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				.setProcessingDateIsNotNullNullable(Boolean.FALSE)
				);
		return dataTable;
	}
	
	private DataTable buildDataTableRequestsAll(RequestListPage.LazyDataModelListenerImpl lazyDataModelListener) {
		DataTable dataTable = RequestListPage.buildDataTable(
				RequestListPage.ContentType.class,RequestListPage.ContentType.ALL
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,RequestListPage.DataTableListenerImpl
				.buildColumnsNames(requestFilterController.getFunction(),requestFilterController.getSection(), requestFilterController.getAdministrativeUnit()
						, requestFilterController.getBudgetSpecializationUnit(), ContentType.ALL)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,lazyDataModelListener
				);
		return dataTable;
	}
	
	private void buildGlobalFilters(Collection<Map<Object,Object>> cellsMaps) {
		requestFilterController = new RequestFilterController()/*.ignore(RequestFilterController.FIELD_NAME_SECTION_SELECT_ONE)*/.build();
		requestFilterController.getOnSelectRedirectorArguments(Boolean.TRUE).outcome(OUTCOME).addParameter(TabMenu.Tab.PARAMETER_NAME, selectedTab.getParameterValue());	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,requestFilterController.getLayout(),Cell.FIELD_WIDTH,12));
	}
	

	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return RequestListPage.buildWindowTitleValue(selectedTab.getName(),requestFilterController.getFunction(), requestFilterController.getSection()
				, requestFilterController.getAdministrativeUnit(), requestFilterController.getBudgetSpecializationUnit());
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