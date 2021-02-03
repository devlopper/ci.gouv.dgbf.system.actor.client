package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.api.SectionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.entities.Profile;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private TabMenu tabMenu;
	private SelectOneCombo sectionSelectOne,functionSelectOne;
	private String sectionIdentifier,functionIdentifier;
	
	@Override
	protected void __listenPostConstruct__() {
		contentOutputPanel.setDeferred(Boolean.TRUE);
		super.__listenPostConstruct__();
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		TabMenu.Tab selectedTab = TabMenu.Tab.getSelectedByRequestParameter(TABS);
		buildGlobalFilters(cellsMaps,selectedTab);
		buildTabMenu(cellsMaps,selectedTab);
		buildTab(cellsMaps,selectedTab);
		buildLayout(cellsMaps);
	}
	
	private void buildGlobalFilters(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {
		buildGlobalFiltersSelectOneSection(cellsMaps,selectedTab);
		buildGlobalFiltersSelectOneFunction(cellsMaps,selectedTab);
	}
	
	private void buildGlobalFiltersSelectOneSection(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {
		sectionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Section.class));
		List<Section> sections = (List<Section>) __inject__(SectionController.class).readVisiblesByLoggedInActorCodeForUI();
		if(CollectionHelper.getSize(sections) > 1)
			sections.add(0, null);
		if(StringHelper.isBlank(sectionIdentifier) && CollectionHelper.getSize(sections) == 1)
			sectionIdentifier = CollectionHelper.getFirst(sections).getIdentifier();

		sectionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Section.class,SelectOneCombo.FIELD_CHOICES,sections
				,SelectOneCombo.FIELD_CHOICES_INITIALIZED,Boolean.TRUE);
		if(StringHelper.isNotBlank(sectionIdentifier))
			sectionSelectOne.selectBySystemIdentifier(sectionIdentifier);
		sectionSelectOne.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@Override
			protected void select(AbstractAction action, Object value) {
				Map<String,List<String>> map = new LinkedHashMap<>();
				map.put(TabMenu.Tab.PARAMETER_NAME,List.of(selectedTab.getParameterValue()));
				if(value != null)
					map.put(ParameterName.stringify(Section.class),List.of(((Section)value).getIdentifier()));
				if(functionSelectOne != null && functionSelectOne.getValue() != null)
					map.put(ParameterName.stringify(Function.class),List.of(((Function)functionSelectOne.getValue()).getIdentifier()));
				
				Redirector.getInstance().redirect(OUTCOME, map);
			}
		}, List.of());
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,sectionSelectOne,Cell.FIELD_WIDTH,9));	
	}
	
	private void buildGlobalFiltersSelectOneFunction(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {
		functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		List<Function> functions = (List<Function>) __inject__(FunctionController.class).readCreditManagers();
		if(CollectionHelper.getSize(functions) > 1)
			functions.add(0, null);
		if(StringHelper.isBlank(functionIdentifier) && CollectionHelper.getSize(functions) == 1)			
			functionIdentifier = CollectionHelper.getFirst(functions).getIdentifier();
		functionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_CHOICES,functions
				,SelectOneCombo.FIELD_CHOICES_INITIALIZED,Boolean.TRUE);
		if(StringHelper.isNotBlank(functionIdentifier))
			functionSelectOne.selectBySystemIdentifier(functionIdentifier);
		functionSelectOne.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@Override
			protected void select(AbstractAction action, Object value) {
				Map<String,List<String>> map = new LinkedHashMap<>();
				map.put(TabMenu.Tab.PARAMETER_NAME,List.of(selectedTab.getParameterValue()));
				if(value != null)
					map.put(ParameterName.stringify(Function.class),List.of(((Function)value).getIdentifier()));
				if(sectionSelectOne != null && sectionSelectOne.getValue() != null)
					map.put(ParameterName.stringify(Section.class),List.of(((Section)sectionSelectOne.getValue()).getIdentifier()));
				
				Redirector.getInstance().redirect(OUTCOME, map);
			}
		}, List.of());
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Catégorie de fonction budgétaire"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,9));	
	}
	
	private void buildTabMenu(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab selectedTab) {		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(TabMenu.Tab tab : TABS) {
			String name;
			if(tab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS)) {
				if(SessionManager.getInstance().isUserHasRole(Profile.CODE_CHARGE_ETUDE_DAS)) {
					name = tab.getName();
				}else {
					name = "Bordereaux transmis";
				}
			}else {
				name = tab.getName();
			}			
			MenuItem menuItem = new MenuItem().setValue(name).addParameter(TabMenu.Tab.PARAMETER_NAME, tab.getParameterValue());
			if(sectionSelectOne != null && sectionSelectOne.getValue() != null)
				menuItem.addParameter(ParameterName.stringify(Section.class), (String)FieldHelper.readSystemIdentifier(sectionSelectOne.getValue()));
			if(functionSelectOne != null && functionSelectOne.getValue() != null)
				menuItem.addParameter(ParameterName.stringify(Function.class), (String)FieldHelper.readSystemIdentifier(functionSelectOne.getValue()));
			tabMenuItems.add(menuItem);
		}
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		if(tab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_TO_SEND))
			buildTabRequestDispatchSlipsToSend(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_SENT))
			buildTaRequestDispatchSlipsSent(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS))
			buildTabRequestDispatchSlipsToProcess(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_REQUEST_DISPATCH_SLIPS_PROCESSED))
			buildTaRequestDispatchSlipsProcessed(cellsMaps);
	}
	
	private Collection<String> buildRequestDataTableColumnsFieldsNames(Boolean sent,Boolean processed) {
		Collection<String> columnsFieldsNames = new ArrayList<>();
		columnsFieldsNames.add(RequestDispatchSlip.FIELD_CODE);
		if(StringHelper.isBlank(sectionIdentifier))
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_SECTION_AS_STRING);
		if(StringHelper.isBlank(functionIdentifier))
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_FUNCTION_AS_STRING);
		columnsFieldsNames.add(RequestDispatchSlip.FIELD_CREATION_DATE_AS_STRING);
		if(Boolean.TRUE.equals(sent))
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_SENDING_DATE_AS_STRING);
		if(Boolean.TRUE.equals(processed))
			columnsFieldsNames.add(RequestDispatchSlip.FIELD_PROCESSING_DATE_AS_STRING);
		return columnsFieldsNames;
	}
	
	private void buildTabRequestDispatchSlipsToSend(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,buildRequestDataTableColumnsFieldsNames(null,null)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestDispatchSlipListPage.LazyDataModelListenerImpl()
				.setSectionIdentifier(sectionIdentifier).setFunctionIdentifier(functionIdentifier)
				.setSendingDateIsNullNullable(Boolean.FALSE)
				//.setSendingDateIsNotNullNullable(Boolean.TRUE)
				//.setProcessingDateIsNullNullable(Boolean.TRUE).setProcessingDateIsNotNullNullable(Boolean.TRUE)
				);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTaRequestDispatchSlipsSent(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,buildRequestDataTableColumnsFieldsNames(Boolean.TRUE,null)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestDispatchSlipListPage.LazyDataModelListenerImpl()
				.setSectionIdentifier(sectionIdentifier).setFunctionIdentifier(functionIdentifier)
				//.setSendingDateIsNullNullable(Boolean.TRUE)
				.setSendingDateIsNotNullNullable(Boolean.FALSE)
				.setProcessingDateIsNullNullable(Boolean.FALSE)
				//.setProcessingDateIsNotNullNullable(Boolean.TRUE)
				);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabRequestDispatchSlipsToProcess(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,buildRequestDataTableColumnsFieldsNames(Boolean.TRUE,null)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestDispatchSlipListPage.LazyDataModelListenerImpl()
				.setSectionIdentifier(sectionIdentifier).setFunctionIdentifier(functionIdentifier)
				//.setSendingDateIsNullNullable(Boolean.TRUE)
				.setSendingDateIsNotNullNullable(Boolean.FALSE)
				.setProcessingDateIsNullNullable(Boolean.FALSE)
				//.setProcessingDateIsNotNullNullable(Boolean.TRUE)
				);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTaRequestDispatchSlipsProcessed(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestDispatchSlipListPage.buildDataTable(
				Section.class,sectionSelectOne == null ? null : sectionSelectOne.getValue(),Function.class,functionSelectOne == null ? null : functionSelectOne.getValue()
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,buildRequestDataTableColumnsFieldsNames(Boolean.TRUE,Boolean.TRUE)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestDispatchSlipListPage.LazyDataModelListenerImpl()
				.setSectionIdentifier(sectionIdentifier).setFunctionIdentifier(functionIdentifier)
				//.setSendingDateIsNullNullable(Boolean.TRUE)
				//.setSendingDateIsNotNullNullable(Boolean.FALSE)
				//.setProcessingDateIsNullNullable(Boolean.TRUE)
				.setProcessingDateIsNotNullNullable(Boolean.FALSE)
				);	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Bordereaux de demandes";
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