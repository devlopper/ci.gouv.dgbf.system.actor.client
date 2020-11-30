package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBooleanButton;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.client.controller.component.annotation.InputNumber;
import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class IdentificationFormAttribute extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo @NotNull
	private IdentificationForm form;
	private String formAsString;
	
	@Input @InputChoice @InputChoiceOne @InputChoiceOneCombo @NotNull
	private IdentificationAttribute attribute;
	private String attributeAsString;
	
	@Input @InputNumber
	private Integer orderNumber;
	
	@Input @InputBooleanButton
	private Boolean required;	
	private String requiredAsString;
	
	/**/
	
	public static final String FIELD_FORM = "form";
	public static final String FIELD_FORM_AS_STRING = "formAsString";
	public static final String FIELD_ATTRIBUTE = "attribute";
	public static final String FIELD_ATTRIBUTE_AS_STRING = "attributeAsString";
	public static final String FIELD_ORDER_NUMBER = "orderNumber";
	public static final String FIELD_REQUIRED = "required";
	public static final String FIELD_REQUIRED_AS_STRING = "requiredAsString";
}