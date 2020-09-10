package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Tree;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.input.SelectManyCheckbox;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.ActorProfile;
import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.server.business.api.ActorProfileBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ActorProfileQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfileQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorEditProfilesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Actor actor;
	private SelectManyCheckbox profilesSelectManyCheckbox;
	private Collection<ActorProfile> actorProfiles;
	private Collection<Profile> profiles,initialSelectedProfiles;
	private Tree tree;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Profile(s) de "+actor.getCode()+" - "+actor.getNames();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		actor = WebController.getInstance().getRequestParameterEntity(Actor.class);
		super.__listenPostConstruct__();		
		profiles = EntityReader.getInstance().readMany(Profile.class, ProfileQuerier.QUERY_IDENTIFIER_READ);
		if(CollectionHelper.isNotEmpty(profiles)) {
			actorProfiles = EntityReader.getInstance().readMany(ActorProfile.class, ActorProfileQuerier.QUERY_IDENTIFIER_READ_BY_ACTORS_CODES
					,ActorProfileQuerier.PARAMETER_NAME_ACTORS_CODES,List.of(actor.getCode()));
			if(CollectionHelper.isNotEmpty(actorProfiles))
				initialSelectedProfiles = actorProfiles.stream().map(x -> x.getProfile()).collect(Collectors.toList());
		}
		
		profilesSelectManyCheckbox = SelectManyCheckbox.build(SelectManyCheckbox.FIELD_CHOICES,profiles,SelectManyCheckbox.FIELD_VALUE,initialSelectedProfiles);
		
		Collection<Privilege> privileges = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_VISIBLE_BY_ACTOR_CODE
				,PrivilegeQuerier.PARAMETER_NAME_ACTOR_CODE, actor.getCode());
		
		tree = PrivilegeListPage.buildTree(Tree.FIELD_VALUE,PrivilegeListPage.instantiateTreeNode(privileges,null));
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,profilesSelectManyCheckbox,Cell.FIELD_WIDTH,6)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,tree,Cell.FIELD_WIDTH,6)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ActorProfile> arguments = new Arguments<ActorProfile>();
								arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ActorProfileBusiness.SAVE));
								if(CollectionHelper.isNotEmpty(profiles)) {
									Collection<Profile> selectedProfiles = CollectionHelper.cast(Profile.class, profilesSelectManyCheckbox.getValue());
									arguments.addCreatablesOrUpdatables(profiles.stream().filter(profile -> CollectionHelper
											.contains(selectedProfiles, profile) && !CollectionHelper.contains(initialSelectedProfiles, profile))
											.map(profile -> new ActorProfile().setActor(actor).setProfile(profile))
										.collect(Collectors.toList()));
									if(CollectionHelper.isNotEmpty(actorProfiles))
										arguments.addDeletables(actorProfiles.stream()
												.filter(actorProfile -> !selectedProfiles.contains(actorProfile.getProfile()))
												.collect(Collectors.toList()));
									if(CollectionHelper.isNotEmpty(arguments.getDeletables()))
										arguments.getDeletables().forEach(x -> x.setActor(actor));
								}
								if(CollectionHelper.isNotEmpty(arguments.getCreatables()) || CollectionHelper.isNotEmpty(arguments.getDeletables())) {
									EntitySaver.getInstance().save(ActorProfile.class, arguments);
									//JsfController.getInstance().redirect("profileListView");
								}
								return null;
							}
						},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12)
				));
	}
}