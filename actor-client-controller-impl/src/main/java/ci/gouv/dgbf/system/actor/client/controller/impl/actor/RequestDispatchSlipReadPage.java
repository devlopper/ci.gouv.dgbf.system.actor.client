package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.enumeration.Action;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.session.SessionManager;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.value.ValueHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.Button;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.RequestDispatchSlip;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.business.api.RequestDispatchSlipBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.RequestDispatchSlipQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.entities.Profile;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestDispatchSlipReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private RequestDispatchSlip requestDispatchSlip;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		String identifier = WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER);
		if(StringHelper.isNotBlank(identifier)) {
			requestDispatchSlip = EntityReader.getInstance().readOne(RequestDispatchSlip.class, RequestDispatchSlipQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI
					,RequestDispatchSlipQuerier.PARAMETER_NAME_IDENTIFIER, identifier);
		}
		super.__listenPostConstruct__();
		if(requestDispatchSlip != null) {
			Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
			buildCommands(cellsMaps);
			buildTexts(cellsMaps);
			buildRequestsDataTable(cellsMaps);
			buildLayout(cellsMaps);
		}		
	}
	
	private void buildCommands(Collection<Map<Object,Object>> cellsMaps) {
		if(StringHelper.isBlank(requestDispatchSlip.getSendingDateAsString())) {
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
					,Button.build(Button.FIELD_VALUE,"Modifier",Button.FIELD_ICON,"fa fa-edit",Button.FIELD_OUTCOME,RequestDispatchSlipEditPage.OUTCOME
					,Button.FIELD_PARAMETERS,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),requestDispatchSlip.getIdentifier()
							,ParameterName.ACTION_IDENTIFIER.getValue(),Action.UPDATE.name())),Cell.FIELD_WIDTH,1));
			
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
					,CommandButton.build(CommandButton.FIELD_VALUE,"Transmettre",CommandButton.FIELD_ICON,"fa fa-send"
							,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
							,CommandButton.ConfiguratorImpl.FIELD_CONFIRMABLE,Boolean.TRUE
							,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
						protected Object __runExecuteFunction__(AbstractAction action) {
							EntitySaver.getInstance().save(RequestDispatchSlip.class, new Arguments<RequestDispatchSlip>().setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
									.setActionIdentifier(RequestDispatchSlipBusiness.SEND)).addCreatablesOrUpdatables(requestDispatchSlip));		
							writeOnShowNotificationMessage("Bordereau transmis", RequestDispatchSlipIndexPage.class);
							Redirector.getInstance().redirect(RequestDispatchSlipIndexPage.OUTCOME
									,Map.of(TabMenu.Tab.PARAMETER_NAME,List.of(RequestDispatchSlipIndexPage.TAB_REQUEST_DISPATCH_SLIPS_TO_PROCESS)));
							return null;
						}
					}),Cell.FIELD_WIDTH,11));
		}else if(StringHelper.isBlank(requestDispatchSlip.getProcessingDateAsString())) {
			if(SessionManager.getInstance().isUserHasRole(Profile.CODE_CHARGE_ETUDE_DAS)) {
				cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL
						,Button.build(Button.FIELD_VALUE,"Traiter",Button.FIELD_ICON,"fa fa-gear",Button.FIELD_OUTCOME,RequestDispatchSlipProcessPage.OUTCOME
						,Button.FIELD_PARAMETERS,Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),requestDispatchSlip.getIdentifier())),Cell.FIELD_WIDTH,12));
			}			
		}
		
	}
	
	private void buildTexts(Collection<Map<Object,Object>> cellsMaps) {
		//buildTexts(cellsMaps, "Catégorie de fonction budgétaire", requestDispatchSlip.getFunctionAsString());
		/*
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Catégorie de fonction budgétaire : "
				+requestDispatchSlip.getFunctionAsString()),Cell.FIELD_WIDTH,4));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Section : "+requestDispatchSlip.getSection()),Cell.FIELD_WIDTH,8));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Code : "+requestDispatchSlip.getCode()),Cell.FIELD_WIDTH,3));
		*/
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Créé le : "+requestDispatchSlip.getCreationDateAsString()),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Transmis le : "
				+ValueHelper.defaultToIfBlank(requestDispatchSlip.getSendingDateAsString(),"-")),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Traité le : "
				+ValueHelper.defaultToIfBlank(requestDispatchSlip.getProcessingDateAsString(),"-")),Cell.FIELD_WIDTH,3));
		/*
		buildTexts(cellsMaps, "Code", requestDispatchSlip.getCode());
		buildTexts(cellsMaps, "Date de création", requestDispatchSlip.getCreationDateAsString());
		buildTexts(cellsMaps, "Date de transmission", requestDispatchSlip.getSendingDateAsString());
		buildTexts(cellsMaps, "Date de traitement", requestDispatchSlip.getProcessingDateAsString());
		*/
	}
	/*
	private void buildTexts(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label),Cell.FIELD_WIDTH,2));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(ValueHelper.defaultToIfBlank(value,"---")),Cell.FIELD_WIDTH,10));
	}*/
	
	private void buildRequestsDataTable(Collection<Map<Object,Object>> cellsMaps) {
		DataTable dataTable = RequestListPage.buildDataTable(DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER
				,new RequestListPage.LazyDataModelListenerImpl().setDispatchSlipIdentifier(requestDispatchSlip.getIdentifier())
				,DataTable.FIELD_STYLE_CLASS, "cyk-ui-datatable-header-visibility-hidden cyk-ui-datatable-footer-visibility-hidden"
				,RequestListPage.class,RequestDispatchSlipReadPage.class);		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12));
	}
	
	private void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);	
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(requestDispatchSlip == null)
			return super.__getWindowTitleValue__();
		return Helper.formatTitleRequestDispatchSlip(requestDispatchSlip, Action.READ);
	}
	
	public static final String OUTCOME = "requestDispatchSlipReadView";
}