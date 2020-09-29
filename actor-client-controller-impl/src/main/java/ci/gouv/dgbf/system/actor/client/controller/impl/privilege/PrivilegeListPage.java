package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;
import java.util.Collection;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.collection.DataTable;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.menu.TabMenu;
import org.primefaces.model.TreeNode;

import ci.gouv.dgbf.system.actor.client.controller.entities.Privilege;
import ci.gouv.dgbf.system.actor.client.controller.entities.PrivilegeType;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class PrivilegeListPage extends AbstractPageContainerManagedImpl implements Serializable {

	private TabMenu tabMenu;
	private DataTable dataTable;
	private Layout layout;
	
	private Collection<PrivilegeType> privilegeTypes;
	private Collection<Privilege> privileges;
	private TreeNode root;
	
	@Override
	protected void __listenPostConstruct__() {
		super.__listenPostConstruct__();				
		
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Privilèges";
	}
}