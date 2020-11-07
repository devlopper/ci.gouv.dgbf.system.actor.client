package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
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
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectBooleanButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.ExecutionImputation;
import ci.gouv.dgbf.system.actor.server.business.api.ExecutionImputationBusiness;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped @Getter @Setter
public class ExecutionImputationsMassSetScopeFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private DataTable executionImputationsDataTable;
	
	private AutoComplete creditManagerHolderAutoComplete;
	private SelectBooleanButton overrideCreditManagerHolderSelectBooleanButton;
	private AutoComplete creditManagerAssistantAutoComplete;
	private SelectBooleanButton overrideCreditManagerAssistantSelectBooleanButton;
	
	private AutoComplete authorizingOfficerHolderAutoComplete;
	private SelectBooleanButton overrideAuthorizingOfficerHolderSelectBooleanButton;
	private AutoComplete authorizingOfficerAssistantAutoComplete;
	private SelectBooleanButton overrideAuthorizingOfficerAssistantSelectBooleanButton;
	
	private AutoComplete financialControllerHolderAutoComplete;
	private SelectBooleanButton overrideFinancialControllerHolderSelectBooleanButton;
	private AutoComplete financialControllerAssistantAutoComplete;
	private SelectBooleanButton overrideFinancialControllerAssistantSelectBooleanButton;
	
	private AutoComplete accountingHolderAutoComplete;
	private SelectBooleanButton overrideAccountingHolderSelectBooleanButton;
	private AutoComplete accountingAssistantAutoComplete;
	private SelectBooleanButton overrideAccountingAssistantSelectBooleanButton;
	
	private CommandButton saveCommandButton;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Affectation par modèle";
	}
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();		
		buildFilters();
		buildDataTable();
		buildSaveCommandButton();		
		buildLayout();
	}
	
	private void buildFilters() {
		creditManagerHolderAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER
				,FIELD_CREDIT_MANAGER_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER);
		creditManagerAssistantAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_ASSISTANT
				,FIELD_CREDIT_MANAGER_ASSISTANT_AUTO_COMPLETE,ExecutionImputation.FIELD_CREDIT_MANAGER_ASSISTANT);
		
		authorizingOfficerHolderAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER
				,FIELD_AUTHORIZING_OFFICER_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER);
		authorizingOfficerAssistantAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_ASSISTANT
				,FIELD_AUTHORIZING_OFFICER_ASSISTANT_AUTO_COMPLETE,ExecutionImputation.FIELD_AUTHORIZING_OFFICER_ASSISTANT);
		
		financialControllerHolderAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER
				,FIELD_FINANCIAL_CONTROLLER_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER);
		financialControllerAssistantAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_ASSISTANT
				,FIELD_FINANCIAL_CONTROLLER_ASSISTANT_AUTO_COMPLETE,ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_ASSISTANT);
		
		accountingHolderAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER
				,FIELD_ACCOUNTING_HOLDER_AUTO_COMPLETE,ExecutionImputation.FIELD_ACCOUNTING_HOLDER);
		accountingAssistantAutoComplete = ExecutionImputationsEditScopeFunctionsPage.buildScopeFunctionAutoComplete(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_ASSISTANT
				,FIELD_ACCOUNTING_ASSISTANT_AUTO_COMPLETE,ExecutionImputation.FIELD_ACCOUNTING_ASSISTANT);
		
		String offLabel = "Non, ne pas écraser existant",onLabel = "Oui, écraser existant";
		overrideCreditManagerHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		overrideCreditManagerAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		
		overrideAuthorizingOfficerHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		overrideAuthorizingOfficerAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		
		overrideFinancialControllerHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		overrideFinancialControllerAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		
		overrideAccountingHolderSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
		overrideAccountingAssistantSelectBooleanButton = SelectBooleanButton.build(SelectBooleanButton.FIELD_OFF_LABEL,offLabel,SelectBooleanButton.FIELD_ON_LABEL,onLabel);
	}
	
	private void buildSaveCommandButton() {
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL)
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@SuppressWarnings("unchecked")
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						Map<String,Object> filters = ((LazyDataModel<ExecutionImputation>)executionImputationsDataTable.getValue()).get__filters__();						
						if(filters == null || MapHelper.isEmpty(filters) || CollectionHelper.isEmpty(filters.values().stream().filter(value -> StringHelper.isNotBlank((String)value)).collect(Collectors.toList())))
							throw new RuntimeException("Le filtre est obligatoire");
						ExecutionImputation model = new ExecutionImputation();
						model.setFilter(((LazyDataModel<ExecutionImputation>)executionImputationsDataTable.getValue()).get__filter__());
						model.getCreditManager(Boolean.TRUE).setHolderIdentifier((String) FieldHelper.readSystemIdentifier(creditManagerHolderAutoComplete.getValue()));
						model.getCreditManager(Boolean.TRUE).setHolderOverridable(overrideCreditManagerHolderSelectBooleanButton.getValue());
						model.getCreditManager(Boolean.TRUE).setAssistantIdentifier((String) FieldHelper.readSystemIdentifier(creditManagerAssistantAutoComplete.getValue()));
						model.getCreditManager(Boolean.TRUE).setAssistantOverridable(overrideCreditManagerAssistantSelectBooleanButton.getValue());
						
						model.getAuthorizingOfficer(Boolean.TRUE).setHolderIdentifier((String) FieldHelper.readSystemIdentifier(authorizingOfficerHolderAutoComplete.getValue()));
						model.getAuthorizingOfficer(Boolean.TRUE).setHolderOverridable(overrideAuthorizingOfficerHolderSelectBooleanButton.getValue());
						model.getAuthorizingOfficer(Boolean.TRUE).setAssistantIdentifier((String) FieldHelper.readSystemIdentifier(authorizingOfficerAssistantAutoComplete.getValue()));
						model.getAuthorizingOfficer(Boolean.TRUE).setAssistantOverridable(overrideAuthorizingOfficerAssistantSelectBooleanButton.getValue());
						
						model.getFinancialController(Boolean.TRUE).setHolderIdentifier((String) FieldHelper.readSystemIdentifier(financialControllerHolderAutoComplete.getValue()));
						model.getFinancialController(Boolean.TRUE).setHolderOverridable(overrideFinancialControllerHolderSelectBooleanButton.getValue());
						model.getFinancialController(Boolean.TRUE).setAssistantIdentifier((String) FieldHelper.readSystemIdentifier(financialControllerAssistantAutoComplete.getValue()));
						model.getFinancialController(Boolean.TRUE).setAssistantOverridable(overrideFinancialControllerAssistantSelectBooleanButton.getValue());
						
						model.getAccounting(Boolean.TRUE).setHolderIdentifier((String) FieldHelper.readSystemIdentifier(accountingHolderAutoComplete.getValue()));
						model.getAccounting(Boolean.TRUE).setHolderOverridable(overrideAccountingHolderSelectBooleanButton.getValue());
						model.getAccounting(Boolean.TRUE).setAssistantIdentifier((String) FieldHelper.readSystemIdentifier(accountingAssistantAutoComplete.getValue()));
						model.getAccounting(Boolean.TRUE).setAssistantOverridable(overrideAccountingAssistantSelectBooleanButton.getValue());
						
						EntitySaver.getInstance().save(ExecutionImputation.class, new Arguments<ExecutionImputation>()
								.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setActionIdentifier(ExecutionImputationBusiness.DERIVE_SCOPE_FUNCTIONS_FROM_MODEL)).setUpdatables(List.of(model)));							
						return null;
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right");
		saveCommandButton.addUpdates(":form:"+executionImputationsDataTable.getIdentifier());
	}
	
	private void buildDataTable() {
		executionImputationsDataTable = ExecutionImputationListPage.buildDataTable(DataTable.FIELD_LISTENER,new DataTableListenerImpl()
				,DataTable.ConfiguratorImpl.FIELD_LAZY_DATA_MODEL_LISTENER,new LazyDataModelListenerImpl()
				,DataTable.FIELD_RENDER_TYPE,org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType.INPUT
				);
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cells = new ArrayList<>();
		buildLayoutAddInput(cells, creditManagerHolderAutoComplete,overrideCreditManagerHolderSelectBooleanButton);
		buildLayoutAddInput(cells, creditManagerAssistantAutoComplete,overrideCreditManagerAssistantSelectBooleanButton);
		
		buildLayoutAddInput(cells, authorizingOfficerHolderAutoComplete,overrideAuthorizingOfficerHolderSelectBooleanButton);
		buildLayoutAddInput(cells, authorizingOfficerAssistantAutoComplete,overrideAuthorizingOfficerAssistantSelectBooleanButton);
		
		buildLayoutAddInput(cells, financialControllerHolderAutoComplete,overrideFinancialControllerHolderSelectBooleanButton);
		buildLayoutAddInput(cells, financialControllerAssistantAutoComplete,overrideFinancialControllerAssistantSelectBooleanButton);
		
		buildLayoutAddInput(cells, accountingHolderAutoComplete,overrideAccountingHolderSelectBooleanButton);
		buildLayoutAddInput(cells, accountingAssistantAutoComplete,overrideAccountingAssistantSelectBooleanButton);
		
		cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12));
		cells.add(MapHelper.instantiate(Cell.FIELD_CONTROL,executionImputationsDataTable,Cell.FIELD_WIDTH,12));
		
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
	public class DataTableListenerImpl extends ExecutionImputationListPage.DataTableListenerImpl implements Serializable {
		@Override
		public Map<Object, Object> getColumnArguments(AbstractDataTable dataTable, String fieldName) {
			Map<Object, Object> map = super.getColumnArguments(dataTable, fieldName);
			if(ExecutionImputation.FIELD_CREDIT_MANAGER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_CREDIT_MANAGER_ASSISTANT.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_AUTHORIZING_OFFICER_ASSISTANT.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_FINANCIAL_CONTROLLER_ASSISTANT.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_ACCOUNTING_HOLDER.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}else if(ExecutionImputation.FIELD_ACCOUNTING_ASSISTANT.equals(fieldName)) {
				map.put(Column.FIELD_INPUTABLE, Boolean.TRUE);
			}
			return map;
		}
	}
	
	@Getter @Setter @Accessors(chain=true)
	public class LazyDataModelListenerImpl extends ExecutionImputationListPage.LazyDataModelListenerImpl implements Serializable {	
		
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