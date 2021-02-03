package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.persistence.query.QueryIdentifierGetter;
import org.cyk.utility.__kernel__.persistence.query.QueryName;
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

import ci.gouv.dgbf.system.actor.client.controller.entities.Action;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActivityCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetSpecializationUnit;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import ci.gouv.dgbf.system.actor.client.controller.impl.account.AbstractActorListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.account.ActorListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AssignationPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private TabMenu tabMenu;
	
	@Override
	protected void __listenPostConstruct__() {
		contentOutputPanel.setDeferred(Boolean.TRUE);
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
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"assignationView",TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		if(tab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildTabScopeFunction(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_PROFILE))
			buildTabProfile(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_SCOPE))
			buildTabScope(cellsMaps);
	}
	
	private void buildTabProfile(Collection<Map<Object,Object>> cellsMaps) {
		String code = WebController.getInstance().getRequestParameter(AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl.FIELD_CODE);
		Collection<Profile> profiles = EntityReader.getInstance().readMany(Profile.class, ProfileQuerier.QUERY_IDENTIFIER_READ);
		if(StringHelper.isBlank(code) && CollectionHelper.isNotEmpty(profiles))			
			code = ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.CODE_ADMINISTRATEUR;		
		DataTable dataTable = ActorListPage.buildDataTable(Profile.class,code,AbstractActorListPage.RenderType.class,AbstractActorListPage.RenderType.PROFILES);		
		SelectOneCombo profileSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICES,profiles);
		profileSelectOneCombo.selectByBusinessIdentifier(code);
		profileSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			protected void select(AbstractAction action, Object value) {
				((AbstractActorListPage.LazyDataModelListenerImpl)((LazyDataModel<Actor>)dataTable.getValue()).getListener())
					.setProfileCode((String)FieldHelper.readBusinessIdentifier(value));
			}
		}, List.of(dataTable));		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,profileSelectOneCombo,Cell.FIELD_WIDTH,12));		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabScope(Collection<Map<Object,Object>> cellsMaps) {
		String scopeTypeCode = WebController.getInstance().getRequestParameter(ParameterName.stringify(ScopeType.class));
		if(StringHelper.isBlank(scopeTypeCode))
			scopeTypeCode = ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION;
		Collection<ScopeType> scopeTypes = EntityReader.getInstance().readMany(ScopeType.class, ScopeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING);				
		if(CollectionHelper.isNotEmpty(scopeTypes)) {
			Collection<MenuItem> tabMenuItems = new ArrayList<>();
			Integer activeIndex = null;
			Integer count = 0;
			for(ScopeType scopeType : scopeTypes) {
				tabMenuItems.add(new MenuItem().setValue(scopeType.getName())
						.addParameter(TabMenu.Tab.PARAMETER_NAME, TAB_SCOPE)
						.addParameter(ParameterName.stringify(ScopeType.class), scopeType.getCode()));
				if(activeIndex == null && scopeType.getCode().equals(scopeTypeCode))
					activeIndex = count;
				count++;
			}
			TabMenu scopeTypesTabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"assignationView",TabMenu.FIELD_ACTIVE_INDEX,activeIndex
					,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,scopeTypesTabMenu,Cell.FIELD_WIDTH,12));
		}
		if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION.equals(scopeTypeCode))
			buildTabScopeAddVisibleScopesSelectOneComboAndDataTable(cellsMaps, Section.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION);
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_USB.equals(scopeTypeCode))
			buildTabScopeAddVisibleScopesSelectOneComboAndDataTable(cellsMaps, BudgetSpecializationUnit.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_USB);
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTION.equals(scopeTypeCode))
			buildTabScopeAddVisibleScopesSelectOneComboAndDataTable(cellsMaps, Action.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTION);
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTIVITE.equals(scopeTypeCode))
			;//addVisibleScopesSelectOneCombo(cellsMaps, Activity.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTIVITE);
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_CATEGORIE_ACTIVITE.equals(scopeTypeCode))
			buildTabScopeAddVisibleScopesSelectOneComboAndDataTable(cellsMaps, ActivityCategory.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_CATEGORIE_ACTIVITE);
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_IMPUTATION.equals(scopeTypeCode))
			;//addVisibleScopesSelectOneCombo(cellsMaps, Imputation.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_IMPUTATION);
		
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_UA.equals(scopeTypeCode))
			;//addVisibleScopesSelectOneCombo(cellsMaps, AdministrativeUnit.class, ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_UA);
		//DataTable dataTable = ExecutionImputationListPage.buildDataTable(ExecutionImputationListPage.class,Boolean.TRUE);
		//cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	public void buildTabScopeAddVisibleScopesSelectOneComboAndDataTable(Collection<Map<Object,Object>> cellsMaps,Class<?> scopeClass,String scopeTypeCode) {
		String queryIdentifier = QueryIdentifierGetter.getInstance().get(scopeClass, QueryName.READ);
		Collection<?> scopes = EntityReader.getInstance().readMany(scopeClass, queryIdentifier);
		if(CollectionHelper.isEmpty(scopes))
			return;
		String code = (String) FieldHelper.readBusinessIdentifier(CollectionHelper.getFirst(scopes));
		DataTable dataTable = ActorListPage.buildDataTable(scopeClass,code,AbstractActorListPage.RenderType.class,AbstractActorListPage.RenderType.SCOPES,ScopeType.class,scopeTypeCode);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildTabScopeBuildVisibleScopesSelectOneCombo(scopeTypeCode, scopes, code,dataTable),Cell.FIELD_WIDTH,12));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	public SelectOneCombo buildTabScopeBuildVisibleScopesSelectOneCombo(String scopeTypeCode,Collection<?> scopes,String code,DataTable dataTable) {
		if(StringHelper.isBlank(scopeTypeCode) || CollectionHelper.isEmpty(scopes))
			return null;
		SelectOneCombo selectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICES,scopes);
		selectOneCombo.selectByBusinessIdentifier(code);
		selectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			protected void select(AbstractAction action, Object value) {
				AbstractActorListPage.LazyDataModelListenerImpl lazyDataModel = ((AbstractActorListPage.LazyDataModelListenerImpl)((LazyDataModel<Actor>)dataTable.getValue())
						.getListener());
				String scopeCode = (String)FieldHelper.readBusinessIdentifier(value);
				if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION.equals(scopeTypeCode))
					lazyDataModel.setVisibleSectionCode(scopeCode);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_USB.equals(scopeTypeCode))
					lazyDataModel.setVisibleBudgetSpecializationUnitCode(scopeCode);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTION.equals(scopeTypeCode))
					lazyDataModel.setVisibleActionCode(scopeCode);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTIVITE.equals(scopeTypeCode))
					lazyDataModel.setVisibleActivityCode(scopeCode);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_CATEGORIE_ACTIVITE.equals(scopeTypeCode))
					lazyDataModel.setVisibleActivityCategoryCode(scopeCode);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_IMPUTATION.equals(scopeTypeCode))
					lazyDataModel.setVisibleImputationCode(scopeCode);
				else if(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_UA.equals(scopeTypeCode))
					lazyDataModel.setVisibleAdministrativeUnitCode(scopeCode);
			}
		}, List.of(dataTable));
		return selectOneCombo;
	}
	
	private void buildTabScopeFunction(Collection<Map<Object,Object>> cellsMaps) {
		//DataTable dataTable = ScopeFunctionListPage.buildDataTable(ScopeFunctionListPage.class,Boolean.TRUE);
		//cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation";
	}
	
	/**/
	
	private static final String TAB_PROFILE = "profile";
	private static final String TAB_SCOPE = "visibilite";
	private static final String TAB_SCOPE_FUNCTION = "poste";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Profile",TAB_PROFILE)
		,new TabMenu.Tab("Visibilité",TAB_SCOPE)
		,new TabMenu.Tab("Poste",TAB_SCOPE_FUNCTION)
	);
}