package ci.gouv.dgbf.system.actor.client.controller.impl.privilege.das;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ProfileDasIndexPage extends AbstractPageContainerManagedImpl implements Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Module de gestion de l'exécution";
	}
}