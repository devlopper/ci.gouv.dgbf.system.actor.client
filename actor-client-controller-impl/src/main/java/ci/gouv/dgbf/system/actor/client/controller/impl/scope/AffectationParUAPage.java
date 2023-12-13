package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationParUAPage extends AffectationPage {

	public AffectationParUAPage() {
		outcome = OUTCOME;
	}
	
	@Override
	protected void __listenBeforePostConstruct__() {
		super.__listenBeforePostConstruct__();
		assignmentsFilterController.setAdministrativeUnitRequired(true);
	}
	
	public static final String OUTCOME = "affectationParUAView";
}
