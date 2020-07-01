package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ProfilePrivilege extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private Profile profile;
	private Privilege privilege;
	private Boolean visible;

	@Override
	public String toString() {
		return profile+"|"+privilege+"|"+visible;
	}
	
	public static final String FIELD_PROFILE = "profile";
	public static final String FIELD_PRIVILEGE = "privilege";
	public static final String FIELD_VISIBLE = "visible";
}