package ci.gouv.dgbf.system.actor.client.controller.impl.account;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ci.gouv.dgbf.system.actor.client.controller.api.FunctionController;
import ci.gouv.dgbf.system.actor.client.controller.entities.Function;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ActorListAdministrateurPage extends AbstractActorListPage implements Serializable {

	@Override
	protected Function getFunctionFromRequestParameterEntity() {
		return __inject__(FunctionController.class).readByBusinessIdentifier(ci.gouv.dgbf.system.actor.server.persistence.entities.Function.CODE_ADMINISTRATEUR);
	}
}