package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ScopeType extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input
	private Byte orderNumber;
	
	public Boolean isCodeEqualsSECTION() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_SECTION.equals(code);
	}
	
	public Boolean isCodeEqualsACTION() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTION.equals(code);
	}
	
	public Boolean isCodeEqualsUSB() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_USB.equals(code);
	}
	
	public Boolean isCodeEqualsACTIVITE() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_ACTIVITE.equals(code);
	}
	
	public Boolean isCodeEqualsCATEGORIE_ACTIVITE() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_CATEGORIE_ACTIVITE.equals(code);
	}
	
	public Boolean isCodeEqualsIMPUTATION() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_IMPUTATION.equals(code);
	}
	
	public Boolean isCodeEqualsUA() {
		return ci.gouv.dgbf.system.actor.server.persistence.entities.ScopeType.CODE_UA.equals(code);
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
	
	public static Boolean isCodeEqualsUSB(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsUSB();
	}
	
	public static Boolean isCodeEqualsACTION(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsACTION();
	}
	
	public static Boolean isCodeEqualsACTIVITE(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsACTIVITE();
	}
	
	public static Boolean isCodeEqualsCATEGORIE_ACTIVITE(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsCATEGORIE_ACTIVITE();
	}
	
	public static Boolean isCodeEqualsIMPUTATION(ScopeType scopeType) {
		return scopeType != null && scopeType.isCodeEqualsIMPUTATION();
	}
	
	public static final String FIELD_ORDER_NUMBER = "orderNumber";
}