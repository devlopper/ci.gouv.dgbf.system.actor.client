package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.api.ScopeTypeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeType;
import ci.gouv.dgbf.system.actor.server.business.api.ActorBusiness;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractActorScopeCreateOrDeleteManyPage<SCOPE> extends AbstractPageContainerManagedImpl implements Serializable {

	protected ScopeType scopeType;
	protected AutoComplete scopesAutoComplete;
	protected DataTable actorsDataTable;
	protected CommandButton saveCommandButton;
	protected Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();
		scopeType = WebController.getInstance().getRequestParameterEntity(ScopeType.class);
		if(scopeType == null)
			scopeType = __inject__(ScopeTypeController.class).readByBusinessIdentifier(getScopeTypeCode());
		
		scopesAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,getScopeClass(),AutoComplete.FIELD_READER_USABLE,Boolean.TRUE
				,AutoComplete.FIELD_MULTIPLE,Boolean.TRUE);
		actorsDataTable = ActorListPage.buildDataTable(DataTable.FIELD_SELECTION_MODE,"multiple",DataTable.ConfiguratorImpl.FIELD_USABLE_AS_SELECTION_ONLY,Boolean.TRUE);	
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,ActorBusiness.CREATE_SCOPES.equals(getActionIdentifier()) ? "Affecter" : "Retirer"
			,CommandButton.FIELD_ICON,"fa fa-floppy-o"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION,CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
				,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
					@Override
					protected Object __runExecuteFunction__(AbstractAction action) {
						@SuppressWarnings("unchecked")
						Collection<Actor> actors = (Collection<Actor>) actorsDataTable.getSelection();
						if(CollectionHelper.isEmpty(actors))
							throw new RuntimeException("Sélectionner au moins un compte");
						@SuppressWarnings("unchecked")
						Collection<Object> scopes = (Collection<Object>) scopesAutoComplete.getValue();
						if(CollectionHelper.isEmpty(scopes))
							throw new RuntimeException("Sélectionner au moins "+scopeType.getName());												
						Arguments<Actor> arguments = new Arguments<Actor>();
						arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(getActionIdentifier()));																
						CollectionHelper.getFirst(actors).setScopesIdentifiers(scopes.stream().map(x -> (String)FieldHelper.readSystemIdentifier(x)).collect(Collectors.toList()));
						if(ActorBusiness.CREATE_SCOPES.equals(getActionIdentifier()))
							arguments.setCreatables(actors);
						else
							arguments.setDeletables(actors);
						EntitySaver.getInstance().save(Actor.class, arguments);		
						return null;
					}
				});
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,scopesAutoComplete,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,actorsDataTable,Cell.FIELD_WIDTH,12)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return (ActorBusiness.CREATE_SCOPES.equals(getActionIdentifier()) ? "Affectation" : "Retrait")+" | "+scopeType.getName();
	}
	
	protected abstract Class<SCOPE> getScopeClass();
	protected abstract String getScopeTypeCode();
	
	protected abstract String getActionIdentifier();
}