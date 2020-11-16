package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationForm;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class IdentificationFormControllerImpl extends AbstractControllerEntityImpl<IdentificationForm> implements IdentificationFormController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
