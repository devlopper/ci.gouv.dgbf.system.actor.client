package ci.gouv.dgbf.system.actor.client.controller.impl;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AccountRequestNotifyAfterCreatePage extends AbstractPageContainerManagedImpl implements Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Demande de compte";
	}
	
}
