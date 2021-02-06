package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Activity extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private Section section;
	private String sectionIdentifier;
	private String sectionCodeName;
	
	private BudgetSpecializationUnit budgetSpecializationUnit;
	private String budgetSpecializationUnitIdentifier;
	private String budgetSpecializationUnitCodeName;
	
	private Action action;
	private String actionIdentifier;
	private String actionCodeName;
	
	private ExpenditureNature expenditureNature;
	private String expenditureNatureIdentifier;
	private String expenditureNatureCodeName;
	
	private ActivityCategory category;
	private String categoryIdentifier;
	private String categoryCodeName;
	
	private AdministrativeUnit administrativeUnit;
	private String administrativeUnitIdentifier;
	private String administrativeUnitCodeName;
	
	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_SECTION_CODE_NAME = "sectionCodeName";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_CODE_NAME = "budgetSpecializationUnitCodeName";	
	public static final String FIELD_ACTION_CODE_NAME = "actionCodeName";
}