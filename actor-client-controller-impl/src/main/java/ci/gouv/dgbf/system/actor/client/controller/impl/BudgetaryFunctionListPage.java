package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryIdentifierGetter;
import org.cyk.utility.__kernel__.persistence.query.QueryName;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetaryFunction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class BudgetaryFunctionListPage extends AbstractEntityListPageContainerManagedImpl<BudgetaryFunction> implements Serializable {

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
		return "Liste des fonctions budgétaires";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, BudgetaryFunction.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(BudgetaryFunction.FIELD_CODE
				,BudgetaryFunction.FIELD_NAME,BudgetaryFunction.FIELD_NUMBER_OF_OCCURENCE));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		return dataTable;
	}
	
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends DataTable.Listener.AbstractImpl implements Serializable {
		
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
			if(BudgetaryFunction.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.FIELD_WIDTH, "200");
			}else if(BudgetaryFunction.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
			}else if(BudgetaryFunction.FIELD_NUMBER_OF_OCCURENCE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Partagée");
				map.put(Column.FIELD_WIDTH, "200");
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<BudgetaryFunction> implements Serializable {				
		
		@Override
		public Boolean getReaderUsable(LazyDataModel<BudgetaryFunction> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<BudgetaryFunction> lazyDataModel) {
			return QueryIdentifierGetter.getInstance().get(BudgetaryFunction.class, QueryName.READ_WHERE_CODE_OR_NAME_LIKE);
		}
	}
}