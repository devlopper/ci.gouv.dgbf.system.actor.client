package ci.gouv.dgbf.system.actor.client.controller.impl.function;

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
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;

import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class AssignmentsAuditListPage extends AbstractEntityListPageContainerManagedImpl<Assignments> implements Serializable {

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
		return "Historique des modifications des affectations";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_RENDER_TYPE,org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType.OUTPUT_UNSELECTABLE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Assignments.class);
		Collection<String> columnsNames = new ArrayList<>();
		columnsNames.addAll(List.of(
				Assignments.FIELD_SECTION_AS_STRING,Assignments.FIELD_ADMINISTRATIVE_UNIT_AS_STRING,Assignments.FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING
				,Assignments.FIELD_ACTION_AS_STRING,Assignments.FIELD_EXPENDITURE_NATURE_AS_STRING,Assignments.FIELD_ACTIVITY_AS_STRING
				,Assignments.FIELD_ECONOMIC_NATURE_AS_STRING
				,Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
				,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING,Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING
				,Assignments.FIELD___AUDIT_FUNCTIONALITY__,Assignments.FIELD___AUDIT_WHO__,Assignments.FIELD___AUDIT_WHEN_AS_STRING__));
		
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, columnsNames);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl().setIsAuditRecord(Boolean.TRUE));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.setAreColumnsChoosable(Boolean.TRUE);
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends AssignmentsListPage.DataTableListenerImpl implements Serializable {

	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends AssignmentsListPage.LazyDataModelListenerImpl implements Serializable {
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Assignments> lazyDataModel) {
			return AssignmentsQuerier.QUERY_IDENTIFIER_READ_AUDIT;
		}
		
		@Override
		public Arguments<Assignments> instantiateArguments(LazyDataModel<Assignments> lazyDataModel) {
			Arguments<Assignments> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setIsProcessableAsAuditByDates(Boolean.TRUE).addProjectionsFromStrings(
					ci.gouv.dgbf.system.actor.server.persistence.entities.AssignmentsAudit.FIELD_IDENTIFIER
					,ci.gouv.dgbf.system.actor.server.persistence.entities.AssignmentsAudit.FIELD___AUDIT_FUNCTIONALITY__
					,ci.gouv.dgbf.system.actor.server.persistence.entities.AssignmentsAudit.FIELD___AUDIT_WHAT__
					,ci.gouv.dgbf.system.actor.server.persistence.entities.AssignmentsAudit.FIELD___AUDIT_WHO__
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELD_CREDIT_MANAGER_HOLDER_AS_STRING
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER_AS_STRING
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER_AS_STRING
					,ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELD_ACCOUNTING_HOLDER_AS_STRING);
			LinkedHashMap<String, SortOrder> sortOrders = new LinkedHashMap<>();
			sortOrders.put(Assignments.FIELD___AUDIT_WHEN__,SortOrder.DESCENDING);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setSortOrders(sortOrders);
			return arguments;
		}
	}
	
	public static final String OUTCOME = "assignmentsAuditListView";
}