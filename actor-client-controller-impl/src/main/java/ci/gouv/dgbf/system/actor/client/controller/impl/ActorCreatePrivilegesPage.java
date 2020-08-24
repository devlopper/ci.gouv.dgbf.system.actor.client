package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;

import ci.gouv.dgbf.system.actor.client.controller.api.ProfilePrivilegeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorCreatePrivilegesPage extends AbstractActorCreateScopesOrPrivilegesPage<Privilege> implements Serializable {

	private Profile profile;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		profile = Helper.getProfileFromRequestParameterEntityAsParent(actor); 
	}
	
	@Override
	protected DataTable instantiateDataTable() {
		DataTable dataTable = DataTable.build(DataTable.FIELD_LAZY,Boolean.TRUE,DataTable.FIELD_ELEMENT_CLASS,Privilege.class,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,List.of(Privilege.FIELD_CODE,Privilege.FIELD_NAME),DataTable.FIELD_LISTENER,new DataTable.Listener.AbstractImpl() {
			@Override
			public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
				Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
				map.put(Column.ConfiguratorImpl.FIELD_EDITABLE, Boolean.FALSE);
				if(Privilege.FIELD_CODE.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Code");
					map.put(Column.FIELD_WIDTH, "150");
				}else if(Privilege.FIELD_NAME.equals(fieldName)) {
					map.put(Column.FIELD_HEADER_TEXT, "Libellé");
				}
				return map;
			}
		});
		
		@SuppressWarnings("unchecked")
		LazyDataModel<Privilege> lazyDataModel = (LazyDataModel<Privilege>) dataTable.getValue();
		lazyDataModel.setReaderUsable(Boolean.TRUE);
		lazyDataModel.setReadQueryIdentifier(PrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES_NOT_ASSOCIATED);
		lazyDataModel.setListener(new LazyDataModel.Listener.AbstractImpl<Privilege>() {
			@Override
			public Filter.Dto instantiateFilter(LazyDataModel<Privilege> lazyDataModel) {
				return new Filter.Dto().addField(PrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode()));
			}
		});
		return dataTable;
	}
	
	@Override
	protected void create(Collection<Privilege> privileges) {
		Collection<ProfilePrivilege> profilePrivileges = privileges.stream().map(privilege -> new ProfilePrivilege().setProfile(profile).setPrivilege(privilege))
				.collect(Collectors.toList());
		__inject__(ProfilePrivilegeController.class).createMany(profilePrivileges);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Ajout de privilèges au profile du compte utilisateur de "+actor.getCode()+" - "+actor.getNames();
	}	
}