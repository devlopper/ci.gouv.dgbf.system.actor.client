package ci.gouv.dgbf.system.actor.client.controller.impl.identification;
import java.io.Serializable;
import java.util.List;

import javax.inject.Named;

import org.cyk.utility.client.controller.component.window.WindowBuilder;

import lombok.Getter;
import lombok.Setter;

@Named @Getter @Setter
public abstract class AbstractPageContainerManagedImpl extends org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	protected WindowBuilder __getWindowBuilder__(List<String> subDurations) {
		WindowBuilder windowBuilder = super.__getWindowBuilder__(subDurations);
		windowBuilder.getApplicationName(Boolean.TRUE).setValue("Module d'identification des acteurs");
		return windowBuilder;
	}	
}