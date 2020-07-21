package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.ThemeManager;
import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.impl.identification.IdentificationTheme;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class ThemeManagerImpl extends ThemeManager.AbstractImpl implements Serializable {

	private TabMenu identificationTabMenu;
	
	@Override
	public TabMenu getLeftTabMenu(AbstractPageContainerManagedImpl page) {
		if(page instanceof IdentificationTheme) {
			if(identificationTabMenu == null)
				identificationTabMenu = IdentificationTheme.instantiateTabMenu();
			return identificationTabMenu;
		}
		return super.getLeftTabMenu(page);
	}
	
	@Override
	public String getApplicationName(AbstractPageContainerManagedImpl page) {
		if(page instanceof IdentificationTheme)
			return IdentificationTheme.APPLICATION_NAME;
		return super.getApplicationName(page);
	}
}