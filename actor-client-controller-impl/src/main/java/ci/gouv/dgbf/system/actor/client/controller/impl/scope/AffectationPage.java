package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.ExecutionImputationListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationPage extends AbstractPageContainerManagedImpl implements Serializable {

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
		tabMenu = TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_ITEMS_OUTCOME,"affectationView",TabMenu.FIELD_ACTIVE_INDEX,TabMenu.Tab.getIndexOf(TABS, selectedTab)
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
		String functionIdentifier = WebController.getInstance().getRequestParameter(ParameterName.stringify(Function.class));
		Collection<Function> functions = EntityReader.getInstance().readMany(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_ASSOCIATED_TO_SCOPE_TYPE_FOR_UI);
		if(StringHelper.isBlank(functionIdentifier) && CollectionHelper.isNotEmpty(functions))
			functionIdentifier = CollectionHelper.getFirst(functions).getIdentifier();
		DataTable dataTable = ScopeFunctionListPage.buildDataTable(ScopeFunctionListPage.class,Boolean.TRUE,FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER)
				,functionIdentifier);
		dataTable.setTitle(OutputText.buildFromValue("Liste des postes"));
		SelectOneCombo functionSelectOneCombo = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_CHOICES,functions);
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
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOneCombo,Cell.FIELD_WIDTH,11));	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildTabExecutionImputation(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = ExecutionImputationListPage.buildDataTable(ExecutionImputationListPage.class,Boolean.TRUE);
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
}