package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorCreatePrivilegesByPage extends AbstractPageContainerManagedImpl implements Serializable {

	protected Actor actor;
	protected Profile profile;
	protected DataTable dataTable;
	protected CommandButton saveCommandButton;
	protected Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntity(Actor.class);
		profile = Helper.getProfileFromRequestParameterEntityAsParent(actor);
		super.__listenPostConstruct__();
		dataTable = buildDataTable();
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						create();
						if(!Boolean.TRUE.equals(action.get__isWindowContainerRenderedAsDialog__())) {
							JsfController.getInstance().redirect("actorListPrivilegesView",Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(actor.getIdentifier())));
						}
						return null;
					}
				});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			));
	}
	
	protected abstract DataTable buildDataTable();
	
	@Override
	protected String __getWindowTitleValue__() {
		return ActorEditPrivilegesPage.formatWindowTitle(actor, getByName());
	}
	
	protected abstract String getByName();
	
	protected void create() {
		if(CollectionHelper.isEmpty(dataTable.getSelection()))
			return;
		__create__();
	}
	
	protected abstract void __create__();
}