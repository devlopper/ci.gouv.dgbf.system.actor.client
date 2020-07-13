package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorScopeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScope;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorCreateScopesPage extends AbstractActorCreateScopesOrPrivilegesPage<Scope> implements Serializable {

	private ScopeType scopeType;
	
	@Override
	protected void __listenPostConstruct__() {
		scopeType = WebController.getInstance().getRequestParameterEntityAsParent(ScopeType.class);
		super.__listenPostConstruct__();
	}
	
	@Override
	protected DataTable instantiateDataTable() {
		return ScopeListPage.buildDataTable(ScopeType.class,scopeType,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setActor(actor).setScopeType(scopeType));
	}
	
	@Override
	protected void create(Collection<Scope> scopes) {
		__inject__(ActorScopeController.class).createMany(scopes.stream().map(scope -> new ActorScope().setActor(actor).setScope(scope)).collect(Collectors.toList()));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout de domaines de type <<"+scopeType.getName()+">> au compte utilisateur de "+actor.getCode()+" - "+actor.getNames();
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ScopeListPage.LazyDataModelListenerImpl implements Serializable {	
		private Actor actor;
		@Override
		public String getReadQueryIdentifier(LazyDataModel<Scope> lazyDataModel) {
			if(ScopeType.isCodeEqualsSECTION(scopeType))
				return ScopeQuerier.QUERY_IDENTIFIER_READ_INVISIBLE_SECTIONS_WHERE_FILTER;
			if(ScopeType.isCodeEqualsUA(scopeType))
				return ScopeQuerier.QUERY_IDENTIFIER_READ_INVISIBLE_ADMINISTRATIVE_UNITS_WITH_SECTIONS_WHERE_FILTER;
			return ScopeQuerier.QUERY_IDENTIFIER_READ_WHERE_FILTER_NOT_ASSOCIATED;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<Scope> lazyDataModel) {
			Filter.Dto filter = new Filter.Dto();
			filter.addField(ScopeQuerier.PARAMETER_NAME_ACTOR_CODE, actor.getCode());
			if(ScopeType.isCodeEqualsSECTION(scopeType))
				;
			else if(ScopeType.isCodeEqualsUA(scopeType))
				;
			else
				filter.addField(ScopeQuerier.PARAMETER_NAME_TYPE_CODE, scopeType.getCode());
			return filter;
		}
	}
}