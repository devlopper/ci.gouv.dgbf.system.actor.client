package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringAuditedImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class RequestScopeFunction extends AbstractDataIdentifiableSystemStringAuditedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Collection<String> scopeFunctionsIdentifiers;
	
}