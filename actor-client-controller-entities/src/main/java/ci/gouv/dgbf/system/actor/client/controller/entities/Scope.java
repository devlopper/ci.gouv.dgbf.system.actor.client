package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Scope extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ScopeType type;	
	private Scope section;
	private String sectionAsString;

	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_SECTION = "section";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
}