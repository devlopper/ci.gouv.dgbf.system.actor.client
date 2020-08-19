package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.model.TabMenu;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.impl.identification.IdentificationTheme;
import ci.gouv.dgbf.system.actor.client.controller.impl.myaccount.MyAccountTheme;

@ci.gouv.dgbf.system.actor.server.annotation.System
public class ThemeManagerImpl extends org.cyk.user.interface_.theme.web.jsf.primefaces.atlantis.dgbf.ThemeManagerImpl implements Serializable {

	private TabMenu identificationTabMenu,myAccountTabMenu;
	
	@Override
	public TabMenu getLeftTabMenu(AbstractPageContainerManagedImpl page) {
		if(page instanceof IdentificationTheme) {
			if(identificationTabMenu == null)
				identificationTabMenu = IdentificationTheme.instantiateTabMenu();
			return identificationTabMenu;
		}
		if(page instanceof MyAccountTheme) {
			if(myAccountTabMenu == null)
				myAccountTabMenu = MyAccountTheme.instantiateTabMenu(page);
			return myAccountTabMenu;
		}
		return super.getLeftTabMenu(page);
	}
	
	@Override
	public String getApplicationName(AbstractPageContainerManagedImpl page) {
		if(page instanceof IdentificationTheme)
			return IdentificationTheme.APPLICATION_NAME;
		if(page instanceof MyAccountTheme)
			return MyAccountTheme.APPLICATION_NAME;
		return super.getApplicationName(page);
	}
	
	@Override
	public String getApplicationOutcome(AbstractPageContainerManagedImpl page) {
		if(page instanceof IdentificationTheme)
			return IdentificationTheme.APPLICATION_OUTCOME;
		if(page instanceof MyAccountTheme)
			return MyAccountTheme.APPLICATION_OUTCOME;
		return super.getApplicationOutcome(page);
	}
}