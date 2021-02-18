package ci.gouv.dgbf.system.actor.client.controller.impl.scope;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.AbstractPageContainerManagedImpl;

import ci.gouv.dgbf.system.actor.client.controller.entities.ScopeFunction;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.ScopeFunctionQuerier;
import lombok.Getter;
import lombok.Setter;

@Named @ViewScoped @Getter @Setter
public class ScopeFunctionReadPage extends AbstractPageContainerManagedImpl implements Serializable {

	private ScopeFunction scopeFunction;
	
	@Override
	protected void __listenPostConstruct__() {
		scopeFunction = WebController.getInstance().getUsingRequestParameterAsSystemIdentifierByQueryIdentifier(ScopeFunction.class
				, ScopeFunctionQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI);
		super.__listenPostConstruct__();
	}
	
}