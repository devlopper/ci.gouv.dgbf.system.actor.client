package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.Redirector;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoice;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AbstractInputChoiceOne;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectOneCombo;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.panel.Dialog;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class FunctionSelectionController implements Serializable {

	private Dialog dialog;
	private CommandButton showDialogCommandButton;
	
	private SelectOneCombo functionSelectOne;
	private Redirector.Arguments onSelectRedirectorArguments;
	
	private CommandButton selectCommandButton;
	
	private Layout layout;
	
	public FunctionSelectionController() {
		buildDialog();
		buildShowDialogCommandButton();
		
		buildFunctionSelectOne();
		buildLayout();
	}
	
	public Redirector.Arguments getOnSelectRedirectorArguments(Boolean injectIfNull) {
		if(onSelectRedirectorArguments == null && Boolean.TRUE.equals(injectIfNull))
			onSelectRedirectorArguments = new Redirector.Arguments();
		return onSelectRedirectorArguments;
	}
	
	private void buildDialog() {
		dialog = Dialog.build(Dialog.FIELD_HEADER,"Recherche d'une activité",Dialog.FIELD_MODAL,Boolean.TRUE
				,Dialog.ConfiguratorImpl.FIELD_COMMAND_BUTTONS_BUILDABLE,Boolean.FALSE);
		dialog.addStyleClasses("cyk-min-width-90-percent");
	}
	
	private void buildShowDialogCommandButton() {
		showDialogCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Rechercher",CommandButton.FIELD_ICON,"fa fa-search"
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
		});
		functionSelectOne.updateChoices();
		functionSelectOne.enableValueChangeListener(List.of());
	}
	
	private void buildLayout() {
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,List.of(
				MapHelper.instantiate(Cell.FIELD_CONTROL,functionSelectOne,Cell.FIELD_WIDTH,12)
		));
	}
}