package ci.gouv.dgbf.system.actor.client.controller.impl.mea;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class MeaIndexPage extends AbstractPageContainerManagedImpl implements MeaTheme,Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "MEA Index";
	}
}