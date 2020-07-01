package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Tree;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ProfileListPrivilegesPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Profile profile;
	private Tree availableTree,selectedTree;
	private Layout layout;
	
	@Override
	protected void __listenPostConstruct__() {
		profile = WebController.getInstance().getRequestParameterEntity(Profile.class);
		super.__listenPostConstruct__();		
		
		Collection<ProfilePrivilege> profilePrivileges = EntityReader.getInstance().readMany(ProfilePrivilege.class, ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES
				,ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode()));
		
		final Collection<Privilege> selectedPrivileges = CollectionHelper.isEmpty(profilePrivileges) ? null : profilePrivileges.stream().map(x -> x.getPrivilege()).collect(Collectors.toSet());
		if(CollectionHelper.isNotEmpty(selectedPrivileges)) {
			Collection<Privilege> parents = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_PARENTS_BY_CHILDREN_IDENTIFIERS
				,PrivilegeQuerier.PARAMETER_NAME_CHILDREN_IDENTIFIERS, profilePrivileges.stream().map(x -> x.getPrivilege().getIdentifier()).collect(Collectors.toList()));
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
			
			availableTree = Tree.build(Tree.FIELD_VALUE,PrivilegeListPage.instantiateTreeNode(availablePrivileges,selectedPrivileges)
					,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Disponible"
					,Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<Privilege>() {
									
				@Override
				public Boolean isParent(Privilege data1, Privilege data2) {
					return data1 != null && data2 != null && StringHelper.isNotBlank(data1.getIdentifier()) && data1.getIdentifier().equals(data2.getParentIdentifier());
				}
			});
			
			selectedTree = Tree.build(Tree.FIELD_VALUE,instantiateTreeNode(profilePrivileges),Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Accordés"
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
		}		

		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.UI_G,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,CollectionHelper.listOf(
				MapHelper.instantiate(Cell.FIELD_CONTROL,availableTree,Cell.FIELD_WIDTH,6)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,selectedTree,Cell.FIELD_WIDTH,6)
				,MapHelper.instantiate(Cell.FIELD_CONTROL,CommandButton.build(CommandButton.FIELD_VALUE,"Modifier",CommandButton.FIELD_ICON,"fa fa-edit"
						,CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.NAVIGATE_TO_VIEW
						,CommandButton.FIELD___OUTCOME__,"profileEditPrivilegesView",CommandButton.FIELD_STYLE_CLASS,"cyk-float-right"
						,CommandButton.FIELD_LISTENER, new AbstractAction.Listener.AbstractImpl() {
							
							@Override
							protected void runNavigateToView(AbstractAction action) {
								action.set__argument__(profile);
								super.runNavigateToView(action);
							}
						}),Cell.FIELD_WIDTH,12)
				));
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des privilèges du profile "+profile.getName();
	}
	
	/**/
	
	public static TreeNode instantiateTreeNode(Collection<ProfilePrivilege> profilePrivileges) {
		if(CollectionHelper.isEmpty(profilePrivileges))
			return null;
		TreeNode root = new DefaultTreeNode();		
		//find roots
		Collection<ProfilePrivilege> roots = profilePrivileges.stream().filter(profilePrivilege -> StringHelper.isBlank(profilePrivilege.getPrivilege().getParentIdentifier())).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(roots)) {
			roots.forEach(profilePrivilege -> {
				TreeNode node = new DefaultTreeNode(profilePrivilege, root);
				instantiateChildren(node, profilePrivileges);
			});
		}
		return root;
	}
	
	private static void instantiateChildren(TreeNode root,Collection<ProfilePrivilege> profilePrivileges) {
		Collection<ProfilePrivilege> children = profilePrivileges.stream().filter(profilePrivilege -> ((ProfilePrivilege)root.getData()).getPrivilege().getIdentifier().equals(profilePrivilege.getPrivilege().getParentIdentifier()))
				.collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(children)) {
			children.forEach(child -> {
				TreeNode node = new DefaultTreeNode(child, root);
				instantiateChildren(node, profilePrivileges);
			});
		}
	}
}