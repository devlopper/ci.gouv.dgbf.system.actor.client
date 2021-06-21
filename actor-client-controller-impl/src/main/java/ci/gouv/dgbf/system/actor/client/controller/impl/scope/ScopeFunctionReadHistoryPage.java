package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionReadHistoryPage extends AbstractPageContainerManagedImpl implements Serializable {

	private ScopeFunction scopeFunction;
	private DataTable historyDataTable;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		Arguments<ScopeFunction> arguments = new Arguments<ScopeFunction>().queryIdentifier(ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE)
				.filterByIdentifier(WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER))
				.transientFieldsNames(ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.FIELD___AUDIT_RECORDS__);
			arguments.transientFieldsNames();
			scopeFunction = EntityReader.getInstance().readOne(ScopeFunction.class, arguments);

		super.__listenPostConstruct__();
		if(scopeFunction != null) {
			Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
			
			Layout auditsRecordsLayout = buildAuditsRecordsLayout();
			if(auditsRecordsLayout != null)
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,auditsRecordsLayout,Cell.FIELD_WIDTH,12));
					
			layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);	
		}		
	}
	
	private Layout buildAuditsRecordsLayout() {
		if(historyDataTable != null)
			return null;
		historyDataTable = ScopeFunctionListPage.buildHistoryDataTable(ScopeFunction.class,scopeFunction);
		if(historyDataTable == null)
			return null;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,historyDataTable,Cell.FIELD_WIDTH,12));
		return Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(scopeFunction == null)
			return super.__getWindowTitleValue__();
		return "Historique - "+scopeFunction.toString();
	}
	
	public static final String OUTCOME = "scopeFunctionReadHistoryView";
}