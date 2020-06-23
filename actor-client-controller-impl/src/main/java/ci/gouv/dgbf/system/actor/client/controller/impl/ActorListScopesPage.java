package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorListScopesPage extends AbstractActorListPrivilegesOrScopesPage<ActorScope> implements Serializable {

	private ScopeType scopeType;
	
	@Override
	protected DataTable instantiateDataTable() {
		return ActorScopeListPage.instantiateDataTable(List.of(ActorScope.FIELD_SCOPE,ActorScope.FIELD_VISIBLE),new DataTableListenerImpl(),new LazyDataModelListenerImpl());
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Affectation des domaines";
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListScopesView";
	}
	
	@Override
	protected String getCreateOutcome() {
		return "actorCreateScopesView";
	}
	
	/**/
	
	public class DataTableListenerImpl extends ActorScopeListPage.DataTableListenerImpl implements Serializable{
		
		public DataTableListenerImpl() {
			
		}
	}
	
	public class LazyDataModelListenerImpl extends ActorScopeListPage.LazyDataModelListenerImpl implements Serializable {
		
		@Override
		public Arguments<ActorScope> instantiateArguments(LazyDataModel<ActorScope> lazyDataModel) {
			Arguments<ActorScope> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ActorScopeQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_BY_SCOPE_TYPES_CODES);
			return arguments;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ActorScope> lazyDataModel) {
			return new Filter.Dto().addField(ActorScopeQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()))
					.addField(ActorScopeQuerier.PARAMETER_NAME_SCOPE_TYPES_CODES, List.of("SECTION"/*scopeType.getCode()*/));
		}
	}
}