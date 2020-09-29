package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.field.FieldHelper;
import org.cyk.utility.__kernel__.log.LogHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.persistence.query.filter.Filter;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.LazyDataModel;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Tree;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ci.gouv.dgbf.system.actor.client.controller.api.ProfilePrivilegeController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.client.controller.impl.AbstractActorListPrivilegesOrScopesPageOLD;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorListPrivilegesPageOLD extends AbstractActorListPrivilegesOrScopesPageOLD<ProfilePrivilege> implements Serializable {

	private Profile profile;
	private Collection<ProfilePrivilege> profilePrivileges;
	
	private Collection<PrivilegeType> privilegeTypes;
	private Collection<Privilege> availablePrivileges,selectedPrivileges;
	
	private Tree availableTree,selectedTree;
	private CommandButton saveCommandButton;
		
	@Override
	protected void addDataComponent(Collection<Map<?, ?>> cellsMaps) {
		if(actor != null) {
			profile = Helper.getProfileFromRequestParameterEntityAsParent(actor); 
			if(profile != null) {
				profilePrivileges =EntityReader.getInstance().readMany(ProfilePrivilege.class, ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES
						,ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode()));
				if(CollectionHelper.isNotEmpty(profilePrivileges)) {
					selectedPrivileges = profilePrivileges.stream().map(x -> x.getPrivilege()).collect(Collectors.toSet());					
					Collection<Privilege> parents = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_PARENTS_BY_CHILDREN_CODES
						,PrivilegeQuerier.PARAMETER_NAME_CHILDREN_CODES, profilePrivileges.stream().map(x -> x.getPrivilege().getCode()).collect(Collectors.toList()));
					if(CollectionHelper.isNotEmpty(parents)) {						
						profilePrivileges.addAll(parents.stream()
								.filter(parent -> !selectedPrivileges.contains(parent))
								.map(x -> new ProfilePrivilege().setProfile(profile).setPrivilege(x)).collect(Collectors.toList()));
						selectedPrivileges.addAll(parents);
					}
				}
			}
			
			privilegeTypes = EntityReader.getInstance().readMany(PrivilegeType.class, PrivilegeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING);
			if(CollectionHelper.isNotEmpty(privilegeTypes)) {				
				//read all
				availablePrivileges = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
				Privilege.processCollectChildren(availablePrivileges);
				
				availableTree = Tree.build(Tree.FIELD_VALUE,PrivilegeTreePage.instantiateTreeNode(availablePrivileges,selectedPrivileges),Tree.ConfiguratorImpl.FIELD_PROCESS_SELECTION_COMMAND_BUTTON_INSTANTIABLE,Boolean.TRUE
						,Tree.ConfiguratorImpl.FIELD_UPDATABLE_AFTER_SELECTION_PROCESSING,Boolean.TRUE,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Disponible"
						,Tree.FIELD_SELECTION_MODE,"checkbox",Tree.FIELD_FILTER_BY,"name",Tree.FIELD_FILTER_MATCH_MODE,"contains"
						,Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE,Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE
						,Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<Privilege>() {
										
					@Override
					public Map<Object, Object> getProcessSelectionCommandButtonArguments(Tree tree) {
						Map<Object, Object> arguments = super.getProcessSelectionCommandButtonArguments(tree);
						arguments.put(CommandButton.FIELD_VALUE, "Ajouter");
						return arguments;
					}
					
					@Override
					public void processSelection(Tree tree,Collection<Privilege> privileges) {
						LogHelper.logInfo("processing "+CollectionHelper.getSize(privileges)+" privilege(s)", getClass());
						if(CollectionHelper.isEmpty(privileges))
							return;
						Collection<ProfilePrivilege> profilePrivileges = privileges.stream().map(privilege -> new ProfilePrivilege().setProfile(profile).setPrivilege(privilege)).collect(Collectors.toList());
						LogHelper.logInfo(CollectionHelper.getSize(profilePrivileges)+" profile privilege(s) instantiated", getClass());
						__inject__(ProfilePrivilegeController.class).createMany(profilePrivileges);
						LogHelper.logInfo("profile privilege(s) created",getClass());
						//get profile privileges by privileges
						profilePrivileges = EntityReader.getInstance().readMany(ProfilePrivilege.class, ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PRIVILEGES_CODES
								,ProfilePrivilegeQuerier.PARAMETER_NAME_PRIVILEGES_CODES, FieldHelper.readBusinessIdentifiers(privileges));
						LogHelper.logInfo(CollectionHelper.getSize(profilePrivileges)+" profile privilege(s) read from "+CollectionHelper.getSize(privileges)+" privilege(s) codes",getClass());
						if(ActorListPrivilegesPageOLD.this.profilePrivileges == null)
							ActorListPrivilegesPageOLD.this.profilePrivileges = new ArrayList<>();
						ActorListPrivilegesPageOLD.this.profilePrivileges.addAll(profilePrivileges);
						LogHelper.logInfo(CollectionHelper.getSize(ActorListPrivilegesPageOLD.this.profilePrivileges)+" profile privilege(s) to show", getClass());
						if(selectedPrivileges == null)
							selectedPrivileges = new ArrayList<>();
						selectedPrivileges.addAll(privileges);
						LogHelper.logInfo("adding created profile privileges to selected tree with root node "+selectedTree.getValue(), getClass());
						profilePrivileges.forEach(profilePrivilege -> {
							System.out.println("PP : "+profilePrivilege);
							selectedTree.addNodeFromData(profilePrivilege);
						});
						LogHelper.logInfo("removing created profile privileges to available tree", getClass());
						for(TreeNode index : tree.getSelection()) {
							index.getParent().getChildren().remove(index);
						}
					}
					
					@Override
					public Boolean isParent(Privilege data1, Privilege data2) {
						return data1 != null && data2 != null && StringHelper.isNotBlank(data1.getIdentifier()) && data1.getIdentifier().equals(data2.getParentIdentifier());
					}
				});
				
				selectedTree = Tree.build(Tree.FIELD_VALUE,instantiateTreeNode(profilePrivileges),Tree.ConfiguratorImpl.FIELD_PROCESS_SELECTION_COMMAND_BUTTON_INSTANTIABLE,Boolean.TRUE
						,Tree.ConfiguratorImpl.FIELD_UPDATABLE_AFTER_SELECTION_PROCESSING,Boolean.TRUE,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Accordés"
						,Tree.FIELD_SELECTION_MODE,"checkbox",Tree.FIELD_FILTER_BY,"name",Tree.FIELD_FILTER_MATCH_MODE,"contains"
						,Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE,Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE
						,Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<ProfilePrivilege>() {
					public void computeNodes(Tree tree) {
						tree.setValue(instantiateTreeNode(profilePrivileges));
					}
					
					@Override
					public Map<Object, Object> getProcessSelectionCommandButtonArguments(Tree tree) {
						Map<Object, Object> arguments = super.getProcessSelectionCommandButtonArguments(tree);
						arguments.put(CommandButton.FIELD_VALUE, "Retirer");
						return arguments;
					}
					
					@Override
					public void processSelection(Tree tree,Collection<ProfilePrivilege> profilePrivileges) {
						System.out.println("ActorListPrivilegesPage.addDataComponent() : "+profilePrivileges);
						if(CollectionHelper.isEmpty(profilePrivileges))
							return;
						System.out.println("IDs : "+profilePrivileges.stream().map(x -> x.getIdentifier()).collect(Collectors.toList()));
						__inject__(ProfilePrivilegeController.class).deleteMany(profilePrivileges);
						Collection<Privilege> privileges = profilePrivileges.stream().map(x->x.getPrivilege()).collect(Collectors.toList());
						if(CollectionHelper.isNotEmpty(selectedPrivileges))							
							selectedPrivileges.removeAll(privileges);
						privileges.forEach(privilege -> {
							availableTree.addNodeFromData(privilege);
						});
						for(TreeNode index : tree.getSelection()) {
							//System.out.println("DATA : "+index.getData());
							//System.out.println("SELECTABLE ? : "+((Privilege)index.getData()).isSelectable(selectedPrivileges));
							//if(Boolean.TRUE.equals(((Privilege)index.getData()).isSelectable(selectedPrivileges)))
							//	continue;
							index.getParent().getChildren().remove(index);
						}
					}
					
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
		}
		
		//selectedTree.computeNodes();
		
		//availableTree.getProcessSelectionCommandButton().addUpdates(":form:"+selectedTree.getIdentifier());
		//selectedTree.getProcessSelectionCommandButton().addUpdates(":form:"+availableTree.getIdentifier());
		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,availableTree,Cell.FIELD_WIDTH,4));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,selectedTree,Cell.FIELD_WIDTH,4));	
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,dataTable,Cell.FIELD_WIDTH,4));
	}
	
	@Override
	protected DataTable instantiateDataTable() {
		return ProfilePrivilegeListPage.instantiateDataTable(List.of(ProfilePrivilege.FIELD_PRIVILEGE,ProfilePrivilege.FIELD_VISIBLE),new DataTableListenerImpl(),new LazyDataModelListenerImpl());
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation des privilèges";
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListPrivilegesView";
	}
	
	@Override
	protected String getCreateOutcome() {
		return "actorCreatePrivilegesView";
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
	
	/**/
	
	public class DataTableListenerImpl extends ProfilePrivilegeListPage.DataTableListenerImpl implements Serializable{
		
	}
	
	public class LazyDataModelListenerImpl extends ProfilePrivilegeListPage.LazyDataModelListenerImpl implements Serializable {
		
		@Override
		public Arguments<ProfilePrivilege> instantiateArguments(LazyDataModel<ProfilePrivilege> lazyDataModel) {
			Arguments<ProfilePrivilege> arguments = super.instantiateArguments(lazyDataModel);
			arguments.getRepresentationArguments().getQueryExecutorArguments().setQueryIdentifier(ProfilePrivilegeQuerier.QUERY_IDENTIFIER_READ_BY_PROFILES_CODES);
			return arguments;
		}
		
		@Override
		public Filter.Dto instantiateFilter(LazyDataModel<ProfilePrivilege> lazyDataModel) {
			return new Filter.Dto().addField(ProfilePrivilegeQuerier.PARAMETER_NAME_PROFILES_CODES, List.of(profile.getCode()));
		}
	}
}