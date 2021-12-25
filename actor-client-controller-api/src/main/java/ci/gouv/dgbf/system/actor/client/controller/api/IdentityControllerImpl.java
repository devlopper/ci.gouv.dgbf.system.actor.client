package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.Identity;
import ci.gouv.dgbf.system.actor.server.representation.api.IdentityRepresentation;

@ApplicationScoped
public class IdentityControllerImpl extends AbstractControllerEntityImpl<Identity> implements IdentityController,Serializable {
	private static final long serialVersionUID = 1L;

	@Override
	public String encryptElectroncicMailAddress(String electronicMailAddress) {
		return IdentityRepresentation.getProxy().encryptElectroncicMailAddress(electronicMailAddress).readEntity(String.class);
	}

	@Override
	public String decryptElectroncicMailAddress(String encryptElectronicMailAddress) {
		return IdentityRepresentation.getProxy().decryptElectroncicMailAddress(encryptElectronicMailAddress).readEntity(String.class);
	}
	
}
