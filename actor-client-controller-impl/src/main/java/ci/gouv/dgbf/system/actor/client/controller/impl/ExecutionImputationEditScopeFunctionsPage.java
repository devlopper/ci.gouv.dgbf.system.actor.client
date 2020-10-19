package ci.gouv.dgbf.system.actor.client.controller.impl;

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
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;

import ci.gouv.dgbf.system.actor.client.controller.entities.ExecutionImputation;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.business.api.ExecutionImputationBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ExecutionImputationQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ExecutionImputationEditScopeFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private ExecutionImputation executionImputation;
	private AutoComplete gcAutoComplete;
	private AutoComplete ordAutoComplete;
	private AutoComplete cfAutoComplete;
	private AutoComplete cptAutoComplete;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Postes de l'imputation "+executionImputation.getCode();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		executionImputation = EntityReader.getInstance().readOne(ExecutionImputation.class, ExecutionImputationQuerier.QUERY_IDENTIFIER_READ_BY_SYSTEM_IDENTIFIER_FOR_EDIT
				,ExecutionImputationQuerier.PARAMETER_NAME_IDENTIFIER, WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));
		WebController.getInstance().getRequestParameterEntity(ExecutionImputation.class);
		super.__listenPostConstruct__();
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Activité"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(executionImputation.getActivityCodeName()),Cell.FIELD_WIDTH,9));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Nature économique"),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(executionImputation.getEconomicNatureCodeName()),Cell.FIELD_WIDTH,9));
		
		gcAutoComplete = addScopeFunctionAutoComplete(executionImputation,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER,cellsMaps);				
		ordAutoComplete = addScopeFunctionAutoComplete(executionImputation,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER,cellsMaps);
		cfAutoComplete = addScopeFunctionAutoComplete(executionImputation,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER,cellsMaps);
		cptAutoComplete = addScopeFunctionAutoComplete(executionImputation,ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER,cellsMaps);
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						
						executionImputation.getCreditManager(Boolean.TRUE).setHolder((ScopeFunction) gcAutoComplete.getValue());
						executionImputation.getAuthorizingOfficer(Boolean.TRUE).setHolder((ScopeFunction) ordAutoComplete.getValue());
						executionImputation.getFinancialController(Boolean.TRUE).setHolder((ScopeFunction) cfAutoComplete.getValue());
						executionImputation.getAccounting(Boolean.TRUE).setHolder((ScopeFunction) cptAutoComplete.getValue());
						
						Arguments<ExecutionImputation> arguments = new Arguments<ExecutionImputation>();
						arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
								.setActionIdentifier(ExecutionImputationBusiness.SAVE_SCOPE_FUNCTIONS));
						arguments.setUpdatables(List.of(executionImputation));
						if(CollectionHelper.isNotEmpty(arguments.getUpdatables()))
							EntitySaver.getInstance().save(ExecutionImputation.class, arguments);	
						return null;
					}
				},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12));
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
	
	private static AutoComplete addScopeFunctionAutoComplete(ExecutionImputation executionImputation,String functionCode,Collection<Map<Object,Object>> cellsMaps) {
		if(StringHelper.isBlank(functionCode))
			return null;
		AutoComplete autoComplete = buildScopeFunctionAutoComplete(executionImputation,functionCode);
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,autoComplete.getOutputLabel(),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,autoComplete,Cell.FIELD_WIDTH,9));
		return autoComplete;
	}
	
	private static AutoComplete buildScopeFunctionAutoComplete(ExecutionImputation executionImputation,String functionCode) {
		if(StringHelper.isBlank(functionCode))
			return null;
		Function function = CollectionHelper.getFirst(executionImputation.getFunctions().stream().filter(x -> functionCode.equals(x.getCode())).collect(Collectors.toList()));
		if(function == null)
			return null;
		Object value = null;
		if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_CREDIT_MANAGER_HOLDER.equals(functionCode))
			value = executionImputation.getCreditManager() == null ? null : executionImputation.getCreditManager(Boolean.TRUE).getHolder();
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_AUTHORIZING_OFFICER_HOLDER.equals(functionCode))
			value = executionImputation.getAuthorizingOfficer() == null ? null : executionImputation.getAuthorizingOfficer(Boolean.TRUE).getHolder();
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_FINANCIAL_CONTROLLER_HOLDER.equals(functionCode))
			value = executionImputation.getFinancialController() == null ? null : executionImputation.getFinancialController(Boolean.TRUE).getHolder();
		else if(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ACCOUNTING_HOLDER.equals(functionCode))
			value = executionImputation.getAccounting() == null ? null : executionImputation.getAccounting(Boolean.TRUE).getHolder();
		return AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,ScopeFunction.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,function.getName(),AutoComplete.FIELD_VALUE,value
				,AutoComplete.FIELD_READ_QUERY_IDENTIFIER,ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_WHERE_CODE_OR_NAME_LIKE_BY_FUNCTION_CODE
				,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<ScopeFunction>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto()
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_CODE, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_NAME, autoComplete.get__queryString__())
						.addField(ScopeFunctionQuerier.PARAMETER_NAME_FUNCTION_CODE, functionCode);
			}
		});
	}
}