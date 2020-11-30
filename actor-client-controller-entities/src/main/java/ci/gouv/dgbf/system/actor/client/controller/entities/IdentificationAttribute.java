package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class IdentificationAttribute extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean required;
	private String requiredAsString;
	private Integer orderNumber;

	public static final String FIELD_REQUIRED = "required";
	public static final String FIELD_REQUIRED_AS_STRING = "requiredAsString";
	public static final String FIELD_ORDER_NUMBER = "orderNumber";
}