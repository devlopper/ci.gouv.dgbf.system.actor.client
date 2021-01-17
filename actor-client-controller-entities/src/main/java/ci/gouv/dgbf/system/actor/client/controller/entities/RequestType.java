package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBoolean;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBooleanButton;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputText;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class RequestType extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo 
	@NotNull 
	private IdentificationForm form;
	private String formAsString;
	
	@Input @InputText 
	private String reportIdentifier;
	
	@Input @InputText 
	private String creditManagerSignatureSpecimenReportIdentifier;
	
	@Input @InputText 
	private String authorizingOfficerSignatureSpecimenReportIdentifier;
	
	@Input @InputBoolean @InputBooleanButton
	private Boolean authenticationRequired;
	private String authenticationRequiredAsString;
	
	/**/
	
	public static final String FIELD_FORM = "form";
	public static final String FIELD_FORM_AS_STRING = "formAsString";
	public static final String FIELD_REPORT_IDENTIFIER = "reportIdentifier";
	public static final String FIELD_AUTHENTICATION_REQUIRED = "authenticationRequired";	
	public static final String FIELD_AUTHENTICATION_REQUIRED_AS_STRING = "authenticationRequiredAsString";
}