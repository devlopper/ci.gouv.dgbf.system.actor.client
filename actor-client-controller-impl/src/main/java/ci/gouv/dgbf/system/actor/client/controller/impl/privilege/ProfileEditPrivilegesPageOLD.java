package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.array.ArrayHelper;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.JsfController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Tree;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;

import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ProfileEditPrivilegesPageOLD extends AbstractPageContainerManagedImpl implements Serializable {

	private Layout layout;
	private Profile profile;
	private Tree availableTree,selectedTree;
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Privilèges du profile "+profile.getName();
	}
	
	@Override
	protected void __listenPostConstruct__() {
		profile = WebController.getInstance().getRequestParameterEntity(Profile.class);
		super.__listenPostConstruct__();		

		Collection<ProfilePrivilege> profilePrivileges = EntityReader.getInstance().readMany(ProfilePrivilege.class, ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES
				,ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode()));
		Collection<Privilege> selectedPrivileges = CollectionHelper.isEmpty(profilePrivileges) ? null : profilePrivileges.stream().map(x -> x.getPrivilege()).collect(Collectors.toSet());	
		
		if(CollectionHelper.isNotEmpty(selectedPrivileges)) {							
			Collection<Privilege> parents = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_PARENTS_BY_CHILDREN_IDENTIFIERS
				,PrivilegeQuerier.PARAMETER_NAME_CHILDREN_IDENTIFIERS, selectedPrivileges.stream().map(x -> x.getIdentifier()).collect(Collectors.toList()));
			if(CollectionHelper.isNotEmpty(parents)) {						
				profilePrivileges.addAll(parents.stream()
						.filter(parent -> !selectedPrivileges.contains(parent))
						.map(x -> new ProfilePrivilege().setProfile(profile).setPrivilege(x)).collect(Collectors.toList()));
				selectedPrivileges.addAll(parents);
			}
		}		
		
		Collection<PrivilegeType> privilegeTypes = EntityReader.getInstance().readMany(PrivilegeType.class, PrivilegeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING);
		if(CollectionHelper.isNotEmpty(privilegeTypes)) {				
			//read all
			Collection<Privilege> availablePrivileges = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
			Privilege.processCollectChildren(availablePrivileges);
			/*
			availableTree = Tree.build(Tree.FIELD_VALUE,PrivilegeListPage.instantiateTreeNode(availablePrivileges,selectedPrivileges)
					,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Disponible (Sélectionnez les éléments à assigner)",Tree.FIELD_SELECTION_MODE,"checkbox"
					,Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE,Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE
					,Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<Privilege>() {
									
				@Override
				public Boolean isParent(Privilege data1, Privilege data2) {
					return data1 != null && data2 != null && StringHelper.isNotBlank(data1.getIdentifier()) && data1.getIdentifier().equals(data2.getParentIdentifier());
				}
			});
			*/
			availableTree = buildAvailableTree(Tree.FIELD_VALUE,PrivilegeTreePage.instantiateTreeNode(availablePrivileges,selectedPrivileges));
			/*
			selectedTree = Tree.build(Tree.FIELD_VALUE,ProfilePrivilegeListPage.instantiateTreeNode(profilePrivileges),Tree.ConfiguratorImpl.FIELD_TITLE_VALUE
					,"Accordés (Sélectionnez les éléments à retirer)"
					,Tree.FIELD_SELECTION_MODE,"checkbox",Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE,Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE
					,Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<ProfilePrivilege>() {
				
				@Override
				public Boolean isParent(ProfilePrivilege data1, ProfilePrivilege data2) {
					return data1 != null && data2 != null && data1.getPrivilege() != null && data2.getPrivilege() != null 
							&& StringHelper.isNotBlank(data1.getPrivilege().getIdentifier()) 
							&& data1.getPrivilege().getIdentifier().equals(data2.getPrivilege().getParentIdentifier());
				}
			},Tree.FIELD_TREE_NODE, org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode.build(
					org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode.FIELD_LISTENER
					,new org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode.Listener.AbstractImpl() {
						public String stringify(Object nodeData, org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode treeNode) {
							if(nodeData == null)
								return null;
							return((ProfilePrivilege)nodeData).getPrivilege().getName(); 
						}
					}
				));
			*/
			selectedTree = buildSelectedTree(Tree.FIELD_VALUE,ProfilePrivilegeListPage.instantiateTreeNode(profilePrivileges));
		}		
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,availableTree,Cell.FIELD_WIDTH,6)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,selectedTree,Cell.FIELD_WIDTH,6)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Enregistrer",CommandButton.FIELD_ICON,"fa fa-floppy-o"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
						,CommandButton.FIELD_LISTENER,new CommandButton.Listener.AbstractImpl() {
							@Override
							protected Object __runExecuteFunction__(AbstractAction action) {
								Arguments<ProfilePrivilege> arguments = new Arguments<ProfilePrivilege>();
								arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments().setActionIdentifier(ProfilePrivilegeBusiness.SAVE));
								if(ArrayHelper.isNotEmpty(availableTree.getSelection()))
									arguments.setCreatables(availableTree.getSelectionDatas(Privilege.class).stream().map(x -> new ProfilePrivilege().setProfile(profile).setPrivilege(x)).collect(Collectors.toList()));
								if(ArrayHelper.isNotEmpty(selectedTree.getSelection()))
									arguments.setDeletables(selectedTree.getSelectionDatas(ProfilePrivilege.class).stream()
											.filter(x -> x.getProfile() != null && StringHelper.isNotBlank(x.getIdentifier())).collect(Collectors.toList()));
								EntitySaver.getInstance().save(ProfilePrivilege.class, arguments);	
								JsfController.getInstance().redirect("profileEditPrivilegesView",Map.of(ParameterName.ENTITY_IDENTIFIER.getValue(),List.of(profile.getIdentifier())));
								return null;
							}
						},CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"),Cell.FIELD_WIDTH,12)
				));
	}
	
	public static Tree buildAvailableTree(Map<Object,Object> arguments) {
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Disponible (Sélectionnez les éléments à assigner)");
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.FIELD_SELECTION_MODE,"checkbox");
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<Privilege>() {		
			@Override
			public Boolean isParent(Privilege data1, Privilege data2) {
				return data1 != null && data2 != null && StringHelper.isNotBlank(data1.getIdentifier()) && data1.getIdentifier().equals(data2.getParentIdentifier());
			}
		});
		return Tree.build(arguments);
	}
	
	public static Tree buildAvailableTree(Object...objects) {
		return buildAvailableTree(MapHelper.instantiate(objects));
	}
	
	public static Tree buildSelectedTree(Map<Object,Object> arguments) {
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Accordés (Sélectionnez les éléments à retirer)");
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.FIELD_SELECTION_MODE,"checkbox");
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE);
		MapHelper.writeByKeyDoNotOverride(arguments, Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<ProfilePrivilege>() {			
			@Override
			public Boolean isParent(ProfilePrivilege data1, ProfilePrivilege data2) {
				return data1 != null && data2 != null && data1.getPrivilege() != null && data2.getPrivilege() != null 
						&& StringHelper.isNotBlank(data1.getPrivilege().getIdentifier()) 
						&& data1.getPrivilege().getIdentifier().equals(data2.getPrivilege().getParentIdentifier());
			}
		});
		MapHelper.writeByKeyDoNotOverride(arguments,Tree.FIELD_TREE_NODE, org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode.build(
				org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode.FIELD_LISTENER
				,new org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode.Listener.AbstractImpl() {
					public String stringify(Object nodeData, org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.TreeNode treeNode) {
						if(nodeData == null)
							return null;
						return((ProfilePrivilege)nodeData).getPrivilege().getName(); 
					}
				}
			));
		return Tree.build(arguments);
	}
	
	public static Tree buildSelectedTree(Object...objects) {
		return buildSelectedTree(MapHelper.instantiate(objects));
	}
}