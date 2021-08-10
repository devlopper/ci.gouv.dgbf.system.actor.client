package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractDataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Column;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.InputTextarea;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneRadio;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.controller.Arguments;

import ci.gouv.dgbf.system.actor.client.controller.api.ActorScopeRequestController;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorScopeRequest;
import ci.gouv.dgbf.system.actor.server.business.api.ActorScopeRequestBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorScopeRequestQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ActorScopeRequestProcessManyPage extends AbstractPageContainerManagedImpl implements Serializable {

	private ActorScopeRequestFilterController filterController;
	private Layout layout;
	private DataTable dataTable;
	private String dataTableCellIdentifier = RandomHelper.getAlphabetic(5);
	private List<ActorScopeRequest> actorScopeRequests;
	private SelectOneRadio grantedInput;
	private InputTextarea processingCommentInput;
	private CommandButton saveCommandButton;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		filterController = new ActorScopeRequestFilterController().setActionIdentifier(ActorScopeRequestBusiness.PROCESS);
		filterController.ignore(ActorScopeRequestFilterController.FIELD_PROCESSED_SELECT_ONE,ActorScopeRequestFilterController.FIELD_GRANTED_SELECT_ONE);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return filterController.generateWindowTitleValue("Traitement des demandes de domaines");
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		grantedInput = buildGrantedInput(ActorScopeRequestProcessManyPage.class, FIELD_GRANTED_INPUT);
		processingCommentInput = buildProcessingCommentInput(ActorScopeRequestProcessManyPage.class, FIELD_PROCESSING_COMMENT_INPUT);
		buildLayout();
	}
	
	private DataTable buildDataTable() {
		dataTable = ActorScopeRequestListPage.buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl()
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().setFilterController(filterController)
				,ActorScopeRequestListPage.OUTCOME,OUTCOME
				
				,DataTable.FIELD_RENDER_TYPE,org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType.INPUT);
		return dataTable;
	}
	
	private CommandButton buildSaveCommandButton() {
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						return __inject__(ActorScopeRequestController.class).process(dataTable.getList(ActorScopeRequest.class), GRANTED_CHOICE_IGNORE, GRANTED_CHOICE_YES);
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right");
		saveCommandButton.addUpdates(":form:"+dataTableCellIdentifier);
		return saveCommandButton;
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_IDENTIFIER,dataTableCellIdentifier,Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE
				,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildDataTable();
			}
		},Cell.FIELD_WIDTH,12));
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,buildSaveCommandButton(),Cell.FIELD_WIDTH,12));
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	/**/
	
	public static SelectOneRadio buildGrantedInput(Class<?> klass,String inputFieldName) {
		SelectOneRadio input = SelectOneRadio.build(SelectOneRadio.ConfiguratorImpl.FIELD_CHOICES_ARE_YES_NO_ONLY,Boolean.TRUE);
		//System.out.println("ActorScopeRequestProcessManyPage.buildGrantedInput() ::::::::::::::::::::::::::::::::::::::::: "+input.getChoices());
		input.setBindingByDerivation(StringHelper.getVariableNameFrom(klass.getSimpleName())+"."+inputFieldName, "record.granted");
		return input;
	}
	
	public static InputTextarea buildProcessingCommentInput(Class<?> klass,String inputFieldName) {
		InputTextarea input = InputTextarea.build();
		input.setBindingByDerivation(StringHelper.getVariableNameFrom(klass.getSimpleName())+"."+inputFieldName, "record.processingComment");
		return input;
	}
	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public static class DataTableListenerImpl extends ActorScopeRequestListPage.DataTableListenerImpl implements Serializable {
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(ActorScopeRequest.FIELD_GRANTED_AS_STRING.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ActorScopeRequest.FIELD_PROCESSING_COMMENT.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public static class LazyDataModelListenerImpl extends ActorScopeRequestListPage.LazyDataModelListenerImpl implements Serializable {
		@Override
		public Arguments<ActorScopeRequest> instantiateArguments(LazyDataModel<ActorScopeRequest> lazyDataModel) {
			return super.instantiateArguments(lazyDataModel).filterFieldsValues(ActorScopeRequestQuerier.PARAMETER_NAME_PROCESSED,Boolean.FALSE);
		}
		
		@Override
		public List<ActorScopeRequest> read(LazyDataModel<ActorScopeRequest> lazyDataModel) {
			List<ActorScopeRequest> actorScopeRequests = super.read(lazyDataModel);
			if(CollectionHelper.isNotEmpty(actorScopeRequests))
				actorScopeRequests.forEach(actorScopeRequest -> {
					actorScopeRequest.setGrantedAsString(GRANTED_CHOICE_IGNORE);
				});
			return actorScopeRequests;
		}
	}
		
	/**/
	
	public static final String GRANTED_CHOICE_YES = "yes";
	public static final String GRANTED_CHOICE_NO = "no";
	public static final String GRANTED_CHOICE_IGNORE = "ignore";
	
	public static final String FIELD_GRANTED_INPUT = "grantedInput";
	public static final String FIELD_PROCESSING_COMMENT_INPUT = "processingCommentInput";
	public static final String FIELD_IGNORE_INPUT = "ignoreInput";
	
	public static final String OUTCOME = "actorScopeRequestProcessManyView";
}