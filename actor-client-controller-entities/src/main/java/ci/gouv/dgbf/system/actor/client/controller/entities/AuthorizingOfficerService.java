package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class AuthorizingOfficerService extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
}