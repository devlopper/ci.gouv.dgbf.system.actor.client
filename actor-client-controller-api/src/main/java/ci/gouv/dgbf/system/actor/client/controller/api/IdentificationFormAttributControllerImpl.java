package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.IdentificationFormAttribut;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class IdentificationFormAttributControllerImpl extends AbstractControllerEntityImpl<IdentificationFormAttribut> implements IdentificationFormAttributController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
