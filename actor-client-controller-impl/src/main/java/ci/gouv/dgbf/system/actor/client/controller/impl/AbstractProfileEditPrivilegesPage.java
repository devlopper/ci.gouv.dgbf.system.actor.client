package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.__kernel__.user.interface_.message.RenderType;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.ajax.Ajax;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.AutoComplete;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.panel.Dialog;
import org.primefaces.model.DualListModel;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AbstractProfileEditPrivilegesPage extends AbstractPageContainerManagedImpl implements Serializable {

	protected Layout layout;
	protected Profile profile;
	protected Actor actor;
	protected Collection<ProfilePrivilege> profilePrivileges;
	protected Collection<Privilege> availablePrivileges;
	protected Collection<Privilege> selectedPrivileges,initialPrivileges;
	protected DualListModel<Privilege> privilegesDualListModel;
	protected AutoComplete actorAutoComplete;
	protected CommandButton saveCommandButton,showActorSearchDialogCommandButton;
	protected Dialog actorSearchDialog;
	
	@Override
	protected void __listenPostConstruct__() {
		profile = WebController.getInstance().getRequestParameterEntity(Profile.class);
		if(profile == null) {
			actor = WebController.getInstance().getRequestParameterEntityAsParent(Actor.class);
			if(actor != null) {
				profile = CollectionHelper.getFirst(EntityReader.getInstance().readMany(Profile.class, new Arguments<Profile>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
								new QueryExecutorArguments.Dto().setQueryIdentifier(ProfileQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES_ORDER_BY_CODE_ASCENDING)
								.addFilterField(ProfileQuerier.PARAMETER_NAME_ACTORS_CODES, List.of(actor.getCode()))))));
			}
			if(profile != null) {
				profilePrivileges = EntityReader.getInstance().readMany(ProfilePrivilege.class, new Arguments<ProfilePrivilege>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
								new QueryExecutorArguments.Dto().setQueryIdentifier(ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES_ORDER_BY_PRIVILEGE_CODE_ASCENDING)
								.addFilterField(ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode())))));
				if(CollectionHelper.isNotEmpty(profilePrivileges))
					selectedPrivileges = profilePrivileges.stream().map(ProfilePrivilege::getPrivilege).collect(Collectors.toList());
				availablePrivileges = EntityReader.getInstance().readMany(Privilege.class, new Arguments<Privilege>()
						.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setQueryExecutorArguments(
								new QueryExecutorArguments.Dto().setQueryIdentifier(PrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES_NOT_ASSOCIATED_ORDER_BY_CODE_ASCENDING)
								.addFilterField(PrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode())))));
			}		
		}
		super.__listenPostConstruct__();		
		if(availablePrivileges == null)
			availablePrivileges = new ArrayList<>();
		if(selectedPrivileges == null)
			selectedPrivileges = new ArrayList<>();
		privilegesDualListModel = new DualListModel<Privilege>((List<Privilege>)availablePrivileges, (List<Privilege>)selectedPrivileges);
		
		if(CollectionHelper.isNotEmpty(selectedPrivileges))
			initialPrivileges = new ArrayList<Privilege>(selectedPrivileges);
		
		actorSearchDialog = Dialog.build(Dialog.FIELD_HEADER,"Recherche d'un compte utilisateur",Dialog.FIELD_MODAL,Boolean.TRUE
				,Dialog.ConfiguratorImpl.FIELD_COMMAND_BUTTONS_BUILDABLE,Boolean.FALSE,Dialog.FIELD_STYLE_CLASS,"cyk-min-width-90-percent");
		//actorSearchDialog.addStyleClasses("cyk-min-width-90-percent");
		showActorSearchDialogCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Rechercher",CommandButton.FIELD_ICON,"fa fa-search",CommandButton.FIELD_PROCESS,"@this"
				,CommandButton.FIELD___DIALOG__,actorSearchDialog,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.SHOW_DIALOG);
		
		actorAutoComplete = AutoComplete.build(AutoComplete.FIELD_ENTITY_CLASS,Actor.class,AutoComplete.FIELD_LISTENER,new AutoComplete.Listener.AbstractImpl<Actor>() {
			@Override
			public Filter.Dto instantiateFilter(AutoComplete autoComplete) {
				return new Filter.Dto().addField(ActorQuerier.PARAMETER_NAME_STRING, autoComplete.get__queryString__());
			}
			@Override
			public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
				
			}
		},AutoComplete.FIELD_PLACEHOLDER,"rechercher par le nom d'utilisateur");
		
		actorAutoComplete.enableAjaxItemSelect();
		actorAutoComplete.getAjaxes().get("itemSelect").setListener(new Ajax.Listener.AbstractImpl() {
			@Override
			protected void run(AbstractAction action) {
				Actor actor = (Actor) FieldHelper.read(action.get__argument__(), "source.value");
				if(actor != null)
					JsfController.getInstance().redirect("actorEditPrivilegesView",Map.of(ParameterName.stringify(Actor.class),List.of(actor.getIdentifier())));
			}			
		});
		actorAutoComplete.getAjaxes().get("itemSelect").setDisabled(Boolean.FALSE);
		actorAutoComplete.setReaderUsable(Boolean.TRUE);
		actorAutoComplete.setReadQueryIdentifier(ActorQuerier.QUERY_IDENTIFIER_READ_BY_STRING);
		actorAutoComplete.setCountQueryIdentifier(ActorQuerier.QUERY_NAME_COUNT_BY_STRING);
		
		saveCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o",CommandButton.FIELD_STYLE,"float:right;"
				,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.ConfiguratorImpl.FIELD_RUNNER_ARGUMENTS_SUCCESS_MESSAGE_ARGUMENTS_RENDER_TYPES,List.of(RenderType.GROWL));
		saveCommandButton.setListener(new CommandButton.Listener.AbstractImpl() {
			@Override
			public void run(AbstractAction action) {				
				Arguments<ProfilePrivilege> arguments = new Arguments<ProfilePrivilege>();
				arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ProfilePrivilegeBusiness.SAVE));
				if(CollectionHelper.isNotEmpty(privilegesDualListModel.getTarget())) {
					//get creatables
					Collection<Privilege> privileges = privilegesDualListModel.getTarget().stream().filter(selected -> initialPrivileges == null || !initialPrivileges.contains(selected)).collect(Collectors.toList());
					if(CollectionHelper.isNotEmpty(privileges))
						arguments.addCreatablesOrUpdatables(privileges.stream().map(privilege -> new ProfilePrivilege().setProfile(profile).setPrivilege(privilege))
								.collect(Collectors.toList()));
				}
				if(CollectionHelper.isNotEmpty(initialPrivileges))
					//get deletables
					arguments.setDeletables(profilePrivileges.stream().filter(profilePrivilege -> !privilegesDualListModel.getTarget().contains(profilePrivilege.getPrivilege())).collect(Collectors.toList()));

				if(CollectionHelper.isNotEmpty(arguments.getCreatables()) || CollectionHelper.isNotEmpty(arguments.getDeletables()))
					EntitySaver.getInstance().save(ProfilePrivilege.class, arguments);										
			}
		});
		Collection<Map<?,?>> cellsMaps = new ArrayList<Map<?,?>>(); 
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Compte utilisateur"),Cell.FIELD_WIDTH,2));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,actorAutoComplete,Cell.FIELD_WIDTH,10));	
		if(actor == null) {
			
		}else {
			actorAutoComplete.setValue(actor);
			//cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(actor.getCode()+" - "+actor.getNames()),Cell.FIELD_WIDTH,8));		
			//cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,showActorSearchDialogCommandButton,Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Privilèges"),Cell.FIELD_WIDTH,2));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"picklist",Cell.FIELD_WIDTH,10));
			cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12));
		}
		/*
		CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Compte utilisateur"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_CONTROL,actorAutoComplete,Cell.FIELD_WIDTH,10)	
				,MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue("Privilèges"),Cell.FIELD_WIDTH,2),MapHelper.instantiate(Cell.FIELD_IDENTIFIER,"picklist",Cell.FIELD_WIDTH,10)	
				,MapHelper.instantiate(Cell.FIELD_CONTROL,saveCommandButton,Cell.FIELD_WIDTH,12)
			);
		*/
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
	}
}