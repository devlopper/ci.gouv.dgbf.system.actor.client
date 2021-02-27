package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.impl.function.FunctionListPage;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorCreatePrivilegesByFunctionsPage extends AbstractActorCreatePrivilegesByPage implements Serializable {

	@Override
	protected DataTable buildDataTable() {
		dataTable = FunctionListPage.buildDataTable(MapHelper.instantiate(DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-header-visibility-hidden"));
		return dataTable;
	}

	@Override
	protected String getByName() {
		return "fonction";
	}

	@Override
	protected void __create__() {
		Arguments<Function> arguments = new Arguments<Function>();
		arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ProfilePrivilegeBusiness.CREATE_FROM_FUNCTIONS));		
		((Function)dataTable.getSelectionAsCollection().iterator().next()).setProfileIdentifier(profile.getIdentifier());
		arguments.setCreatables(CollectionHelper.cast(Function.class, dataTable.getSelectionAsCollection()));
		EntitySaver.getInstance().save(Function.class, arguments);
	}
}