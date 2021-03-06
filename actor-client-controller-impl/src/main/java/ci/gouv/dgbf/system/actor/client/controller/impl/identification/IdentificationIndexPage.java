package ci.gouv.dgbf.system.actor.client.controller.impl.identification;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class IdentificationIndexPage extends AbstractPageContainerManagedImpl implements IdentificationTheme, Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Module d'identification des acteurs";
	}
	
}
