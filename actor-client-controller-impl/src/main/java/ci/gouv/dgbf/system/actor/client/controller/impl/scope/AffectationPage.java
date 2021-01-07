package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private TabMenu tabMenu;
	private String functionIdentifier;
	
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
		
		if(selectedTab != null && selectedTab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildFunctionsTabMenu(cellsMaps);
	}
	
	private void buildFunctionsTabMenu(Collection<Map<Object,Object>> cellsMaps) {		
		Collection<Function> functions = EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATED_TO_SCOPE_TYPE_FOR_UI);
		Collections.sort((List<Function>)functions, new Comparator<Function>() {
			@Override
			public int compare(Function f1, Function f2) {
				return FUNCTIONS_ORDERS_INDEXES.get(f1.getCode()).compareTo(FUNCTIONS_ORDERS_INDEXES.get(f2.getCode()));
			}
		});
		if(CollectionHelper.isEmpty(functions))
			return;
		functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		if(StringHelper.isBlank(functionIdentifier))
			functionIdentifier = CollectionHelper.getFirst(functions).getIdentifier();		
		Collection<MenuItem> tabMenuItems = new ArrayList<>();
		Integer tabActiveIndex = null,index = 0;
		for(Function function : functions) {
			tabMenuItems.add(new MenuItem().setValue(function.getName())
				.addParameter(TabMenu.Tab.PARAMETER_NAME, TabMenu.Tab.getByParameterValue(TABS, TAB_SCOPE_FUNCTION).getParameterValue())
				.addParameter(ParameterName.stringify(Function.class), function.getIdentifier())
			);
			if(function.getIdentifier().equals(functionIdentifier))
				tabActiveIndex = index;
			else
				index++;
		}
		
		TabMenu tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,OUTCOME,TabMenu.FIELD_ACTIVE_INDEX,tabActiveIndex
				,TabMenu.ConfiguratorImpl.FIELD_ITEMS,tabMenuItems);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,tabMenu,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTab(Collection<Map<Object,Object>> cellsMaps,TabMenu.Tab tab) {
		if(tab.getParameterValue().equals(TAB_SCOPE_FUNCTION))
			buildTabScopeFunction(cellsMaps);
		else if(tab.getParameterValue().equals(TAB_EXECUTION_IMPUTATION))
			buildTabExecutionImputation(cellsMaps);
	}
	
	private void buildTabScopeFunction(Collection<Map<Object,Object>> cellsMaps) {
		/*String functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		Collection<Function> functions = EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATED_TO_SCOPE_TYPE_FOR_UI);
		if(StringHelper.isBlank(functionIdentifier) && CollectionHelper.isNotEmpty(functions))
			functionIdentifier = CollectionHelper.getFirst(functions).getIdentifier();
		*/
		DataTable dataTable = ScopeFunctionListPage.buildDataTable(ScopeFunctionListPage.class,Boolean.TRUE
				,FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER),functionIdentifier);
		//dataTable.setTitle(OutputText.buildFromValue("Liste des postes"));
		/*SelectOneCombo functionSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_CHOICES,functions);
		functionSelectOneCombo.selectBySystemIdentifier(functionIdentifier);
		functionSelectOneCombo.enableValueChangeListener(new AbstractInputChoiceOne.ValueChangeListener() {
			@SuppressWarnings("unchecked")
			@Override
			protected void select(AbstractAction action, Object value) {
				((ScopeFunctionListPage.LazyDataModelListenerImpl)((LazyDataModel<ScopeFunction>)dataTable.getValue()).getListener())
					.setFunctionIdentifier((String)FieldHelper.readSystemIdentifier(value));
			}
		}, List.of(dataTable));		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Fonction"),Cell.FIELD_WIDTH,1));	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOneCombo,Cell.FIELD_WIDTH,11));*/
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabExecutionImputation(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = AssignmentsListPage.buildDataTable(AssignmentsListPage.class,Boolean.TRUE);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Affectation";
	}
	
	/**/
	
	private static final String TAB_SCOPE_FUNCTION = "poste";
	private static final String TAB_EXECUTION_IMPUTATION = "lignebudgetaire";
	
	private static final List<TabMenu.Tab> TABS = List.of(
		new TabMenu.Tab("Poste",TAB_SCOPE_FUNCTION)
		,new TabMenu.Tab("Ligne budgétaire",TAB_EXECUTION_IMPUTATION)
	);
	
	public static final String OUTCOME = "affectationView";
	public static Map<String,Integer> FUNCTIONS_ORDERS_INDEXES = Map.of(
		ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER,0
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT,1
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER,2
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT,3
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER,4
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT,5
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER,6
		,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT,7
	);
}