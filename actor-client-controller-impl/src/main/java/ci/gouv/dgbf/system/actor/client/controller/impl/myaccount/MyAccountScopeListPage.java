package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Scope;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.scope.ScopeListPage;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeTypeQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class MyAccountScopeListPage extends AbstractEntityListPageContainerManagedImpl<Scope> implements MyAccountTheme,Serializable{
	
	private ScopeFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ScopeFilterController();
		filterController.setActorInitial(__inject__(ActorController.class).getLoggedIn());
		filterController.setScopeTypeInitial(EntityReader.getInstance().readOne(ScopeType.class, new Arguments<ScopeType>()
				.queryIdentifier(ScopeTypeQuerier.QUERY_IDENTIFIER_READ_DYNAMIC_ONE).filterFieldsValues(ScopeTypeQuerier.PARAMETER_NAME_CODE
						,ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION)));
		if( !((HttpServletRequest)__getRequest__()).getParameterMap().containsKey(Scope.FIELD_VISIBLE) )
			filterController.setVisibleInitial(Boolean.TRUE);
		filterController.ignore(ScopeFilterController.FIELD_ACTOR_SELECT_ONE);
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(ScopeFilterController.class,filterController,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFilterController(filterController),ScopeListPage.class,MyAccountScopeListPage.class
				,ScopeListPage.OUTCOME,OUTCOME);
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(filterController == null)
			return super.__getWindowTitleValue__();
		return filterController.generateWindowTitleValue("Visibilités");
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		DataTable dataTable = ScopeListPage.buildDataTable(arguments);
		return dataTable;
	}
		
	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends ScopeListPage.DataTableListenerImpl implements Serializable {
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ScopeListPage.LazyDataModelListenerImpl implements Serializable {
		
	}

	/**/

	public static final String OUTCOME = "myAccountScopeListView";
}