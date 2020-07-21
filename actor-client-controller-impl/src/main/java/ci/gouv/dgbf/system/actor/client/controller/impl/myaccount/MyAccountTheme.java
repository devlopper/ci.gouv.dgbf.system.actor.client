package ci.gouv.dgbf.system.actor.client.controller.impl.myaccount;

import java.util.List;
import java.util.Map;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.ThemeManager;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Menu;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Tab;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

public interface MyAccountTheme {

	String APPLICATION_NAME = "Mon compte";
	String APPLICATION_OUTCOME = "myAccountIndexView";
	
	String MENU_ITEM_MY_ACCOUNT = "Mon compte";
	
	static TabMenu instantiateTabMenu(AbstractPageContainerManagedImpl page) {
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_TABS_NAMES,List.of(MENU_ITEM_MY_ACCOUNT)
				,TabMenu.FIELD_LISTENER,new TabMenu.Listener.AbstractImpl() {
				@Override
				public Map<Object, Object> getTabArguments(String name) {
					Map<Object, Object> arguments = super.getTabArguments(name);
					if(MENU_ITEM_MY_ACCOUNT.equals(name)) {
						arguments.put(Tab.FIELD_ICON, "fa fa-user");						
						arguments.put(Tab.FIELD_MENU, Menu.build(Menu.FIELD_MODEL,ThemeManager.getInstance().getTopMenuModel(page)));
					}
					return arguments;
				}
			});
	}	
}