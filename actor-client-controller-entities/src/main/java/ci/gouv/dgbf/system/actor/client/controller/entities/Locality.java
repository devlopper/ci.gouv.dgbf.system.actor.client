package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import ci.gouv.dgbf.system.actor.server.persistence.entities.Locality.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Locality extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Type type;
	private Locality parent;
	
	private String regionIdentifier;
	private String regionCodeName;
	
	private String departmentIdentifier;
	private String departmentCodeName;
	
	private Locality region;
	private Locality department;
	
	@Override
	public String toString() {
		return code+" "+name;
	}
}