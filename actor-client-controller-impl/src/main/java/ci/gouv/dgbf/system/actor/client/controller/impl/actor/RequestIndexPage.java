package ci.gouv.dgbf.system.actor.client.controller.impl.actor;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class RequestIndexPage extends AbstractRequestIndexPage implements Serializable {
	
	public RequestIndexPage() {
		outcome = OUTCOME;
	}
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		requestFilterController.setAdministrativeUnitRequired(false);
	}
	
	public static final String OUTCOME = "requestIndexView";
}