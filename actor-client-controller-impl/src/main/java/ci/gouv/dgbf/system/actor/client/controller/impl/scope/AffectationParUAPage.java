package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.actor.client.controller.impl.function.AssignmentsFilterController;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class AffectationParUAPage extends AffectationPage {

	public AffectationParUAPage() {
		outcome = OUTCOME;
	}
		
	@Override
	protected AssignmentsFilterController instantiateFilterController() {
		AssignmentsFilterController filterController = super.instantiateFilterController();
		filterController.setAdministrativeUnitRequired(true);
		return filterController;
	}
	
	public static final String OUTCOME = "affectationParUAView";
}
