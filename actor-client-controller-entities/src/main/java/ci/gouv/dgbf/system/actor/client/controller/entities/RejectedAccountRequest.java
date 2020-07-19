package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class RejectedAccountRequest extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String firstName;
	private String lastNames;
	private String electronicMailAddress;
	private String reason;
	private String requestDateAsString;
	private String dateAsString;
	private String names;
	
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAMES = "lastNames";
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS = "electronicMailAddress";
	public static final String FIELD_REASON = "reason";
	public static final String FIELD_REQUEST_DATE_AS_STRING = "requestDateAsString";
	public static final String FIELD_DATE_AS_STRING = "dateAsString";
	public static final String FIELD_NAMES = "names";
}