package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;
import java.util.Collection;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableAuditedImpl;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Choices.Count;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.Input;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputBooleanButton;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoice;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOne;
import org.cyk.utility.__kernel__.object.__static__.controller.annotation.InputChoiceOneCombo;
import org.cyk.utility.client.controller.component.annotation.InputNumber;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Profile extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableAuditedImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Input @InputChoice(choices = @Choices(count = Count.ALL)) @InputChoiceOne @InputChoiceOneCombo
	private ProfileType type;	
	private String typeIdentifier;
	
	private String profileIdentifier;
	private Collection<String> privilegesAsStrings;
	private Collection<String> creatablePrivilegesIdentifiers;
	private Collection<String> deletablePrivilegesIdentifiers;
	
	@Input @InputNumber
	private Byte orderNumber;
	
	@Input @InputBooleanButton
	private Boolean requestable;
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_TYPE_IDENTIFIER = "typeIdentifier";
	public static final String FIELD_ORDER_NUMBER = "orderNumber";
	public static final String FIELD_REQUESTABLE = "requestable";
	public static final String FIELD_PRIVILEGES_AS_STRINGS = "privilegesAsStrings";
}