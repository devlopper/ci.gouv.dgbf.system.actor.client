package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.__kernel__.user.interface_.UserInterfaceAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.AbstractAction;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.Tree;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.command.CommandButton;
import org.cyk.utility.controller.Arguments;
import org.cyk.utility.controller.EntityReader;
import org.cyk.utility.controller.EntitySaver;
import org.cyk.utility.persistence.query.QueryExecutorArguments;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import ci.gouv.dgbf.system.actor.server.business.api.PrivilegeBusiness;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PrivilegeTreePage extends AbstractPageContainerManagedImpl implements Serializable {

	private Collection<PrivilegeType> privilegeTypes;
	private Collection<Privilege> privileges;
	private TreeNode root;
	private CommandButton refreshCommandButton;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();				
		privilegeTypes = EntityReader.getInstance().readMany(PrivilegeType.class, new Arguments<PrivilegeType>()
				.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(PrivilegeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING))));
		if(CollectionHelper.isNotEmpty(privilegeTypes)) {
			//read all
			privileges = EntityReader.getInstance().readMany(Privilege.class, new Arguments<Privilege>()
					.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
							.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(PrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));			
			//build tree
			if(CollectionHelper.isNotEmpty(privileges))
				root = instantiateTreeNode(privileges,null);
		}
		
		refreshCommandButton = CommandButton.build(CommandButton.FIELD_VALUE,"Rafraichir",CommandButton.FIELD_USER_INTERFACE_ACTION,UserInterfaceAction.EXECUTE_FUNCTION
				,CommandButton.FIELD_LISTENER,new AbstractAction.Listener.AbstractImpl() {
			@Override
			protected Object __runExecuteFunction__(AbstractAction action) {
				Privilege privilege = new Privilege();
				Arguments<Privilege> arguments = new Arguments<Privilege>().addCreatablesOrUpdatables(privilege);
				arguments.setRepresentationArguments(new org.cyk.utility.representation.Arguments()
						.setActionIdentifier(PrivilegeBusiness.REFRESH));
				EntitySaver.getInstance().save(Privilege.class, arguments);
				return null;
			}
		});
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Arborescence des privilèges";
	}
	
	/**/
	
	public static TreeNode instantiateTreeNode(Collection<Privilege> privileges,Collection<Privilege> selected) {
		if(CollectionHelper.isEmpty(privileges))
			return null;
		TreeNode root = new DefaultTreeNode();		
		//find roots
		Collection<Privilege> roots = privileges.stream().filter(privilege -> StringHelper.isBlank(privilege.getParentIdentifier())).collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(roots)) {
			roots.forEach(privilege -> {
				if(Boolean.TRUE.equals(privilege.isSelectable(selected))) {
					TreeNode node = new DefaultTreeNode(privilege, root);
					instantiateChildren(node, privileges,selected);
				}
			});
		}
		return root;
	}
	
	private static void instantiateChildren(TreeNode root,Collection<Privilege> privileges,Collection<Privilege> selected) {
		Collection<Privilege> children = privileges.stream().filter(privilege -> ((Privilege)root.getData()).getIdentifier().equals(privilege.getParentIdentifier()))
				.collect(Collectors.toList());
		if(CollectionHelper.isNotEmpty(children)) {
			children.forEach(child -> {
				if(Boolean.TRUE.equals(child.isSelectable(selected))) {
					TreeNode node = new DefaultTreeNode(child, root);
					instantiateChildren(node, privileges,selected);	
				}			
			});
		}
	}
	
	public static Tree buildTree(Map<Object,Object> arguments) {
		MapHelper.writeByKeyDoNotOverride(arguments, Tree.FIELD_LISTENER,new Tree.Listener.AbstractImpl<Privilege>() {		
			@Override
			public Boolean isParent(Privilege data1, Privilege data2) {
				return data1 != null && data2 != null && StringHelper.isNotBlank(data1.getIdentifier()) && data1.getIdentifier().equals(data2.getParentIdentifier());
			}
		});
		return Tree.build(arguments);
	}
	
	public static Tree buildTree(Object...objects) {
		return buildTree(MapHelper.instantiate(objects));
	}
}