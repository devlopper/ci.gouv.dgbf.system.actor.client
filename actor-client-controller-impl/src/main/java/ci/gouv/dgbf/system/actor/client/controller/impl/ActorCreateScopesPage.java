package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorScopeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorCreateScopesPage extends AbstractActorCreateScopesOrPrivielesPage<Scope> implements Serializable {

	private ScopeType scopeType;
	
	@Override
	protected DataTable instantiateDataTable() {
		scopeType = WebController.getInstance().getRequestParameterEntityAsParent(ScopeType.class);
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Scope.class,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,List.of(Scope.FIELD_CODE,Scope.FIELD_NAME),DataTable.FIELD_LISTENER,new DataTable.Listener.AbstractImpl() {
			@Override
			public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
				Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
				map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
				if(Scope.FIELD_CODE.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Code");
					map.put(Column.FIELD_WIDTH, "150");
				}else if(Scope.FIELD_NAME.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				}
				return map;
			}
		});
		
		@SuppressWarnings("unchecked")
		LazyDataModel<Scope> lazyDataModel = (LazyDataModel<Scope>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		lazyDataModel.setReadQueryIdentifier(ScopeQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_NOT_ASSOCIATED_BY_TYPES_CODES);
		lazyDataModel.setListener(new LazyDataModel.Listener.AbstractImpl<Scope>() {
			@Override
			public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
				return new Filter.Dto().addField(ScopeQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()))
						.addField(ScopeQuerier.PARAMETER_NAME_TYPES_CODES, List.of(scopeType == null ? "SECTION" : scopeType.getCode()));
			}
		});
		return dataTable;
	}
	
	@Override
	protected void create(Collection<Scope> scopes) {
		__inject__(ActorScopeController.class).createMany(scopes.stream().map(scope -> new ActorScope().setActor(actor).setScope(scope))
				.collect(Collectors.toList()));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout de domaines de type "+scopeType.getName()+" au compte utilisateur de "+actor.getCode()+" - "+actor.getNames();
	}	
}