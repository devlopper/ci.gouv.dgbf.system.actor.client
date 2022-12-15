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
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.persistence.query.Filter;

import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.client.controller.entities.BudgetCategory;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.AssignmentsBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.FunctionQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AssignmentsEditScopeFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Assignments assignments;
	private AutoComplete creditManagerHolderAutoComplete;
	private AutoComplete creditManagerAssistantAutoComplete;
	private AutoComplete authorizingOfficerHolderAutoComplete;
	private AutoComplete authorizingOfficerAssistantAutoComplete;
	private AutoComplete financialControllerHolderAutoComplete;
	private AutoComplete financialControllerAssistantAutoComplete;
	private AutoComplete accountingHolderAutoComplete;
	private AutoComplete accountingAssistantAutoComplete;
	private BudgetCategory budgetCategory;
	
	@Override
	protected String __getWindowTitleValue__() {
		if(assignments == null)
			return super.__getWindowTitleValue__();
		return "Affectation : "+(assignments.getActivityAsString()+" | "+assignments.getEconomicNatureAsString());
	}
	
	@Override
	protected void __listenPostConstruct__() {
		assignments = EntityReader.getInstance().readOne(Assignments.class, AssignmentsQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_EDIT
				,AssignmentsQuerier.PARAMETER_NAME_IDENTIFIER, WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
		budgetCategory =  WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(BudgetCategory.class, null);
		super.__listenPostConstruct__();
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		/*
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Activité"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(assignments.getActivityAsString()),Cell.FIELD_WIDTH,9));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Nature économique"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(assignments.getEconomicNatureAsString()),Cell.FIELD_WIDTH,9));
		*/
		buildInputs(cellsMaps);
		buildCommandButton(cellsMaps);
		buildLayout(cellsMaps);
	}
	
	public void buildInputs(Collection<Map<Object,Object>> cellsMaps) {
		creditManagerHolderAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_CREDIT_MANAGER_HOLDER,Assignments.FIELD_CREDIT_MANAGER_HOLDER,cellsMaps,budgetCategory);
		//creditManagerAssistantAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_CREDIT_MANAGER_ASSISTANT,Assignments.FIELD_CREDIT_MANAGER_ASSISTANT,cellsMaps);
		authorizingOfficerHolderAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_AUTHORIZING_OFFICER_HOLDER,Assignments.FIELD_AUTHORIZING_OFFICER_HOLDER,cellsMaps,budgetCategory);
		//authorizingOfficerAssistantAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_AUTHORIZING_OFFICER_ASSISTANT,Assignments.FIELD_AUTHORIZING_OFFICER_ASSISTANT,cellsMaps);
		financialControllerHolderAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_FINANCIAL_CONTROLLER_HOLDER,Assignments.FIELD_FINANCIAL_CONTROLLER_HOLDER,cellsMaps,budgetCategory);
		//financialControllerAssistantAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_FINANCIAL_CONTROLLER_ASSISTANT,Assignments.FIELD_FINANCIAL_CONTROLLER_ASSISTANT,cellsMaps);
		accountingHolderAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_ACCOUNTING_HOLDER,Assignments.FIELD_ACCOUNTING_HOLDER,cellsMaps,budgetCategory);
		//accountingAssistantAutoComplete = addScopeFunctionAutoComplete(assignments,CODE_ACCOUNTING_ASSISTANT,Assignments.FIELD_ACCOUNTING_ASSISTANT,cellsMaps);
	}
	
	public void buildCommandButton(Collection<Map<Object,Object>> cellsMaps) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						assignments.setCreditManagerHolderAsString((String) FieldHelper.readSystemIdentifier(creditManagerHolderAutoComplete.getValue()));
						//assignments.setCreditManagerAssistantAsString((String) FieldHelper.readSystemIdentifier(creditManagerAssistantAutoComplete.getValue()));
						assignments.setAuthorizingOfficerHolderAsString((String) FieldHelper.readSystemIdentifier(authorizingOfficerHolderAutoComplete.getValue()));
						//assignments.setAuthorizingOfficerAssistantAsString((String) FieldHelper.readSystemIdentifier(authorizingOfficerAssistantAutoComplete.getValue()));
						assignments.setFinancialControllerHolderAsString((String) FieldHelper.readSystemIdentifier(financialControllerHolderAutoComplete.getValue()));
						//assignments.setFinancialControllerAssistantAsString((String) FieldHelper.readSystemIdentifier(financialControllerAssistantAutoComplete.getValue()));
						assignments.setAccountingHolderAsString((String) FieldHelper.readSystemIdentifier(accountingHolderAutoComplete.getValue()));						
						//assignments.setAccountingAssistantAsString((String) FieldHelper.readSystemIdentifier(accountingAssistantAutoComplete.getValue()));
						Arguments<Assignments> arguments = new Arguments<Assignments>();
						arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
								.setActionIdentifier(AssignmentsBusiness.SAVE_SCOPE_FUNCTIONS_THEN_EXPORT));
						arguments.setUpdatables(List.of(assignments));
						if(CollectionHelper.isNotEmpty(arguments.getUpdatables())) {
							EntitySaver.getInstance().save(Assignments.class, arguments);
						}
						return null;
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12));
	}
	
	public void buildLayout(Collection<Map<Object,Object>> cellsMaps) {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	private static AutoComplete addScopeFunctionAutoComplete(Assignments assignments,String functionCode,String fieldName,Collection<Map<Object,Object>> cellsMaps,BudgetCategory budgetCategory) {
		if(StringHelper.isBlank(functionCode))
			return null;
		AutoComplete autoComplete = buildScopeFunctionAutoComplete(assignments,functionCode,fieldName,budgetCategory);
		if(autoComplete == null)
			return null;
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,autoComplete.getOutputLabel(),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,autoComplete,Cell.FIELD_WIDTH,9));
		return autoComplete;
	}
	
	public static AutoComplete buildScopeFunctionAutoComplete(Assignments assignments,String functionCode,String fieldName,BudgetCategory budgetCategory) {
		if(StringHelper.isBlank(functionCode) || StringHelper.isBlank(fieldName))
			return null;
		Function function = EntityReader.getInstance().readOne(Function.class, FunctionQuerier.QUERY_IDENTIFIER_READ_BY_CODE_FOR_UI,FunctionQuerier.PARAMETER_NAME_CODE, functionCode);
		//CollectionHelper.getFirst(assignments.getFunctions().stream().filter(x -> functionCode.equals(x.getCode())).collect(Collectors.toList()));
		if(function == null)
			return null;
		return AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ScopeFunction.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,function.getName(),AutoComplete.FIELD_VALUE,assignments == null ? null :FieldHelper.read(assignments, fieldName)
				,AutoComplete.FIELD_READ_QUERY_IDENTIFIER,ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_BY_FUNCTION_CODE_BY_BUDGET_CATEGORY_IDENTIFIER
				,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<ScopeFunction>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				Filter.Dto filter = new Filter.Dto()
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_CODE, functionCode);
				if(budgetCategory != null)
					filter.addField(ScopeFunctionQuerier.PARAMETER_NAME_BUDGET_CATEGORY_IDENTIFIER, budgetCategory.getIdentifier());
				return filter;
			}
		});
	}
}