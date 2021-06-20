package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;

import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class AbstractAssignmentsReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	protected AssignmentsReadController readController;
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		instantiateController();
		readController.initialize();
		readController.build();
	}
	
	protected void instantiateController() {
		readController = new AssignmentsReadController();
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		if(readController == null || readController.getAssignments() == null)
			return super.__getWindowTitleValue__();
		return readController.getAssignments().getActivityAsString()+" | "+readController.getAssignments().getEconomicNatureAsString();
	}
}
