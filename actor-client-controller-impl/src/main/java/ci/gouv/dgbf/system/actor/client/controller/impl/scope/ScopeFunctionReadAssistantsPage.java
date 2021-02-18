package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionReadAssistantsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private ScopeFunction scopeFunction;
	private DataTable dataTable;

	@Override
	protected void __listenPostConstruct__() {
		scopeFunction = WebController.getInstance().getUsingRequestParameterAsSystemIdentifierByQueryIdentifier(ScopeFunction.class
				, ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI);
		super.__listenPostConstruct__();
		if(scopeFunction != null)
			dataTable = ScopeFunctionListPage.buildDataTable(
					DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,List.of(ScopeFunction.FIELD_CODE,ScopeFunction.FIELD_NAME)
					//,ScopeFunctionListPage.RenderType.class,ScopeFunctionListPage.RenderType.ASSISTANTS
					,ScopeFunction.class,scopeFunction,DataTable.FIELD_LISTENER,new DataTableListenerImpl()
					,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setHolder(scopeFunction));
		if(dataTable != null) {
			dataTable.setAreColumnsChoosable(Boolean.FALSE);
		}
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assistant(s) de "+scopeFunction.toString();
	}

	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends ScopeFunctionListPage.DataTableListenerImpl implements Serializable {
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(ScopeFunction.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Code");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.FALSE);
			}else if(ScopeFunction.FIELD_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.FALSE);
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ScopeFunctionListPage.LazyDataModelListenerImpl implements Serializable {				
		private ScopeFunction holder;
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<ScopeFunction> lazyDataModel) {
			return ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_PARENTS_IDENTIFIERS_FOR_UI;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ScopeFunction> lazyDataModel) {
			return new Filter.Dto().addField(ScopeFunctionQuerier.PARAMETER_NAME_PARENTS_IDENTIFIERS, List.of(holder.getIdentifier()));
		}
	}
	
	public static final String OUTCOME = "scopeFunctionReadAssistantsView";
}