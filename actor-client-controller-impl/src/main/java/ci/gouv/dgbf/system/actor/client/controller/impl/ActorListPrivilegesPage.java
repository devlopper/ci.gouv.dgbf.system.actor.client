package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorListPrivilegesPage extends AbstractActorListPrivilegesOrScopesPage<ProfilePrivilege> implements Serializable {

	private Profile profile;
	
	@Override
	protected void __listenAfterPostConstruct__() {
		super.__listenAfterPostConstruct__();
		profile = Helper.getProfileFromRequestParameterEntityAsParent(actor); 
	}
	
	@Override
	protected DataTable instantiateDataTable() {
		return ProfilePrivilegeListPage.instantiateDataTable(List.of(ProfilePrivilege.FIELD_PRIVILEGE,ProfilePrivilege.FIELD_VISIBLE),new DataTableListenerImpl(),new LazyDataModelListenerImpl());
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation des privilèges";
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListPrivilegesView";
	}
	
	@Override
	protected String getCreateOutcome() {
		return "actorCreatePrivilegesView";
	}
	
	/**/
	
	public class DataTableListenerImpl extends ProfilePrivilegeListPage.DataTableListenerImpl implements Serializable{
		
		public DataTableListenerImpl() {
			
		}
	}
	
	public class LazyDataModelListenerImpl extends ProfilePrivilegeListPage.LazyDataModelListenerImpl implements Serializable {
		
		@Override
		public Arguments<ProfilePrivilege> instantiateArguments(LazyDataModel<ProfilePrivilege> lazyDataModel) {
			Arguments<ProfilePrivilege> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES);
			return arguments;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ProfilePrivilege> lazyDataModel) {
			return new Filter.Dto().addField(ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode()));
		}
	}
}