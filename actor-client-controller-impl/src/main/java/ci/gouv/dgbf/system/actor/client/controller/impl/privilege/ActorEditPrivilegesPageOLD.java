package ci.gouv.dgbf.system.actor.client.controller.impl.privilege;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorEditPrivilegesPageOLD extends AbstractProfileEditPrivilegesPage implements Serializable {

	@Override
	protected String __getWindowTitleValue__() {
		return "Assignation de privilèges";
	}
}