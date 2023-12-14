package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestParUAIndexPage extends RequestIndexPage {

	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		outcome = OUTCOME;
		if(requestFilterController != null) {
			requestFilterController.setAdministrativeUnitRequired(true);
		}
	}
	
	public static final String OUTCOME = "requestParUAIndexView";
}
