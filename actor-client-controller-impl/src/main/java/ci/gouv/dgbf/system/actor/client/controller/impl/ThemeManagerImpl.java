package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;
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

import ci.gouv.dgbf.system.actor.client.controller.impl.identification.IdentificationPage;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class ThemeManagerImpl extends ThemeManager.AbstractImpl implements Serializable {

	private TabMenu identificationTabMenu;
	
	@Override
	public TabMenu getLeftTabMenu(AbstractPageContainerManagedImpl page) {
		if(page instanceof IdentificationPage) {
			if(identificationTabMenu == null)
				identificationTabMenu = instantiateIdentificationTabMenu();
			return identificationTabMenu;
		}
		return super.getLeftTabMenu(page);
	}
	
	public static TabMenu instantiateIdentificationTabMenu() {
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_TABS_NAMES,List.of("Identification")
				,TabMenu.FIELD_LISTENER,new TabMenu.Listener.AbstractImpl() {
				@Override
				public Map<Object, Object> getTabArguments(String name) {
					Map<Object, Object> arguments = super.getTabArguments(name);
					if("Identification".equals(name)) {
						arguments.put(Tab.FIELD_ICON, "fa fa-user");
						MenuModel model = new DefaultMenuModel();
						model.addElement(new DefaultMenuItem("Demander un compte"));
						//model.addElement(new DefaultMenuItem("Consulter saisie"));
						//model.addElement(new DefaultMenuItem("Renvoyer jeton d'accès"));
						arguments.put(Tab.FIELD_MENU, Menu.build(Menu.FIELD_MODEL,model));
					}
					return arguments;
				}
			});
	}
}