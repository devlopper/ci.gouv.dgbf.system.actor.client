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
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.controller.EntitySaver;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Tree;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;

import ci.gouv.dgbf.system.actor.client.controller.entities.Actor;
import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import ci.gouv.dgbf.system.actor.client.controller.entities.Profile;
import ci.gouv.dgbf.system.actor.client.controller.entities.ProfilePrivilege;
import ci.gouv.dgbf.system.actor.client.controller.impl.AbstractActorEditPrivilegesOrScopesPage;
import ci.gouv.dgbf.system.actor.client.controller.impl.Helper;
import ci.gouv.dgbf.system.actor.server.business.api.ProfilePrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeTypeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ProfilePrivilegeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorEditPrivilegesPage extends AbstractActorEditPrivilegesOrScopesPage<ProfilePrivilege> implements Serializable {

	private Profile profile;	
	private Tree availableTree,selectedTree;
		
	@Override
	protected void addInputs(Collection<Map<?, ?>> cellsMaps) {
		profile = Helper.getProfileFromRequestParameterEntityAsParent(actor);
		if(profile == null)
			return;
		
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
			/*
			if(CollectionHelper.isNotEmpty(parents)) {
				selectedPrivileges.addAll(parents);
				profilePrivileges.addAll(parents.stream().map(x -> new ProfilePrivilege().setPrivilege(x)).collect(Collectors.toList()));
			}
			*/			
		}		
		
		Collection<PrivilegeType> privilegeTypes = EntityReader.getInstance().readMany(PrivilegeType.class, PrivilegeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING);
		if(CollectionHelper.isNotEmpty(privilegeTypes)) {				
			//read all
			Collection<Privilege> availablePrivileges = EntityReader.getInstance().readMany(Privilege.class, PrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING);
			Privilege.processCollectChildren(availablePrivileges);
			/*
			availableTree = Tree.build(Tree.FIELD_VALUE,PrivilegeListPage.instantiateTreeNode(availablePrivileges,selectedPrivileges)
					,Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Disponible",Tree.FIELD_SELECTION_MODE,"checkbox"
					,Tree.FIELD_PROPAGATE_SELECTION_UP,Boolean.TRUE,Tree.FIELD_PROPAGATE_SELECTION_DOWN,Boolean.TRUE
					,Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<Privilege>() {
									
				@Override
				public Boolean isParent(Privilege data1, Privilege data2) {
					return data1 != null && data2 != null && StringHelper.isNotBlank(data1.getIdentifier()) && data1.getIdentifier().equals(data2.getParentIdentifier());
				}
			});
			*/
			availableTree = ProfileEditPrivilegesPage.buildAvailableTree(Tree.FIELD_VALUE,PrivilegeTreePage.instantiateTreeNode(availablePrivileges,selectedPrivileges));
			/*
			selectedTree = Tree.build(Tree.FIELD_VALUE,ProfilePrivilegeListPage.instantiateTreeNode(profilePrivileges),Tree.ConfiguratorImpl.FIELD_TITLE_VALUE,"Accordés"
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
			selectedTree = ProfileEditPrivilegesPage.buildSelectedTree(Tree.FIELD_VALUE,ProfilePrivilegeListPage.instantiateTreeNode(profilePrivileges));
		}		
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,availableTree,Cell.FIELD_WIDTH,6));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,selectedTree,Cell.FIELD_WIDTH,6));	
	}
		
	@Override
	protected String __getWindowTitleValue__() {
		return formatWindowTitle(actor);
	}
	
	@Override
	protected String getEditOutcome() {
		return "actorEditPrivilegesView";
	}
	
	@Override
	protected String getListOutcome() {
		return "actorListPrivilegesView";
	}
	
	@Override
	protected void save() {
		Arguments<ProfilePrivilege> arguments = new Arguments<ProfilePrivilege>();
		arguments.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments().setActionIdentifier(ProfilePrivilegeBusiness.SAVE));
		if(ArrayHelper.isNotEmpty(availableTree.getSelection()))
			arguments.setCreatables(availableTree.getSelectionDatas(Privilege.class).stream().map(x -> new ProfilePrivilege().setProfile(profile).setPrivilege(x)).collect(Collectors.toList()));
		if(ArrayHelper.isNotEmpty(selectedTree.getSelection()))
			arguments.setDeletables(selectedTree.getSelectionDatas(ProfilePrivilege.class).stream()
					.filter(x -> x.getProfile() != null && StringHelper.isNotBlank(x.getIdentifier())).collect(Collectors.toList()));
		EntitySaver.getInstance().save(ProfilePrivilege.class, arguments);
	}
	
	/**/
	
	public static String formatWindowTitle(Actor actor,String by) {
		if(actor == null)
			return WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES;
		if(StringHelper.isBlank(by))
			return String.format(WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_ACCOUNT,actor.getCode(),actor.getNames());
		return String.format(WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY_ACCOUNT, by,actor.getCode(),actor.getNames());
	}
	
	public static String formatWindowTitle(Actor actor) {
		return formatWindowTitle(actor, null);
	}
	
	private static final String WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES = "Assignation de privilèges";
	private static final String WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY = WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES+" par %s";
	private static final String WINDOW_TITLE_FORMAT_ACCOUNT = " | Compte utilisateur %s : %s";
	private static final String WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_ACCOUNT = WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES+WINDOW_TITLE_FORMAT_ACCOUNT;
	private static final String WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY_ACCOUNT = WINDOW_TITLE_FORMAT_ASSIGN_PRIVILEGES_BY+WINDOW_TITLE_FORMAT_ACCOUNT;
}