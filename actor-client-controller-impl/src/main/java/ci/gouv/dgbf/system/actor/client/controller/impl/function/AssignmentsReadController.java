package ci.gouv.dgbf.system.actor.client.controller.impl.function;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.cyk.utility.__kernel__.identifier.resource.ParameterName;
import org.cyk.utility.__kernel__.map.MapHelper;
import org.cyk.utility.__kernel__.string.StringHelper;
import org.cyk.utility.client.controller.web.WebController;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Cell;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.layout.Layout;
import org.cyk.utility.client.controller.web.jsf.primefaces.model.output.OutputText;
import org.cyk.utility.controller.EntityReader;

import ci.gouv.dgbf.system.actor.client.controller.entities.Assignments;
import ci.gouv.dgbf.system.actor.server.persistence.api.query.AssignmentsQuerier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain=true)
public class AssignmentsReadController implements Serializable {

	private Assignments assignments;
	private Layout layout;
	
	public AssignmentsReadController initialize() {
		if(assignments == null)
			assignments = EntityReader.getInstance().readOne(Assignments.class, AssignmentsQuerier.QUERY_IDENTIFIER_READ_BY_IDENTIFIER_FOR_UI
					, new String[] {ci.gouv.dgbf.system.actor.server.persistence.entities.Assignments.FIELD___ALL__}
			, AssignmentsQuerier.PARAMETER_NAME_IDENTIFIER,WebController.getInstance().getRequestParameter(ParameterName.ENTITY_IDENTIFIER));			
		return this;
	}
	
	public AssignmentsReadController build() {
		if(assignments == null)
			return this;
		Collection<Map<Object,Object>> cellsMaps = new ArrayList<>();
		addLabelValue(cellsMaps, "Section", assignments.getSectionAsString());
		addLabelValue(cellsMaps, "Unité administrative", assignments.getAdministrativeUnitAsString());
		addLabelValue(cellsMaps, "Dotation/Programme", assignments.getBudgetSpecializationUnitAsString());
		addLabelValue(cellsMaps, "Action", assignments.getActionAsString());
		addLabelValue(cellsMaps, "Activité", assignments.getActivityAsString());
		addLabelValue(cellsMaps, "Nature économique", assignments.getEconomicNatureAsString());
		addLabelValue(cellsMaps, "Nature de dépense", assignments.getExpenditureNatureAsString());
		addLabelValue(cellsMaps, "Catégorie d'activité", assignments.getActivityCategoryAsString());
		
		addLabelValue(cellsMaps, "Gestionnaire de crédits", assignments.getCreditManagerHolderAsString());
		addLabelValue(cellsMaps, "Assistant gestionnaire de crédits", assignments.getCreditManagerAssistantAsString());
		addLabelValue(cellsMaps, "Ordonnateur", assignments.getAuthorizingOfficerHolderAsString());
		addLabelValue(cellsMaps, "Assistant ordonnateur", assignments.getAuthorizingOfficerAssistantAsString());
		addLabelValue(cellsMaps, "Controleur financier", assignments.getFinancialControllerHolderAsString());
		addLabelValue(cellsMaps, "Assistant controleur financier", assignments.getFinancialControllerAssistantAsString());
		addLabelValue(cellsMaps, "Comptable", assignments.getAccountingHolderAsString());
		addLabelValue(cellsMaps, "Assistant comptable", assignments.getAccountingAssistantAsString());
		
		layout = Layout.build(Layout.FIELD_CELL_WIDTH_UNIT,Cell.WidthUnit.FLEX,Layout.ConfiguratorImpl.FIELD_LABEL_VALUE,Boolean.TRUE
				,Layout.ConfiguratorImpl.FIELD_CELLS_MAPS,cellsMaps);
		return this;
	}
	
	protected void addLabelValue(Collection<Map<Object,Object>> cellsMaps,String label,String value) {
		OutputText valueOutputText = OutputText.buildFromValue(StringHelper.isBlank(value) ? "---" : value);
		valueOutputText.setEscape(Boolean.FALSE);
		addLabelControl(cellsMaps, label, valueOutputText);
	}
	
	protected void addLabelControl(Collection<Map<Object,Object>> cellsMaps,String label,Object control) {
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,OutputText.buildFromValue(label+StringUtils.repeat("&nbsp;",2)+":"+StringUtils.repeat("&nbsp;",2))
				.setEscape(Boolean.FALSE),Cell.FIELD_WIDTH));
		cellsMaps.add(MapHelper.instantiate(Cell.FIELD_CONTROL,control,Cell.FIELD_WIDTH));
	}
}