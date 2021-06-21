package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Named @ViewScoped
@Getter @Setter @Accessors(chain=true)
public class AssignmentsReadHistoryPage extends AbstractAssignmentsReadPage implements Serializable {

	@Override
	protected void instantiateController() {
		super.instantiateController();
		readController.setHistoryReadable(Boolean.TRUE);
	}
	
	@Override
	protected String __getWindowTitleValue__() {
		return "Historique - "+super.__getWindowTitleValue__();
	}
	
	public static final String OUTCOME = "assignmentsReadHistoryView";
}