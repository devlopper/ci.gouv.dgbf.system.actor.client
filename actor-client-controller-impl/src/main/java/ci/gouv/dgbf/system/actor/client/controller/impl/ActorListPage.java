package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.MenuItem;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.IdentityQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorListPage extends AbstractEntityListPageContainerManagedImpl<Actor> implements Serializable {

	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable();
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des comptes utilisateurs";
	}
	
	/**/
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LAZY, Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_ELEMENT_CLASS, Actor.class);
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES, CollectionHelper.listOf(Actor.FIELD_CODE,Actor.FIELD_FIRST_NAME
				,Actor.FIELD_LAST_NAMES,Actor.FIELD_FUNCTIONS
				//,Actor.FIELD_VISIBLE_SECTIONS,Actor.FIELD_VISIBLE_MODULES
				));
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-footer-visibility-hidden");
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.FIELD_LISTENER,new DataTableListenerImpl());
		MapHelper.writeByKeyDoNotOverride(arguments, DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl());
		DataTable dataTable = DataTable.build(arguments);
		dataTable.addHeaderToolbarLeftCommandsByArgumentsOpenViewInDialogCreate();
		dataTable.addRecordMenuItemByArgumentsNavigateToView(null,"actorListPrivilegesStaticView", MenuItem.FIELD_VALUE,"Assignation", MenuItem.FIELD_ICON,"fa fa-lock"
				,MenuItem.FIELD_PARAMETERS,Map.of(ParameterName.IS_STATIC.getValue(),"true"));
		dataTable.addRecordMenuItemByArgumentsNavigateToView(null,"actorListScopesStaticView", MenuItem.FIELD_VALUE,"Affectation", MenuItem.FIELD_ICON,"fa fa-eye"
				,MenuItem.FIELD_PARAMETERS,Map.of(ParameterName.IS_STATIC.getValue(),"true"));
		dataTable.addRecordMenuItemByArgumentsOpenViewInDialog("actorEditFunctionsView", MenuItem.FIELD_VALUE,"Fonctions", MenuItem.FIELD_ICON,"fa fa-flash");
		dataTable.addRecordMenuItemByArgumentsExecuteFunctionDelete();
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
			if(Actor.FIELD_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom et prénom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_NAMES);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_FIRST_NAME.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_FIRST_NAME);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_LAST_NAMES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Prenom(s)");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_LAST_NAMES);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_ELECTRONIC_MAIL_ADDRESS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Email");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_CODE.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Nom d'utilisateur");
				map.put(Column.ConfiguratorImpl.FIELD_FILTERABLE, Boolean.TRUE);
				map.put(Column.FIELD_FILTER_BY, IdentityQuerier.PARAMETER_NAME_CODE);
				map.put(Column.FIELD_WIDTH, "200");
			}else if(Actor.FIELD_FUNCTIONS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Fonction(s)");
			}else if(Actor.FIELD_VISIBLE_SECTIONS.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Section(s) visible(s)");
			}else if(Actor.FIELD_VISIBLE_MODULES.equals(fieldName)) {
				map.put(Column.FIELD_HEADER_TEXT, "Module(s) visible(s)");
			}
			return map;
		}
		
		@Override
		public Object getCellValueByRecordByColumn(Object record, Integer recordIndex, Column column,Integer columnIndex) {
			if(column != null && Actor.FIELD_FUNCTIONS.equals(column.getFieldName()))
				return CollectionHelper.isEmpty(((Actor)record).getFunctions()) ? null : ((Actor)record).getFunctions().stream().map(x -> x.getName()).collect(Collectors.joining(","));
			return super.getCellValueByRecordByColumn(record, recordIndex, column, columnIndex);
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends LazyDataModel.Listener.AbstractImpl<Actor> implements Serializable {
		@Override
		public Boolean getReaderUsable(LazyDataModel<Actor> lazyDataModel) {
			return Boolean.TRUE;
		}
		
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Actor> lazyDataModel) {
			//return ActorQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER;
			return ActorQuerier.QUERY_IDENTIFIER_READ_WITH_FUNCTIONS_WHERE_FILTER;
			//return ActorQuerier.QUERY_IDENTIFIER_READ_WITH_ALL_WHERE_FILTER;
		}
	}
}