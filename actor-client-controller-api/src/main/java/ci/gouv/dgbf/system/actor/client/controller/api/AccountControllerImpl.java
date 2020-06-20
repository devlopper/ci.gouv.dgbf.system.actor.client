package ci.gouv.dgbf.system.actor.client.controller.api;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;

import ci.gouv.dgbf.system.actor.client.controller.entities.Account;
import org.cyk.utility.client.controller.AbstractControllerEntityImpl;

@ApplicationScoped
public class AccountControllerImpl extends AbstractControllerEntityImpl<Account> implements AccountController,Serializable {
	private static final long serialVersionUID = 1L;
	
}
