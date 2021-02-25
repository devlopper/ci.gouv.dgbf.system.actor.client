package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import static ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER;
import static ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER;
import static ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER;
import static ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.random.RandomHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.server.business.api.AssignmentsBusiness;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class AssignmentsEditScopeFunctionsByModelPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private DataTable assignmentsDataTable;
	private String assignmentsDataTableCellIdentifier = RandomHelper.getAlphabetic(5);
	private Assignments model = new Assignments();
	
	private AutoComplete creditManagerHolderAutoComplete;
	private SelectBooleanButton creditManagerHolderSelectBooleanButton;
	//private AutoComplete creditManagerAssistantAutoComplete;
	//private SelectBooleanButton creditManagerAssistantSelectBooleanButton;
	
	private AutoComplete authorizingOfficerHolderAutoComplete;
	private SelectBooleanButton authorizingOfficerHolderSelectBooleanButton;
	//private AutoComplete authorizingOfficerAssistantAutoComplete;
	//private SelectBooleanButton authorizingOfficerAssistantSelectBooleanButton;
	
	private AutoComplete financialControllerHolderAutoComplete;
	private SelectBooleanButton financialControllerHolderSelectBooleanButton;
	//private AutoComplete financialControllerAssistantAutoComplete;
	//private SelectBooleanButton financialControllerAssistantSelectBooleanButton;
	
	private AutoComplete accountingHolderAutoComplete;
	private SelectBooleanButton accountingHolderSelectBooleanButton;
	//private AutoComplete accountingAssistantAutoComplete;
	//private SelectBooleanButton accountingAssistantSelectBooleanButton;
	
	private CommandButton saveCommandButton;
	
	private AssignmentsListPage.PageArguments pageArguments = new AssignmentsListPage.PageArguments();
	
	@Override
	protected String __getWindowTitleValue__() {
		return AssignmentsListPage.buildWindowTitleValue("Affectation par modèle", pageArguments);
	}
	
	@Override
	protected void __listenPostConstruct__() {
		pageArguments.initialize();
		super.__listenPostConstruct__();		
		buildFilters();
		buildSaveCommandButton();		
		buildLayout();
	}
	
	private void buildFilters() {
		creditManagerHolderAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_CREDIT_MANAGER_HOLDER
				,Assignments.FIELD_CREDIT_MANAGER_HOLDER);
		//creditManagerAssistantAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_CREDIT_MANAGER_ASSISTANT
		//		,Assignments.FIELD_CREDIT_MANAGER_ASSISTANT);
		
		authorizingOfficerHolderAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_AUTHORIZING_OFFICER_HOLDER
				,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER);
		//authorizingOfficerAssistantAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_AUTHORIZING_OFFICER_ASSISTANT
		//		,Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT);
		
		financialControllerHolderAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_FINANCIAL_CONTROLLER_HOLDER
				,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER);
		//financialControllerAssistantAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_FINANCIAL_CONTROLLER_ASSISTANT
		//		,Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT);
		
		accountingHolderAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_ACCOUNTING_HOLDER
				,Assignments.FIELD_ACCOUNTING_HOLDER);
		//accountingAssistantAutoComplete = AssignmentsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(model,CODE_ACCOUNTING_ASSISTANT
		//		,Assignments.FIELD_ACCOUNTING_ASSISTANT);
		
		String offLabel = "Non, ne pas écraser existant",onLabel = "Oui, écraser existant";
		creditManagerHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		//creditManagerAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		
		authorizingOfficerHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		//authorizingOfficerAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		
		financialControllerHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		//financialControllerAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		
		accountingHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		//accountingAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
	}
	
	private void buildSaveCommandButton() {
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@SuppressWarnings("unchecked")
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						LazyDataModel<Assignments> lazyDataModel = assignmentsDataTable == null ? null : (LazyDataModel<Assignments>)assignmentsDataTable.getValue();
						//AssignmentsListPage.LazyDataModelListenerImpl lazyDataModelListener = (AssignmentsListPage.LazyDataModelListenerImpl) lazyDataModel.getListener();						
						Filter.Dto filter = lazyDataModel == null ? null : lazyDataModel.get__filter__();
						if(filter == null || CollectionHelper.isEmpty(filter.getFields()))
							throw new RuntimeException("Le filtre est obligatoire");
						model.setFilter(filter);
						/*
						Map<String,Object> filters = ((LazyDataModel<Assignments>)assignmentsDataTable.getValue()).get__filters__();						
						if(filters == null || MapHelper.isEmpty(filters) || CollectionHelper.isEmpty(filters.values().stream().filter(value -> StringHelper.isNotBlank((String)value)).collect(Collectors.toList())))
							throw new RuntimeException("Le filtre est obligatoire");
						model.setFilter(((LazyDataModel<Assignments>)assignmentsDataTable.getValue()).get__filter__());
						*/
						copyToModel(Assignments.FIELD_CREDIT_MANAGER_HOLDER);
						//copyToModel(Assignments.FIELD_CREDIT_MANAGER_ASSISTANT);
						copyToModel(Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER);
						//copyToModel(Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT);
						copyToModel(Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER);
						//copyToModel(Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT);
						copyToModel(Assignments.FIELD_ACCOUNTING_HOLDER);
						//copyToModel(Assignments.FIELD_ACCOUNTING_ASSISTANT);
						EntitySaver.getInstance().save(Assignments.class, new Arguments<Assignments>()
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setActionIdentifier(AssignmentsBusiness.APPLY_MODEL)).setUpdatables(List.of(model)));							
						return null;
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right");
		saveCommandButton.addUpdates(":form:"+assignmentsDataTableCellIdentifier);
	}
	
	private void copyToModel(String fieldName) {
		AutoComplete autoComplete = (AutoComplete) FieldHelper.read(this, fieldName+"AutoComplete");
		FieldHelper.write(model, fieldName+"AsString", FieldHelper.readSystemIdentifier(autoComplete.getValue()));
		SelectBooleanButton selectBooleanButton = (SelectBooleanButton) FieldHelper.read(this, fieldName+"SelectBooleanButton");
		if(Boolean.TRUE.equals(selectBooleanButton.getValue()))
			model.getOverridablesFieldsNames(Boolean.TRUE).add(fieldName);
	}
	
	private DataTable buildDataTable() {
		assignmentsDataTable = AssignmentsListPage.buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl()
				,DataTable.ConfiguratorImpl.FIELD_COLUMNS_FIELDS_NAMES,AssignmentsListPage.DataTableListenerImpl.buildColumnsNames(pageArguments)
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl().applyPageArguments(pageArguments)
				,DataTable.FIELD_RENDER_TYPE,org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType.INPUT
				);
		return assignmentsDataTable;
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cells = new ArrayList<>();
		buildLayoutAddInput(cells, creditManagerHolderAutoComplete,creditManagerHolderSelectBooleanButton);
		//buildLayoutAddInput(cells, creditManagerAssistantAutoComplete,creditManagerAssistantSelectBooleanButton);
		
		buildLayoutAddInput(cells, authorizingOfficerHolderAutoComplete,authorizingOfficerHolderSelectBooleanButton);
		//buildLayoutAddInput(cells, authorizingOfficerAssistantAutoComplete,authorizingOfficerAssistantSelectBooleanButton);
		
		buildLayoutAddInput(cells, financialControllerHolderAutoComplete,financialControllerHolderSelectBooleanButton);
		//buildLayoutAddInput(cells, financialControllerAssistantAutoComplete,financialControllerAssistantSelectBooleanButton);
		
		buildLayoutAddInput(cells, accountingHolderAutoComplete,accountingHolderSelectBooleanButton);
		//buildLayoutAddInput(cells, accountingAssistantAutoComplete,accountingAssistantSelectBooleanButton);
		
		cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12));
		cells.add(MapHelper.instantiate(Cell.FIELD_IDENTIFIER,assignmentsDataTableCellIdentifier,Cell.ConfiguratorImpl.FIELD_CONTROL_BUILD_DEFFERED,Boolean.TRUE
				,Cell.FIELD_LISTENER,new Cell.Listener.AbstractImpl() {
			@Override
			public Object buildControl(Cell cell) {
				return buildDataTable();
			}
		},Cell.FIELD_WIDTH,12));
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cells);
	}
	
	private void buildLayoutAddInput(Collection<Map<Object,Object>> cells,AutoComplete autoComplete,SelectBooleanButton overrideSelectBooleanButton) {
		cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,autoComplete.getOutputLabel(),Cell.FIELD_WIDTH,2));
		cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,autoComplete,Cell.FIELD_WIDTH,7));
		//cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Écraser ?"),Cell.FIELD_WIDTH,1));
		cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,overrideSelectBooleanButton,Cell.FIELD_WIDTH,3));
	}
		

	
	/**/
	
	@Getter @Setter @Accessors(chain=true)
	public class DataTableListenerImpl extends AssignmentsListPage.DataTableListenerImpl implements Serializable {
		
	}
	
	@Getter @Setter @Accessors(chain=true)
	public class LazyDataModelListenerImpl extends AssignmentsListPage.LazyDataModelListenerImpl implements Serializable {	
		
	}

	/**/
	
	public static final String FIELD_CREDIT_MANAGER_HOLDER_AUTO_COMPLETE = "creditManagerHolderAutoComplete";
	public static final String FIELD_CREDIT_MANAGER_ASSISTANT_AUTO_COMPLETE = "creditManagerAssistantAutoComplete";
	
	public static final String FIELD_AUTHORIZING_OFFICER_HOLDER_AUTO_COMPLETE = "authorizingOfficerHolderAutoComplete";
	public static final String FIELD_AUTHORIZING_OFFICER_ASSISTANT_AUTO_COMPLETE = "authorizingOfficerAssistantAutoComplete";
	
	public static final String FIELD_FINANCIAL_CONTROLLER_HOLDER_AUTO_COMPLETE = "financialControllerHolderAutoComplete";
	public static final String FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AUTO_COMPLETE = "financialControllerAssistantAutoComplete";
	
	public static final String FIELD_ACCOUNTING_HOLDER_AUTO_COMPLETE = "accountingHolderAutoComplete";
	public static final String FIELD_ACCOUNTING_ASSISTANT_AUTO_COMPLETE = "accountingAssistantAutoComplete";
}