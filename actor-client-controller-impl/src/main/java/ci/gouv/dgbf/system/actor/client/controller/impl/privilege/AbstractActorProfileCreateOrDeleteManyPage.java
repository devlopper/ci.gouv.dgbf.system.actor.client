package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.AbstractCollection.RenderType;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.impl.account.ActorListPage;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorProfileCreateOrDeleteManyPage extends AbstractPageContainerManagedImpl implements Serializable {

	protected AutoComplete profilesAutoComplete;
	protected DataTable actorsDataTable;
	protected CommandButton saveCommandButton;
	protected Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();		
		profilesAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Profile.class,AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
		actorsDataTable = ActorListPage.buildDataTable(DataTable.FIELD_SELECTION_MODE,"multiple",DataTable.FIELD_RENDER_TYPE,RenderType.SELECTION);	
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,ActorBusiness.CREATE_PROFILES.equals(getActionIdentifier()) ? "Assigner" : "Retirer"
			,CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						@SuppressWarnings("unchecked")
						Collection<Actor> actors = (Collection<Actor>) actorsDataTable.getSelectionAsCollection();
						if(CollectionHelper.isEmpty(actors))
							throw new RuntimeException("Sélectionner au moins un compte utilisateur");
						@SuppressWarnings("unchecked")
						Collection<Profile> profiles = (Collection<Profile>) profilesAutoComplete.getValue();
						if(CollectionHelper.isEmpty(profiles))
							throw new RuntimeException("Sélectionner au moins un profile");
						Arguments<Actor> arguments = new Arguments<Actor>();
						arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(getActionIdentifier()));																
						CollectionHelper.getFirst(actors).setProfilesCodes(profiles.stream().map(x -> x.getCode()).collect(Collectors.toList()));
						if(ActorBusiness.CREATE_PROFILES.equals(getActionIdentifier()))
							arguments.setCreatables(actors);
						else
							arguments.setDeletables(actors);
						EntitySaver.getInstance().save(Actor.class, arguments);						
						return null;
					}
				});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,profilesAutoComplete,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,actorsDataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return (ActorBusiness.CREATE_PROFILES.equals(getActionIdentifier()) ? "Assignation" : "Retrait")+" de profiles";
	}
	
	protected abstract String getActionIdentifier();
}