package ci.gouv.dgbf.system.actor.client.controller.impl.mea;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.dgbf.DesktopDefault;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Tab;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.TabMenu;
import org.cyk.utility.__kernel__.collection.CollectionHelper;
import org.cyk.utility.__kernel__.properties.Properties;
import org.cyk.utility.client.controller.component.menu.Menu;
import org.cyk.utility.client.controller.component.tab.Tabs;

public interface MeaTheme {

	String APPLICATION_NAME = "MISE EN EXÉCUTION DES ACTES BUDGÉTAIRES";
	String APPLICATION_OUTCOME = "meaIndexView";
	
	String MENU_ITEM_IDENTIFICATION = "MEA";
	
	static TabMenu instantiateTabMenu() {
		Tabs tabs = DesktopDefault.buildMenuTabsByIdentifier("APP", MeaTheme.class);
		System.out.println("MeaTheme.instantiateTabMenu() ::: "+tabs);
		if(tabs == null || CollectionHelper.isEmpty(tabs.get()))
			return null;
		Collection<String> tabsNames = tabs.get().stream().map(x -> x.getProperty(Properties.NAME).toString()).collect(Collectors.toList());
		System.out.println("MeaTheme.instantiateTabMenu() ::: "+tabsNames);
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_TABS_NAMES,tabsNames
				,TabMenu.FIELD_LISTENER,new TabMenu.Listener.AbstractImpl() {
				@Override
				public Map<Object, Object> getTabArguments(String name) {
					Map<Object, Object> arguments = super.getTabArguments(name);
					tabsNames.forEach(x -> {
						org.cyk.utility.client.controller.component.tab.Tab tab = (org.cyk.utility.client.controller.component.tab.Tab) CollectionHelper
								.getFirst(tabs.get().stream().filter(a -> a.getProperty(Properties.NAME).equals(name)).collect(Collectors.toList()));
						Menu menu = (Menu) tab.getProperty(Properties.MENU);
						arguments.put(Tab.FIELD_ICON, tab.getProperty(Properties.ICON));
						arguments.put(Tab.FIELD_MENU, org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Menu
								.build(org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Menu.FIELD_MODEL,menu.getTargetModel().getValue()));
					});
					return arguments;
				}
			});
	}	
}