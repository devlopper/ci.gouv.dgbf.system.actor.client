package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.FinancialControllerService;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class FinancialControllerServiceControllerImpl extends AbstractControllerEntityImpl<FinancialControllerService> implements FinancialControllerServiceController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
