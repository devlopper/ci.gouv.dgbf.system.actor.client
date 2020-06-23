package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorCreateScopesOrPrivielesPage<T> extends AbstractPageContainerManagedImpl implements Serializable {

	protected Layout layout;
	protected Actor actor;
	protected DataTable dataTable;
	protected CommandButton createCommandButton;
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntityAsParent(Actor.class);
		super.__listenPostConstruct__();
		dataTable = instantiateDataTable();
	
		createCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION);
		createCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@SuppressWarnings("unchecked")
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				if(CollectionHelper.isNotEmpty(dataTable.getSelection()))
					create((Collection<T>) dataTable.getSelection());
				return null;
			}
		});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS
				,CollectionHelper.listOf(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)
						,MapHelper.instantiate(Cell.FIELD_CONTROL,createCommandButton,Cell.FIELD_WIDTH,12)));
	}
	
	protected abstract DataTable instantiateDataTable();
	
	protected abstract void create(Collection<T> collection);	
}