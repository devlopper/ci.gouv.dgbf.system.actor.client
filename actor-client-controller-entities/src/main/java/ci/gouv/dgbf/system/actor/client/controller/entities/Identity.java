package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.cyk.utility.__kernel__.constant.ConstantEmpty;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.data.AbstractDataIdentifiableSystemStringImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Identity extends AbstractDataIdentifiableSystemStringImpl implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull private String firstName;
	@NotNull private String lastNames;
	private String names;
	@NotNull private String electronicMailAddress;	
	
	private String registrationNumber;
	private String postalBox;
	private String mobilePhoneNumber;
	private String officePhoneNumber;
	private String officePhoneExtension;
	private AdministrativeUnit administrativeUnit;
	private String administrativeFunction;
	private Civility civility;
	private IdentityGroup group;
	private String actOfAppointmentReference;
	private String actOfAppointmentSignatory;
	private String actOfAppointmentSignatureDateAsString;
	private Long actOfAppointmentSignatureTimestamp;
	
	public String getNames() {
		if(names == null) {
			if(StringHelper.isNotBlank(firstName))
				names = firstName;
			if(StringHelper.isNotBlank(lastNames))
				if(StringHelper.isBlank(names))
					names = lastNames;
				else
					names += " "+lastNames;
			if(names == null)
				names = ConstantEmpty.STRING;
		}
		return names;
	}
}