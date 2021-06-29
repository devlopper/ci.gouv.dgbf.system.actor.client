package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.computation.SortOrder;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionAuditListPage extends AbstractEntityListPageContainerManagedImpl<ScopeFunction> implements Serializable {

	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)));
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Historique des créations , modifications et suppressions des postes";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, ScopeFunction.class);
		String functionIdentifier = (String) MapHelper.readByKey(arguments, FieldHelper.join(ScopeFunction.FIELD_FUNCTION,Function.FIELD_IDENTIFIER));
		Function function = (Function) MapHelper.readByKey(arguments, Function.class);
		if(function != null) {
			functionIdentifier = function.getIdentifier();
		}
		Collection<String> columnsNames = new ArrayList<>();
		columnsNames.addAll(List.of(ScopeFunction.FIELD_CODE,ScopeFunction.FIELD_NAME,ScopeFunction.FIELD___AUDIT_FUNCTIONALITY__,ScopeFunction.FIELD___AUDIT_WHO__
				,ScopeFunction.FIELD___AUDIT_WHEN_AS_STRING__));
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl().setFunctionIdentifier(functionIdentifier)
				.setIsAuditRecord(Boolean.TRUE));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
				.setFunctionIdentifier(functionIdentifier));
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends ScopeFunctionListPage.DataTableListenerImpl implements Serializable {

	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ScopeFunctionListPage.LazyDataModelListenerImpl implements Serializable {
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ScopeFunction> lazyDataModel) {
			return ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_AUDIT;
		}
		
		@Override
		public Arguments<ScopeFunction> instantiateArguments(LazyDataModel<ScopeFunction> lazyDataModel) {
			Arguments<ScopeFunction> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setIsProcessableAsAuditByDates(Boolean.TRUE).addProjectionsFromStrings(
					ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunctionAudit.FIELD_IDENTIFIER
					,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.FIELD___AUDIT_FUNCTIONALITY__
					,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.FIELD___AUDIT_WHAT__
					,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunction.FIELD___AUDIT_WHO__
					,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunctionAudit.FIELD_CODE
					,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeFunctionAudit.FIELD_NAME);
			LinkedHashMap<String, SortOrder> sortOrders = new LinkedHashMap<>();
			sortOrders.put(ScopeFunction.FIELD___AUDIT_WHEN__,SortOrder.DESCENDING);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setSortOrders(sortOrders);
			return arguments;
		}
	}
	
	public static final String OUTCOME = "scopeFunctionAuditListView";
}