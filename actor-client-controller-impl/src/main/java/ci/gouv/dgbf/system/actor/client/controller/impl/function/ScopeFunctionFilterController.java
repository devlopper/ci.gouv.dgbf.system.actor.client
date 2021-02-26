package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.panel.Dialog;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.panel.Panel;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class ScopeFunctionFilterController implements Serializable {

	private Dialog dialog;
	private CommandButton showDialogCommandButton;
	
	private SelectOneCombo functionSelectOne;
	private Function function;
	
	private Redirector.Arguments onSelectRedirectorArguments;
	
	private CommandButton filterCommandButton;
	
	private Layout layout;
	
	public ScopeFunctionFilterController build() {
		if(function == null)
			function = WebController.getInstance().getRequestParameterEntityAsParentBySystemIdentifier(Function.class, null);
		buildDialog();
		buildShowDialogCommandButton();
		
		buildFunctionSelectOne();
		buildFilterCommandButton();
		buildLayout();
		return this;
	}
	
	public Redirector.Arguments getOnSelectRedirectorArguments(Boolean injectIfNull) {
		if(onSelectRedirectorArguments == null && Boolean.TRUE.equals(injectIfNull))
			onSelectRedirectorArguments = new Redirector.Arguments();
		return onSelectRedirectorArguments;
	}
	
	private void buildDialog() {
		dialog = Dialog.build(Dialog.FIELD_HEADER,"Filtre fonction(s) budgétaire(s)",Dialog.FIELD_MODAL,Boolean.TRUE
				,Dialog.ConfiguratorImpl.FIELD_COMMAND_BUTTONS_BUILDABLE,Boolean.FALSE);
		dialog.addStyleClasses("cyk-min-width-90-percent");
	}
	
	private void buildShowDialogCommandButton() {
		showDialogCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Filtrer",CommandButton.FIELD_ICON,"fa fa-filter"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.SHOW_DIALOG,CommandButton.FIELD___DIALOG__,dialog);		
	}
	
	private void buildFunctionSelectOne() {
		functionSelectOne = SelectOneCombo.build(SelectOneCombo.FIELD_CHOICE_CLASS,Function.class,SelectOneCombo.FIELD_LISTENER
				,new SelectOneCombo.Listener.AbstractImpl<Function>() {
			@Override
			public Collection<Function> computeChoices(AbstractInputChoice<Function> input) {
				Collection<Function> choices = __inject__(FunctionController.class).readCreditManagersAuthorizingOfficersFinancialControllersAssistants();
				CollectionHelper.addNullAtFirstIfSizeGreaterThanOne(choices);
				return choices;
			}
			@Override
			public void select(AbstractInputChoiceOne input, Function function) {
				super.select(input, function);
				
			}
		},SelectOneCombo.ConfiguratorImpl.FIELD_OUTPUT_LABEL_VALUE,"Catégorie de fonction budgétaire");
		functionSelectOne.updateChoices();
		functionSelectOne.selectBySystemIdentifier(FieldHelper.readSystemIdentifier(function));
		//functionSelectOne.enableValueChangeListener(List.of());
	}
	
	private void buildFilterCommandButton() {
		filterCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Filtrer",CommandButton.FIELD_ICON,"fa fa-filter"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_LISTENER
				,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				if(functionSelectOne != null && functionSelectOne.getValue() != null)
					onSelectRedirectorArguments.addParameters(Map.of(ParameterName.stringify(Function.class)
							,List.of(FieldHelper.readSystemIdentifier(functionSelectOne.getValue()).toString())));
				Redirector.getInstance().redirect(onSelectRedirectorArguments);		
				return super.__runExecuteFunction__(action);
			}
		});
	}
	
	private void buildLayout() {
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne.getOutputLabel(),Cell.FIELD_WIDTH,3));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,8));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,filterCommandButton,Cell.FIELD_WIDTH,1));
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps
				,Layout.FIELD_CONTAINER,Panel.build(Panel.FIELD_HEADER,"Filtre",Panel.FIELD_TOGGLEABLE,Boolean.TRUE));
	}
}