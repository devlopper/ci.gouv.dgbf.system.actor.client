package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.util.List;
import java.util.Map;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.ThemeManager;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Menu;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Tab;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

public interface MyAccountTheme {

	String APPLICATION_NAME = "Mon compte";
	String APPLICATION_OUTCOME = "myAccountIndexView";
	
	String MENU_ITEM_MY_ACCOUNT = "Mon compte";
	String MENU_ITEM_REQUESTS = "Demandes";
	
	static TabMenu instantiateTabMenu(AbstractPageContainerManagedImpl page) {
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_TABS_NAMES,List.of(MENU_ITEM_MY_ACCOUNT,MENU_ITEM_REQUESTS)
				,TabMenu.FIELD_LISTENER,new TabMenu.Listener.AbstractImpl() {
				@Override
				public Map<Object, Object> getTabArguments(String name) {
					Map<Object, Object> arguments = super.getTabArguments(name);
					if(MENU_ITEM_MY_ACCOUNT.equals(name)) {
						arguments.put(Tab.FIELD_ICON, "fa fa-user");						
						MenuModel model = ThemeManager.getInstance().getTopMenuModel(page);
						
						/*
						DefaultMenuItem item = new DefaultMenuItem("Profile");
						item.setOutcome("myAccountProfileView");
						model.addElement(item);
						*/
						
						arguments.put(Tab.FIELD_MENU, Menu.build(Menu.FIELD_MODEL,model));
					}else if(MENU_ITEM_REQUESTS.equals(name)) {
						arguments.put(Tab.FIELD_ICON, "fa fa-file");
						MenuModel model = new DefaultMenuModel();
						
						DefaultMenuItem item = new DefaultMenuItem("Demandes");
						item.setOutcome("myAccountRequestsView");
						model.addElement(item);
						
						arguments.put(Tab.FIELD_MENU, Menu.build(Menu.FIELD_MODEL,model));
					}
					return arguments;
				}
			});
	}	
}