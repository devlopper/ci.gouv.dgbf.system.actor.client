package ci.gouv.dgbf.system.actor.client.controller.entities;

import java.io.Serializable;

import org.cyk.utility.__kernel__.object.__static__.controller.AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @NoArgsConstructor @Accessors(chain=true)
public class Scope extends AbstractDataIdentifiableSystemStringIdentifiableBusinessStringNamableImpl implements Serializable {
	private static final long serialVersionUID = 1L;

	private ScopeType type;	
	private Scope section;
	private Boolean visible;
	private String sectionAsString,budgetSpecializationUnitAsString,actionAsString,activityAsString,activityCategoryAsString,economicNatureAsString,visibleAsString;
	
	@Override
	public String toString() {
		return code+" "+name;
	}
	
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_SECTION = "section";
	public static final String FIELD_SECTION_AS_STRING = "sectionAsString";
	public static final String FIELD_BUDGET_SPECIALIZATION_UNIT_AS_STRING = "budgetSpecializationUnitAsString";
	public static final String FIELD_ACTION_AS_STRING = "actionAsString";
	public static final String FIELD_ACTIVITY_AS_STRING = "activityAsString";
	public static final String FIELD_ACTIVITY_CATEGORY_AS_STRING = "activityCategoryAsString";
	public static final String FIELD_ECONOMIC_NATURE_AS_STRING = "economicNatureAsString";
	public static final String FIELD_VISIBLE = "visible";
	public static final String FIELD_VISIBLE_AS_STRING = "visibleAsString";
}