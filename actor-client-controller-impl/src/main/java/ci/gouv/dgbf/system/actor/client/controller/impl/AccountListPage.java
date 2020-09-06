package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityCounter;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.AccountRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.RejectedAccountRequest;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AccountRequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RejectedAccountRequestQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.SectionQuerier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountListPage extends AbstractPageContainerManagedImpl implements Serializable {

	private String type;
	private TabMenu tabMenu;
	private DataTable dataTable;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		Tab selectedTab = Tab.getByParameterValue(WebController.getInstance().getRequestParameter(Tab.PARAMETER_NAME));
		if(selectedTab == null)
			selectedTab = Tab.ACTOR;
		super.__listenPostConstruct__();
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		for(Tab tab : Tab.values()) {
			String name = tab.name;
			if(Tab.ACTOR.equals(tab))
				name += "("+EntityCounter.getInstance().count(Actor.class,ActorQuerier.QUERY_IDENTIFIER_COUNT_ALL_01)+")";
			else if(Tab.REQUEST.equals(tab))
				name += "("+EntityCounter.getInstance().count(AccountRequest.class,AccountRequestQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)+" | "+
						EntityCounter.getInstance().count(RejectedAccountRequest.class,RejectedAccountRequestQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)+")";
			tabMenuItems.add(new MenuItem().setValue(name).addParameter(Tab.PARAMETER_NAME, tab.parameterValue));
		}
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>(); 
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"accountListView",TabMenu.FIELD_ACTIVE_INDEX,selectedTab.ordinal()
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
		
		if(Tab.ACTOR.equals(selectedTab))
			dataTable = ActorListPage.buildDataTable();
		else if(Tab.FUNCTIONS.equals(selectedTab)) {
			String code = WebController.getInstance().getRequestParameter(AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl.FIELD_CODE);
			Collection<Function> functions = EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ);
			if(CollectionHelper.isNotEmpty(functions)) {
				if(StringHelper.isBlank(code))
					code = ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ADMINISTRATEUR;
				dataTable = ActorListPage.buildDataTable(Function.class,code);	
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildFunctionsSelectOneCombo(functions,code),Cell.FIELD_WIDTH,12));				
			}		
		}//else if(Tab.ADMINISTRATEUR.equals(selectedTab))
		//	dataTable = ActorListPage.buildDataTable(Function.class,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ADMINISTRATEUR);
		else if(Tab.VISIBLE_SECTIONS.equals(selectedTab)) {
			String code = WebController.getInstance().getRequestParameter(AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl.FIELD_CODE);
			Collection<Section> sections = EntityReader.getInstance().readMany(Section.class, SectionQuerier.QUERY_IDENTIFIER_READ);
			if(CollectionHelper.isNotEmpty(sections)) {
				if(StringHelper.isBlank(code))
					code = CollectionHelper.getFirst(sections).getCode();
				dataTable = ActorListPage.buildDataTable(Section.class,code);
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildVisibleSectionsSelectOneCombo(sections,code),Cell.FIELD_WIDTH,12));				
			}			
		}
		else if(Tab.REQUEST.equals(selectedTab)) {
			RequestTab selectedRequestTab = RequestTab.getByParameterValue(WebController.getInstance().getRequestParameter(RequestTab.PARAMETER_NAME));
			if(selectedRequestTab == null)
				selectedRequestTab = RequestTab.TO_PROCESS;
			Collection<MenuItem> requestTabMenuItems = new ArrayList<>();
			for(RequestTab tab : RequestTab.values()) {
				String name = tab.name;
				if(RequestTab.TO_PROCESS.equals(tab))
					name += "("+EntityCounter.getInstance().count(AccountRequest.class,AccountRequestQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)+")";
				else if(RequestTab.REJECTED.equals(tab))
					name += "("+EntityCounter.getInstance().count(RejectedAccountRequest.class,RejectedAccountRequestQuerier.QUERY_IDENTIFIER_COUNT_WHERE_FILTER)+")";
				requestTabMenuItems.add(new MenuItem().setValue(name).addParameter(Tab.PARAMETER_NAME, Tab.REQUEST.parameterValue)
						.addParameter(RequestTab.PARAMETER_NAME, tab.parameterValue));
			}
			TabMenu requestTabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"accountListView",TabMenu.FIELD_ACTIVE_INDEX,selectedRequestTab.ordinal()
					,TabMenu.ConfiguratorImpl.FIELD_ITEMS,requestTabMenuItems);	
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,requestTabMenu,Cell.FIELD_WIDTH,12));			
			if(RequestTab.REJECTED.equals(selectedRequestTab))
				dataTable = RejectedAccountRequestListPage.buildDataTable();
			else
				dataTable = AccountRequestListPage.buildDataTable();							
		}
		if(dataTable != null)
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	public static TabMenu buildFunctionsTab(Collection<Function> functions,String code) {
		if(CollectionHelper.isEmpty(functions))
			return null;		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer activeIndex = null;
		Integer count = 0;
		for(Function function : functions) {
			tabMenuItems.add(new MenuItem().setValue(function.getCode()).addParameter(Tab.PARAMETER_NAME, Tab.FUNCTIONS.parameterValue).addParameter(AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl.FIELD_CODE, function.getCode()));
			if(activeIndex == null && function.getCode().equals(code))
				activeIndex = count;
			count++;
		}
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"accountListView",TabMenu.FIELD_ACTIVE_INDEX,activeIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
	}
	
	@SuppressWarnings("unchecked")
	public SelectOneCombo buildFunctionsSelectOneCombo(Collection<Function> functions,String code) {
		if(CollectionHelper.isEmpty(functions))
			return null;		
		SelectOneCombo selectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICES,functions);
		selectOneCombo.selectByBusinessIdentifier(code);
		selectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@Override
			protected void select(AbstractAction action, Object value) {
				((AbstractActorListPage.LazyDataModelListenerImpl)((LazyDataModel<Actor>)dataTable.getValue()).getListener())
					.setFunctionCode((String)FieldHelper.readBusinessIdentifier(value));
			}
		}, List.of(dataTable));
		return selectOneCombo;
	}
	
	public static TabMenu buildSectionsTab(Collection<Section> sections,String code) {
		if(CollectionHelper.isEmpty(sections))
			return null;		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer activeIndex = null;
		Integer count = 0;
		for(Section section : sections) {
			tabMenuItems.add(new MenuItem().setValue(section.getCode()).addParameter(Tab.PARAMETER_NAME, Tab.VISIBLE_SECTIONS.parameterValue).addParameter(AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl.FIELD_CODE, section.getCode()));
			if(activeIndex == null && section.getCode().equals(code))
				activeIndex = count;
			count++;
		}
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"accountListView",TabMenu.FIELD_ACTIVE_INDEX,activeIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
	}
	
	public SelectOneCombo buildVisibleSectionsSelectOneCombo(Collection<Section> sections,String code) {
		if(CollectionHelper.isEmpty(sections))
			return null;		
		SelectOneCombo selectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICES,sections);
		selectOneCombo.selectByBusinessIdentifier(code);
		selectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			protected void select(AbstractAction action, Object value) {
				((AbstractActorListPage.LazyDataModelListenerImpl)((LazyDataModel<Actor>)dataTable.getValue()).getListener())
					.setVisibleSectionCode((String)FieldHelper.readBusinessIdentifier(value));
			}
		}, List.of(dataTable));
		return selectOneCombo;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Comptes";
	}
	
	/**/
	
	@AllArgsConstructor @Getter
	public static enum Tab {
		ACTOR("Liste des comptes","liste_comptes")
		,FUNCTIONS("Liste des comptes par fonction","liste_comptes_par_fonction")
		,VISIBLE_SECTIONS("Liste des comptes par section visible","liste_comptes_par_section_visible")
		,REQUEST("Liste des demandes de comptes","liste_demandes")
		
		;
		private String name;
		private String parameterValue;
		
		public static Tab getByParameterValue(String value) {
			for(Tab tab : Tab.values())
				if(tab.parameterValue.equals(value))
					return tab;
			return null;
		}
		
		public static final String PARAMETER_NAME = "tab";
	}
	
	@AllArgsConstructor @Getter
	public static enum RequestTab {
		TO_PROCESS("Liste des demandes de comptes à traiter","liste_demandes_a_traiter")
		,REJECTED("Liste des demandes de comptes rejetées","liste_demandes_rejetees")
		
		;
		private String name;
		private String parameterValue;
		
		public static RequestTab getByParameterValue(String value) {
			for(RequestTab tab : RequestTab.values())
				if(tab.parameterValue.equals(value))
					return tab;
			return null;
		}
		public static final String PARAMETER_NAME = "request";
	}
}