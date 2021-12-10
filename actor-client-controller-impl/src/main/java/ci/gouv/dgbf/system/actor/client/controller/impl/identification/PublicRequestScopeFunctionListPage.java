package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractFilterController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.page.AbstractEntityListPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestScopeFunction;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestScopeFunctionFilterController;
import ci.gouv.dgbf.system.actor.client.controller.impl.actor.RequestScopeFunctionListPage;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class PublicRequestScopeFunctionListPage extends AbstractEntityListPageContainerManagedImpl<RequestScopeFunction> implements IdentificationTheme,Serializable{

	private RequestScopeFunctionFilterController filterController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = instantiateFilterController();
	}
	
	public static RequestScopeFunctionFilterController instantiateFilterController() {
		String electronicMailAddress = WebController.getInstance().getRequestParameter(QUERY_PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS);
		if(StringHelper.isBlank(electronicMailAddress)) {
			Redirector.Arguments arguments = new Redirector.Arguments();
			arguments.outcome(PublicRequestScopeFunctionListInputElectronicMailAddressPage.OUTCOME);
			Redirector.getInstance().redirect(arguments);		
			return null;
		}
		RequestScopeFunctionFilterController filterController = RequestScopeFunctionFilterController.instantiate();
		filterController.setIsUsedForLoggedUser(Boolean.FALSE);
		filterController.setElectronicMailAddressInitial(electronicMailAddress/*"kycdev@gmail.com"*/);//this one must read from secret value only known by email owner
		filterController.setGrantedInitial(Boolean.TRUE);
		filterController.setFunctionsCodes(FUNCTIONS_CODES);
		filterController.setRenderType(AbstractFilterController.RenderType.NONE);
		return filterController;
	}
	
	@Override
	protected DataTable __buildDataTable__() {
		DataTable dataTable = buildDataTable(RequestScopeFunctionFilterController.class,filterController,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new LazyDataModelListenerImpl().setFilterController(filterController));
		return dataTable;
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des postes associés à "+filterController.getElectronicMailAddress(); //RequestScopeFunctionListPage.buildWindowTitleValue(filterController);
	}
	
	public static DataTable buildDataTable(Map<Object,Object> arguments) {
		if(arguments == null)
			arguments = new HashMap<>();
		DataTable dataTable = RequestScopeFunctionListPage.buildDataTable(arguments);
		dataTable.setAreColumnsChoosable(Boolean.FALSE);
		return dataTable;
	}

	public static DataTable buildDataTable(Object...objects) {
		return buildDataTable(ArrayHelper.isEmpty(objects) ? null : MapHelper.instantiate(objects));
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends RequestScopeFunctionListPage.DataTableListenerImpl implements Serializable {
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends RequestScopeFunctionListPage.LazyDataModelListenerImpl implements Serializable {
		
	}

	/**/
	
	public static final Collection<String> FUNCTIONS_CODES = List.of(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
			,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER);
	public static final String QUERY_PARAMETER_NAME_ELECTRONIC_MAIL_ADDRESS = "email";
	public static final String OUTCOME = "publicRequestScopeFunctionListView";
}