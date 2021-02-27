package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import ci.gouv.dgbf.system.actor.client.controller.impl.account.ActorListPage;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AssignPrivilegesToActorsByFunctionsPage extends AbstractPageContainerManagedImpl implements Serializable {

	private AutoComplete functionsAutoComplete;
	private DataTable actorsDataTable;
	private CommandButton saveCommandButton;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();		
		functionsAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Function.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
		actorsDataTable = ActorListPage.buildDataTable(DataTable.FIELD_SELECTION_MODE,"multiple");	
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						if(CollectionHelper.isNotEmpty(actorsDataTable.getSelectionAsCollection())) {
							@SuppressWarnings("unchecked")
							Collection<Actor> actors = (Collection<Actor>) actorsDataTable.getSelectionAsCollection();
							@SuppressWarnings("unchecked")
							Collection<Function> functions = (Collection<Function>) functionsAutoComplete.getValue();
							if(CollectionHelper.isNotEmpty(functions)) {
								Arguments<Actor> arguments = new Arguments<Actor>();
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ActorBusiness.CREATE_PRIVILEGES_FROM_FUNCTIONS));																
								CollectionHelper.getFirst(actors).setFunctionsCodes(functions.stream().map(x -> x.getCode()).collect(Collectors.toList()));
								arguments.setUpdatables(actors);
								EntitySaver.getInstance().save(Actor.class, arguments);
							}
						}
						return null;
					}
				});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,functionsAutoComplete,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,actorsDataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation";
	}
}