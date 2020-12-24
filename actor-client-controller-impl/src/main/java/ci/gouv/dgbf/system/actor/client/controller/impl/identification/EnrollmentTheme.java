package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.util.List;
import java.util.Map;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Menu;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.Tab;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.TabMenu;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

public interface EnrollmentTheme {

	String APPLICATION_NAME = "Identification";
	String APPLICATION_OUTCOME = "identificationIndexView";
	
	String MENU_ITEM_ENROLLMENT = "Enrollement";
	
	static TabMenu instantiateTabMenu() {
		return TabMenu.build(TabMenu.ConfiguratorImpl.FIELD_TABS_NAMES,List.of(MENU_ITEM_ENROLLMENT)
				,TabMenu.FIELD_LISTENER,new TabMenu.Listener.AbstractImpl() {
				@Override
				public Map<Object, Object> getTabArguments(String name) {
					Map<Object, Object> arguments = super.getTabArguments(name);
					if(MENU_ITEM_ENROLLMENT.equals(name)) {
						arguments.put(Tab.FIELD_ICON, "fa fa-user");
						MenuModel model = new DefaultMenuModel();
						DefaultMenuItem item = new DefaultMenuItem("Créer mon compte");
						item.setOutcome("actorCreateFromPublicView");
						model.addElement(item);
						
						item = new DefaultMenuItem("Nouvelle demande");
						item.setOutcome("publicRequestEditView");
						model.addElement(item);
						
						item = new DefaultMenuItem("Ouvrir demande");
						item.setOutcome("publicRequestReadView");
						model.addElement(item);
						
						item = new DefaultMenuItem("Obtenir jeton");
						item.setOutcome("publicRequestSendAccessTokenView");
						model.addElement(item);
						
						arguments.put(Tab.FIELD_MENU, Menu.build(Menu.FIELD_MODEL,model));
					}
					return arguments;
				}
			});
	}	
}