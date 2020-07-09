package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ScopeType extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public Boolean isCodeEqualsUA() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_UA.equals(code);
	}
	
	public Boolean isCodeEqualsSECTION() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION.equals(code);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**/
	
	public static Boolean isCodeEqualsUA(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsUA();
	}
	
	public static Boolean isCodeEqualsSECTION(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsSECTION();
	}
}