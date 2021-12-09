package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringAuditedImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class RequestScopeFunction extends AbstractDataIdentifiableSystemStringAuditedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Collection<String> scopeFunctionsIdentifiers;
	private String scopeFunctionString;
	private String electronicMailAddress;
	private Boolean granted;
	private String grantedString;
	private String sectionString;
	private String administrativeUnitString;
	private String firstName;
	private String lastNames;
	private String administrativeUnitFunction;
	private String mobilePhoneNumber;
	private String officePhoneNumber;
	private String postalBoxAddress;
	private String actOfAppointmentReference;
	private String registrationNumber;
	private String signatureSpecimenReadReportURIQuery;
	
	public static final String FIELD_SECTION_STRING = "sectionString";
	public static final String FIELD_ADMINISTRATIVE_UNIT_STRING = "administrativeUnitString";
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAMES = "lastNames";
	public static final String FIELD_ELECTRONIC_MAIL_ADDRESS = "electronicMailAddress";
	public static final String FIELD_SCOPE_FUNCTION_STRING = "scopeFunctionString";
	public static final String FIELD_GRANTED = "granted";
	public static final String FIELD_GRANTED_STRING = "grantedString";
	public static final String FIELD_ADMINISTRATIVE_UNIT_FUNCTION = "administrativeUnitFunction";
	public static final String FIELD_MOBILE_PHONE_NUMBER = "mobilePhoneNumber";
	public static final String FIELD_OFFICE_PHONE_NUMBER = "officePhoneNumber";
	public static final String FIELD_POSTAL_BOX_ADDRESS = "postalBoxAddress";
	public static final String FIELD_ACT_OF_APPOINTMENT_REFERENCE = "actOfAppointmentReference";
	public static final String FIELD_REGISTRATION_NUMBER = "registrationNumber";
	
}