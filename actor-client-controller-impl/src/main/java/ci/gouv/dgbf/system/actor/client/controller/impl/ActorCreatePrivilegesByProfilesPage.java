package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;

import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorCreatePrivilegesByProfilesPage extends AbstractActorCreatePrivilegesByPage implements Serializable {

	@Override
	protected DataTable buildDataTable() {
		dataTable = ProfileListPage.buildDataTable(MapHelper.instantiate(DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-header-visibility-hidden"));
		return dataTable;
	}

	@Override
	protected String getByName() {
		return "profile";
	}

	@Override
	protected void __create__() {
		Arguments<Profile> arguments = new Arguments<Profile>();
		arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ProfilePrivilegeBusiness.CREATE_FROM_PROFILES));		
		((Profile)dataTable.getSelection().iterator().next()).setProfileIdentifier(profile.getIdentifier());
		arguments.setCreatables(CollectionHelper.cast(Profile.class, dataTable.getSelection()));
		EntitySaver.getInstance().save(Profile.class, arguments);
	}
}