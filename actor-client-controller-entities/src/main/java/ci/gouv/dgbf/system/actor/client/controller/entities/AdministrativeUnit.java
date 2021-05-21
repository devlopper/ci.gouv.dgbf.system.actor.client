package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class AdministrativeUnit extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Section section;	
	private String sectionCodeName;
	private String sectionAsString;
	private String sectionIdentifier;
	
	private Locality subPrefecture;
	private Locality department;
	private Locality region;
	
	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_SECTION = "section";
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	
}