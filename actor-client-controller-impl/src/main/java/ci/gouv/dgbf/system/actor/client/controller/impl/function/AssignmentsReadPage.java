package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped
@Getter @Setter @Accessors(chain=true)
public class AssignmentsReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private AssignmentsReadController readController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		readController = new AssignmentsReadController();
		readController.initialize();
		readController.build();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(readController == null || readController.getAssignments() == null)
			return super.__getWindowTitleValue__();
		return readController.getAssignments().getActivityAsString()+" | "+readController.getAssignments().getEconomicNatureAsString();
	}
}
