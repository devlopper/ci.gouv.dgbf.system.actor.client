package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class ScopeFunction extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Scope scope;
	private String scopeAsString;
	private Function function;
	private String functionAsString;
	private Integer numberOfActor;
	private Boolean shared;
	private String sharedAsString;
	
	public static final String FIELD_SCOPE = "scope";
	public static final String FIELD_FUNCTION = "function";
	public static final String FIELD_SCOPE_AS_STRING = "scopeAsString";
	public static final String FIELD_FUNCTION_AS_STRING = "functionAsString";
	public static final String FIELD_NUMBER_OF_ACTOR = "numberOfActor";
	public static final String FIELD_SHARED = "shared";
	public static final String FIELD_SHARED_AS_STRING = "sharedAsString";
}