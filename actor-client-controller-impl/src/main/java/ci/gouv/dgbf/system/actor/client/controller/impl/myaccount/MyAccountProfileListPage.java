package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.impl.privilege.ProfileListPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ProfileFilterController;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class MyAccountProfileListPage extends AbstractEntityListPageContainerManagedImpl<Profile> implements MyAccountTheme,Serializable{
	
	private ProfileFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = instantiateFilterController();
	}
	
	public static ProfileFilterController instantiateFilterController() {
		ProfileFilterController filterController = ProfileFilterController.instantiate(__inject__(ActorController.class).getLoggedIn(), null);
		filterController.setIsUsedForLoggedUser(Boolean.TRUE).setRenderType(AbstractFilterController.RenderType.NONE);
		filterController.ignore(ProfileFilterController.FIELD_ACTOR_SELECT_ONE);
		return filterController;
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue(ci.gouv.dgbf.system.actor.server.persistence.entities.Profile.LABEL,Boolean.TRUE);
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		MapHelper.writeByKeyDoNotOverride(arguments, ProfileListPage.OUTCOME,OUTCOME);
		DataTable dataTable = ProfileListPage.buildDataTable(arguments);
		dataTable.setAreColumnsChoosable(Boolean.FALSE);
		return dataTable;
	}
		
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends ProfileListPage.DataTableListenerImpl implements Serializable {
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ProfileListPage.LazyDataModelListenerImpl implements Serializable {
		
	}

	/**/

	public static final String OUTCOME = "myAccountProfileListView";
}