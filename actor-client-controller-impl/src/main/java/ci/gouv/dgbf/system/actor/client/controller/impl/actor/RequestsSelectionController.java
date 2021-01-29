package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.primefaces.PrimeFaces;

import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.Request;
import ci.gouv.dgbf.system.actor.client.controller.entities.RequestStatus;
import ci.gouv.dgbf.system.actor.client.controller.entities.Section;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class RequestsSelectionController implements Serializable {

	private String listIdentifier = RandomHelper.getAlphabetic(4);
	private DataTable requestsDataTable;
	private Collection<Request> selected;	
	private CommandButton addCommandButton;
	
	private String dialogWidgetVar = "requestSelectorDialog";
	private String dialogTitle;
	
	private Layout layout;
	private String layoutIdentifier = RandomHelper.getAlphabetic(4);
	
	public RequestsSelectionController(RequestStatus requestStatus) {
		buildDataTable(requestStatus);		
		buildAddCommandButton();	
		buildLayout();
	}
	
	public void remove(Request request) {
		if(CollectionHelper.isEmpty(selected))
			return;
		selected.remove(request);
	}
	
	private void buildDataTable(RequestStatus requestStatus) {
		requestsDataTable = RequestListPage.buildDataTable(RequestListPage.class,RequestsSelectionController.class
				,DataTable.FIELD_RENDER_TYPE,AbstractCollection.RenderType.SELECTION,DataTable.FIELD_SELECTION_MODE,"multiple"
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new RequestListPage.LazyDataModelListenerImpl()
				.setStatusIdentifier(requestStatus == null ? null : requestStatus.getIdentifier()));
		
	}
	
	private void buildAddCommandButton() {
		addCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Ajouter",CommandButton.FIELD_ICON,"fa fa-plus"
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_NULLABLE,Boolean.TRUE
				,CommandButton.FIELD_USER_INTERFACE_ACTION
				,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
				@Override
				protected Object __runExecuteFunction__(AbstractAction action) {
					if(requestsDataTable == null || CollectionHelper.isEmpty(requestsDataTable.getSelectionAsCollection()))
						return null;
					if(selected == null)
						selected = new LinkedHashSet<>();
					requestsDataTable.getSelectionAsCollection().forEach(x -> {
						selected.add((Request) x);
					});
					requestsDataTable.setSelection(null);
					requestsDataTable.setSelectionAsCollection(null);
					PrimeFaces.current().executeScript(String.format("PF('%s').hide();",dialogWidgetVar));
					return null;
				}
			});
		//addCommandButton.setProcess("@(."+requestsDataTable.getStyleClassAsIdentifier()+")");
		//addCommandButton.setProcess("@this");
		addCommandButton.addUpdates(listIdentifier);
	}
	
	private void buildLayout() {
		layout = Layout.build(Layout.FIELD_IDENTIFIER,layoutIdentifier,Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(
						MapHelper.instantiate(Cell.FIELD_CONTROL,requestsDataTable,Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,addCommandButton,Cell.FIELD_WIDTH,12)
					));
	}
	
	@SuppressWarnings("unchecked")
	public void showDialog(Section section,Function function) {
		if(section == null || function == null) {
			dialogTitle = "Sélection de demandes à porter sur le bordereau";
		}else {
			dialogTitle = String.format("Sélection de demandes de la section %s de %s à porter sur le bordereau",section.getCode(),function.getName());
			((RequestListPage.LazyDataModelListenerImpl) ((LazyDataModel<Request>)requestsDataTable.getValue()).getListener()).setFunctionIdentifier(function.getIdentifier());
			((RequestListPage.LazyDataModelListenerImpl) ((LazyDataModel<Request>)requestsDataTable.getValue()).getListener()).setSectionIdentifier(section.getIdentifier());
			if(CollectionHelper.isEmpty(selected))
				((RequestListPage.LazyDataModelListenerImpl) ((LazyDataModel<Request>)requestsDataTable.getValue()).getListener()).setExcludedIdentifiers(null);
			else
				((RequestListPage.LazyDataModelListenerImpl) ((LazyDataModel<Request>)requestsDataTable.getValue()).getListener())
				.setExcludedIdentifiers(selected.stream().map(x -> x.getIdentifier()).collect(Collectors.toList()));
			//PrimeFaces.current().executeScript(String.format("PF('%s').show();",dialogWidgetVar));
		}
	}
}