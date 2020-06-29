package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.controller.Arguments;
import org.cyk.utility.__kernel__.controller.EntityReader;
import org.cyk.utility.__kernel__.persistence.query.QueryExecutorArguments;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeQuerier;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.PrivilegeTypeQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PrivilegeListPage extends AbstractPageContainerManagedImpl implements Serializable {

	private Collection<PrivilegeType> privilegeTypes;
	private Collection<Privilege> privileges;
	private TreeNode root;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();				
		privilegeTypes = EntityReader.getInstance().readMany(PrivilegeType.class, new Arguments<PrivilegeType>()
				.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
						.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(PrivilegeTypeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_ORDER_NUMBER_ASCENDING))));
		if(CollectionHelper.isNotEmpty(privilegeTypes)) {
			//read all
			privileges = EntityReader.getInstance().readMany(Privilege.class, new Arguments<Privilege>()
					.setRepresentationArguments(new org.cyk.utility.__kernel__.representation.Arguments()
							.setQueryExecutorArguments(new QueryExecutorArguments.Dto().setQueryIdentifier(PrivilegeQuerier.QUERY_IDENTIFIER_READ_ORDER_BY_CODE_ASCENDING))));			
			//build tree
			if(CollectionHelper.isNotEmpty(privileges))
				root = instantiateTreeNode(privileges,null);
		}		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Liste des privilèges";
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
}